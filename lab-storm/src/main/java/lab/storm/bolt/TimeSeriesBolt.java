package lab.storm.bolt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

//import com.stumbleupon.async.Callback;
//import com.stumbleupon.async.Deferred;

import lab.storm.model.Datapackage;
import lab.storm.model.KpiData;
//import net.opentsdb.core.TSDB;
//import net.opentsdb.utils.Config;

public class TimeSeriesBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private TSDB tsdb;
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		super.prepare(stormConf, context);
//		try {
//			Config config = new Config("C:\\Users\\ZhouXunYou\\Desktop\\opentsdb.conf");
//			tsdb = new TSDB(config);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
//		Datapackage datapackage = (Datapackage)input.getValueByField("datapackage");
//		String moId = datapackage.getMoId();
//		Long collectTime = datapackage.getCollectTime();
//		Long interval = datapackage.getInterval();
//		String moType = datapackage.getMoType();
//		String collectMethod = datapackage.getCollectMethod();
//		Map<String,List<KpiData>> kpiDatas = datapackage.getKpiDatas();
////		Map<String,List<Map<String,String>>>  kpiDatas = (Map<String,List<Map<String,String>>>)datapackage.get("kpiDatas");
//		
////		tsdb.getUID(UniqueIdType.METRIC, moId);
//		List<Deferred<Object>> deferreds = new ArrayList<Deferred<Object>>(datapackage.kpiDataCount());
////		tsdb.newBatch(moId, tags)
//		kpiDatas.forEach((kpiCode,KpiData)->{
//			KpiData.forEach(data->{
//				String instance = data.getObjectName();
//				Object value = data.getData();
//				Map<String,String> tags = new HashMap<>();
//				
//				tags.put("moType", moType);
//				tags.put("collectMethod", collectMethod);
//				tags.put("kpiCode", kpiCode);
//				tags.put("instance", instance);
//				double kpiValue = Double.valueOf(value.toString());
//				deferreds.add(tsdb.addPoint(moId, collectTime, kpiValue, tags));
////				deferreds.add(e)
//				
////				Map<String,>
////				Deferred<Object> deferred = tsdb.addPoint(moId, collectTime, value + i, tags);
//				System.out.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s", moId,collectTime,interval,moType,collectMethod,kpiCode,instance,value.toString()));
//			});
//		});
//		Deferred.groupInOrder(deferreds).addErrback(new Callback<String, Exception>() {
//			@Override
//			public String call(Exception e) throws Exception {
//				e.printStackTrace();
//				return e.getMessage();
//			}
//		});
//		tsdb.flush();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
