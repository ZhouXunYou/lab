package lab.storm;

import java.util.Map;
import java.util.Random;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class MoCounter extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long count;
	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
	}

	@Override
	public void cleanup() {
		super.cleanup();
		System.out.println(count);
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		count++;
		System.out.println(count);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

//	public static void main(String[] args) {
//		for(int j=0;j<10;j++) {
//			Random random = new Random();
//			
//			int rate = 0;
//			for(int i=0;i<10000000;i++) {
//				int value = (int)(1+Math.random()*100);
//				if(value<=3) {
//					rate++;
//				}
//			}
//			System.out.println(rate/10000000d);
//		}
//	}
}
