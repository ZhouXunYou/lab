package lab.storm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;

import net.opentsdb.core.DataPoint;
import net.opentsdb.core.DataPoints;
import net.opentsdb.core.Query;
import net.opentsdb.core.SeekableView;
import net.opentsdb.core.TSDB;
import net.opentsdb.core.TSQuery;
import net.opentsdb.core.TSSubQuery;
import net.opentsdb.query.filter.TagVFilter;
import net.opentsdb.search.SearchQuery;
import net.opentsdb.uid.NoSuchUniqueName;
import net.opentsdb.uid.UniqueId.UniqueIdType;
import net.opentsdb.utils.Config;
import net.opentsdb.utils.DateTime;

public class TestOpenTSDB {
	private static String pathToConfigFile = "C:\\Users\\ZhouXunYou\\Desktop\\opentsdb.conf";
	private static void query1() throws Exception {
		final Config config = new Config(pathToConfigFile);

		final TSDB tsdb = new TSDB(config);
		Deferred<SearchQuery> deferreds = tsdb.executeSearch(new SearchQuery("my.tsdb.test.metric"));
//		deferreds.
//		TSQuery tsQuery = new TSQuery();
//		tsQuery.setStart("5h-ago");
//		TSSubQuery subQuery = new TSSubQuery();
//		
////		subQuery.setAggregator("sum");
//		subQuery.setAggregator("sum");
//		subQuery.setMetric("my.tsdb.test.metric");
//		subQuery.setFilters(new ArrayList<>());
//		ArrayList<TSSubQuery> queries = new ArrayList<>();
//		
//		tsQuery.setQueries(queries);
//		
//		
//		System.out.println("++"+tsQuery.buildQueries(tsdb).length);
//		
//		
//		for(Query query:tsQuery.buildQueries(tsdb)) {
//			System.out.println("------------------");
//			System.out.println(query.runAsync());
////			for(DataPoints dataPoint:query.run()) {
////				System.out.println(dataPoint.longValue(0));
////			}
//		}
		
//		Query query = tsdb.newQuery();
//		query.setStartTime(0);
//		query.setEndTime(System.currentTimeMillis());
//		
//		System.out.println(query.run().length);
		tsdb.shutdown().join();
		
	}
	private static void query() throws Exception {
		final Config config = new Config(pathToConfigFile);
		final TSDB tsdb = new TSDB(config);
		final TSQuery query = new TSQuery();
		query.setStart("1h-ago");
		final TSSubQuery subQuery = new TSSubQuery();
		subQuery.setMetric("my.tsdb.test.metric");

		final List<TagVFilter> filters = new ArrayList<TagVFilter>(1);
		filters.add(new TagVFilter.Builder().setType("literal_or").setFilter("example1").setTagk("script").setGroupBy(false).build());
		subQuery.setFilters(filters);
//		subQuery.setAggregator("sum");

		// IMPORTANT: don't forget to add the subQuery
		final ArrayList<TSSubQuery> subQueries = new ArrayList<TSSubQuery>(1);
		subQueries.add(subQuery);
		query.setQueries(subQueries);
		query.setMsResolution(true); // otherwise we aggregate on the second.

		// make sure the query is valid. This will throw exceptions if something
		// is missing
		query.validateAndSetQuery();

		// compile the queries into TsdbQuery objects behind the scenes
		Query[] tsdbqueries = query.buildQueries(tsdb);

		// create some arrays for storing the results and the async calls
		final int nqueries = tsdbqueries.length;
		final ArrayList<DataPoints[]> results = new ArrayList<DataPoints[]>(nqueries);
		final ArrayList<Deferred<DataPoints[]>> deferreds = new ArrayList<Deferred<DataPoints[]>>(nqueries);

		// this executes each of the sub queries asynchronously and puts the
		// deferred in an array so we can wait for them to complete.
		for (int i = 0; i < nqueries; i++) {
			deferreds.add(tsdbqueries[i].runAsync());
		}

		// Start timer
		long startTime = DateTime.nanoTime();

		// This is a required callback class to store the results after each
		// query has finished
		class QueriesCB implements Callback<Object, ArrayList<DataPoints[]>> {
			public Object call(final ArrayList<DataPoints[]> queryResults) throws Exception {
				results.addAll(queryResults);
				return null;
			}
		}

		// Make sure to handle any errors that might crop up
//		class QueriesEB implements Callback<Object, Exception> {
//			@Override
//			public Object call(final Exception e) throws Exception {
//				System.err.println("Queries failed");
//				e.printStackTrace();
//				return null;
//			}
//		}

		// this will cause the calling thread to wait until ALL of the queries
		// have completed.
		try {
			Deferred.groupInOrder(deferreds).addCallback(new QueriesCB()).join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// End timer.
		double elapsedTime = DateTime.msFromNanoDiff(DateTime.nanoTime(), startTime);
		System.out.println("Query returned in: " + elapsedTime + " milliseconds.");

		// now all of the results are in so we just iterate over each set of
		// results and do any processing necessary.
		for (final DataPoints[] dataSets : results) {
			for (final DataPoints data : dataSets) {
				System.out.print(data.metricName());
				Map<String, String> resolvedTags = data.getTags();
				for (final Map.Entry<String, String> pair : resolvedTags.entrySet()) {
					System.out.print(" " + pair.getKey() + "=" + pair.getValue());
				}
				System.out.print("\n");

				final SeekableView it = data.iterator();
				while (it.hasNext()) {
					final DataPoint dp = it.next();
					System.out.println(
							"  " + dp.timestamp() + " " + (dp.isInteger() ? dp.longValue() : dp.doubleValue()));
				}
				System.out.println("");
			}
		}

		// Gracefully shutdown connection to TSDB
		try {
			tsdb.shutdown().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void write() throws Exception {
		// processArgs(args);
		// Create a config object with a path to the file for parsing. Or manually
		// override settings.
		// e.g. config.overrideConfig("tsd.storage.hbase.zk_quorum", "localhost");
		final Config config;
		if (pathToConfigFile != null && !pathToConfigFile.isEmpty()) {
			config = new Config(pathToConfigFile);
		} else {
			// Search for a default config from /etc/opentsdb/opentsdb.conf, etc.
			config = new Config(true);
		}
		// config.
		final TSDB tsdb = new TSDB(config);

		// Declare new metric
		String metricName = "my.tsdb.test.metric";
		// First check to see it doesn't already exist
		byte[] byteMetricUID; // we don't actually need this for the first
								// .addPoint() call below.
		// TODO: Ideally we could just call a not-yet-implemented tsdb.uIdExists()
		// function.
		// Note, however, that this is optional. If auto metric is enabled
		// (tsd.core.auto_create_metrics), the UID will be assigned in call to
		// addPoint().
		try {
			byteMetricUID = tsdb.getUID(UniqueIdType.METRIC, metricName);
		} catch (IllegalArgumentException iae) {
			System.out.println("Metric name not valid.");
			iae.printStackTrace();
			System.exit(1);
		} catch (NoSuchUniqueName nsune) {
			// If not, great. Create it.
			byteMetricUID = tsdb.assignUid("metric", metricName);
		}

		// Make a single datum
		long timestamp = System.currentTimeMillis();
		long value = 314159;
		// Make key-val
		Map<String, String> tags = new HashMap<String, String>(1);
		tags.put("script", "example1");

		// Start timer

		// Write a number of data points at 30 second intervals. Each write will
		// return a deferred (similar to a Java Future or JS Promise) that will
		// be called on completion with either a "null" value on success or an
		// exception.
		int count = 0;
		for (int l = 0; l < 1; l++) {
			long startTime1 = System.currentTimeMillis();
			int n = 10;
			ArrayList<Deferred<Object>> deferreds = new ArrayList<Deferred<Object>>(n);
			for (int i = 0; i < n; i++) {
				count++;
				tags.put("time", String.valueOf(timestamp + count));
				
				Deferred<Object> deferred = tsdb.addPoint(metricName, timestamp, new Random().nextLong(), tags);
				deferreds.add(deferred);
				// timestamp += 1;
			}

			// Add the callbacks to the deferred object. (They might have already
			// returned, btw)
			// This will cause the calling thread to wait until the add has completed.
			System.out.println("Waiting for deferred result to return...");

			Deferred.groupInOrder(deferreds).addErrback(new Callback<String, Exception>() {

				@Override
				public String call(Exception e) throws Exception {
					String message = ">>>>>>>>>>>Failure!>>>>>>>>>>>";
					System.err.println(message + " " + e.getMessage());
					e.printStackTrace();
					return null;
				}
			}).addCallback(new Callback<Object, ArrayList<Object>>() {

				@Override
				public Object call(ArrayList<Object> results) throws Exception {
					System.out.println("Successfully wrote " + results.size() + " data points");
					return null;
				}

			});
			// .addErrback(new AddDataExample().new errBack())
			// .addCallback(new AddDataExample().new succBack())
			// Block the thread until the deferred returns it's result.
			// .join();
			// Alternatively you can add another callback here or use a join with a
			// timeout argument.

			// End timer.
			long elapsedTime1 = System.currentTimeMillis() - startTime1;
			System.out.println("\nAdding " + n + " points took: " + elapsedTime1 + " milliseconds.\n");

			// Gracefully shutdown connection to TSDB. This is CRITICAL as it will
			// flush any pending operations to HBase.
		}
		tsdb.shutdown();
	}

	public static void main(final String[] args) throws Exception {
		write();
	}

	// // This is an optional errorback to handle when there is a failure.
	// class errBack implements Callback<String, Exception> {
	// public String call(final Exception e) throws Exception {
	// String message = ">>>>>>>>>>>Failure!>>>>>>>>>>>";
	// System.err.println(message + " " + e.getMessage());
	// e.printStackTrace();
	// return message;
	// }
	// };
	//
	// // This is an optional success callback to handle when there is a success.
	// class succBack implements Callback<Object, ArrayList<Object>> {
	// public Object call(final ArrayList<Object> results) {
	// System.out.println("Successfully wrote " + results.size() + " data points");
	// return null;
	// }
	// };
}
