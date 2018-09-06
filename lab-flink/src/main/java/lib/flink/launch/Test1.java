package lib.flink.launch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class Test1 {
	public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		DataSet<String> text = env.readTextFile("E:\\test\\1.txt");
		DataSet<Tuple2<String, Integer>> counts =
		        // split up the lines in pairs (2-tuples) containing: (word,1)
		        text.flatMap(new FlatMapFunction<String,Tuple2<String,Integer>>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
						out.collect(Tuple2.of(value, 1));
					}
				})
		        // group by the tuple field "0" and sum up tuple field "1"
		        .groupBy(0)
		        .sum(1);
		counts.print();
	}
}
