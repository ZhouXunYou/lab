package lab.storm.bolt;

import java.util.List;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;

import lab.storm.model.AlarmRule;
import lab.storm.model.Datapackage;
import lab.storm.model.KpiData;

public class ThresholdBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private InfluxDB influxDB;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
		String influxdbServer = stormConf.get("influxdbServer").toString();
		String user = stormConf.get("influxdbUser").toString();
		String password = stormConf.get("influxdbPassword").toString();
		influxDB = InfluxDBFactory.connect(influxdbServer,user,password);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Datapackage datapackage = (Datapackage)input.getValueByField("datapackage");
		List<AlarmRule> alarmRules = (List<AlarmRule>)input.getValueByField("alarmRules");
		for(AlarmRule alarmRule:alarmRules) {
			/*
			 * 直接通过告警规则获取需要验证的kpi数据
			 */
			List<KpiData> kpiDatas = datapackage.getKpiDatas().get(alarmRule.getKpiCode());
			for(KpiData kpiData:kpiDatas) {
				/*
				 * 阀值规则判断，是否生成告警
				 */
			}
		}
	}
	
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		System.out.println("asdasdfawreSM".matches(".*SM$"));
	}

}
