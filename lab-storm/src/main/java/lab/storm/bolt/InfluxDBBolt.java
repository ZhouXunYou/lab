package lab.storm.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import com.stumbleupon.async.Deferred;

import lab.storm.model.Datapackage;
import lab.storm.model.KpiData;

public class InfluxDBBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		super.prepare(stormConf, context);
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Datapackage datapackage = (Datapackage)input.getValueByField("datapackage");
		String moId = datapackage.getMoId();
		Long collectTime = datapackage.getCollectTime();
		Long interval = datapackage.getInterval();
		String moType = datapackage.getMoType();
		String collectMethod = datapackage.getCollectMethod();
		Map<String,List<KpiData>> kpiDatas = datapackage.getKpiDatas();
//		Map<String,List<Map<String,String>>>  kpiDatas = (Map<String,List<Map<String,String>>>)datapackage.get("kpiDatas");
		
//		tsdb.getUID(UniqueIdType.METRIC, moId);
		List<Deferred<Object>> deferreds = new ArrayList<Deferred<Object>>(datapackage.kpiDataCount());
//		tsdb.newBatch(moId, tags)
		kpiDatas.forEach((kpiCode,KpiData)->{
			KpiData.forEach(data->{
				String instance = data.getObjectName();
				Object value = data.getData();
				
				System.out.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s", moId,collectTime,interval,moType,collectMethod,kpiCode,instance,value.toString()));
			});
		});
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
