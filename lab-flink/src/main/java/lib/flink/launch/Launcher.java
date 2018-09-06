package lib.flink.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;


public class Launcher {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//		env.addsource
		List<String> topics = new ArrayList<>();
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "bd1:9092,bd2:9092,bd3:9092");
		// only required for Kafka 0.8
//		properties.setProperty("zookeeper.connect", "bd1:2181,bd2:2181,bd3:2181");
		properties.setProperty("group.id", "test");
//		new flinkkafkacon
		topics.add("test");
		
		
//		final OutputTag<String> outputTag = new OutputTag<String>("side-output"){
//			private static final long serialVersionUID = 1L;
//		};
//		
//		new FlinkKafkaConsumer011<>(topics, new SimpleStringSchema(), properties);
		DataStream<String> kafkaStream = env.addSource(new FlinkKafkaConsumer011<>(topics, new SimpleStringSchema(), properties));
		kafkaStream.map(value->{
//			Map<String,Integer> map = new HashMap<>();
			Tuple1<Integer> tuple = Tuple1.of(Integer.parseInt(value));
			
			return tuple;
		}).keyBy(0).sum(1).print();
		
		
//		SingleOutputStreamOperator<String> sideOut = kafkaStream.map((value)->{
//			return value.split(",");
//		}).process(new ProcessFunction<String[], String>() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void processElement(String[] values, ProcessFunction<String[], String>.Context context,
//					Collector<String> collector) throws Exception {
//				// TODO Auto-generated method stub
//				if(values[0].equals("0")) {
//					collector.collect(values[0]);
//					context.output(outputTag, values[0]);
//				}
//			}
//		});
//		
//		DataStream<String> kqi = sideOut.getSideOutput(outputTag);
//		kqi.map(line->{
//			System.out.println("kqi"+line);
//			return line;
//		});
		
		
//		OutputTag<String> output = new OutputTag<>("");
//		kafkaStream.map((line)->{
//			System.out.println(line);
//			return line;
//		});
		
//		kafkaStream.forward().flatMap(new FlatMapFunction<String, String>() {
//
//			@Override
//			public void flatMap(String value, Collector<String> collector) throws Exception {
//				System.out.println(value);
//			}
//		});
//		kafkaStream.process(new ProcessFunction<String, String>() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void processElement(String value, ProcessFunction<String, String>.Context context,
//					Collector<String> collector) throws Exception {
//				System.out.println(value);
//			}
//		});
//		kafkaStream.print()
//		kafkaStream.e
//		StreamTableEnvironment.getTableEnvironment(env);
//		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(env);
		env.execute();
	}
}
