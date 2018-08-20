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

import lab.storm.model.Datapackage;
import lab.storm.model.KpiData;

public class InfluxBolt extends BaseBasicBolt {

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

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Datapackage datapackage = (Datapackage)input.getValueByField("datapackage");
		BatchPoints batchPoints = BatchPoints.database("mmc").build();
		String moId = datapackage.getMoId();
		Long collectTime = datapackage.getCollectTime();
		long createTime = System.currentTimeMillis();
		Map<String,List<KpiData>> kpiDatas = datapackage.getKpiDatas();
		kpiDatas.forEach((kpiCode,KpiData)->{
			KpiData.forEach(data->{
				String instance = data.getObjectName()==null||"".equals(data.getObjectName())?"NO_OBJECT":data.getObjectName();
				Object value = data.getData();
				
//				Builder builder = Point.measurement(moId.concat("_PM_STORM"))
//						.tag("kpi_code", kpiCode)
//						.tag("object_id", instance)
//						.addField("collect_time",collectTime)
//						.addField("createTime", createTime)
//						.addField("moId", moId)
//						.addField("value", Double.parseDouble(value.toString()));
				batchPoints.point(buildPoint(kpiCode,moId,value.toString(),instance,collectTime,createTime));
			});
		});
//		batchPoints.
		influxDB.write(batchPoints);
	}
	private Point buildPoint(String kpiCode,String moId,String value,String instance,long collectTime,long createTime) {
		String measurement = buildMeasurement(kpiCode,moId);
		Builder builder = Point.measurement(measurement);
		builder
			.tag("kpi_code",kpiCode)
			.tag("object_id",instance)
			.addField("collect_time", collectTime)
			.addField("createTime", createTime)
			.addField("moId", moId);
		if(measurement.matches(".*PM$")) {
			builder.addField("value", Double.parseDouble(value));
		}else {
			builder.addField("value", value);
		}
		if(measurement.matches(".*SM$")) {
			builder.tag("status",value);
		}
		return builder.build();
	}
	private String buildMeasurement(String kpiCode,String moId) {
		String suffix = "";
		if(kpiCode.matches(".*1$")) {
			suffix = "_PM";
		}else if(kpiCode.matches(".*2$")) {
			suffix = "_CM";
		}else if(kpiCode.matches(".*3$")) {
			suffix = "_SM";
		}else if(kpiCode.matches(".*4$")) {
			suffix = "_LM";
		}
		suffix.concat("_STORM");
		return moId.concat(suffix);
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		System.out.println("asdasdfawre_SM".matches(".*SM$"));
	}

}
