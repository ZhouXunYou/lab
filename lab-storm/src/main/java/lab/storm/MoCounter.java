package lab.storm;

import java.util.Map;

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

}
