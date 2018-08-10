package lab.storm.bolt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import lab.storm.kqi.ExecutorApi;
import lab.storm.kqi.KQI;
import lab.storm.kqi.KpiExecutor;
import lab.storm.model.Datapackage;

public class KpiCalculateBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
	private static final ClassPool classPool = ClassPool.getDefault();
	private Map<String,Map<Integer,KpiExecutor>> dynamicKpiExecutors = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Datapackage datapackage = (Datapackage)input.getValueByField("datapackage");
		List<KQI> kqis = (List<KQI>)input.getValueByField("kqis");
		try {
			for(KQI kqi:kqis) {
				KpiExecutor kpiExecutor = buildExcutor(kqi);
				kpiExecutor.execute(new ExecutorApi(datapackage));
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NotFoundException | CannotCompileException e) {
			e.printStackTrace();
		}
//		System.out.println("KpiCalculateBolt process finished");
		collector.emit(new Values(datapackage));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("datapackage"));
	}
	
	private KpiExecutor buildExcutor(KQI kqi) throws  CannotCompileException, ClassNotFoundException, InstantiationException, IllegalAccessException, NotFoundException {
//		String className = KpiExecutor.class.getName()+"_"+kqi.getKqiCode();
		
//		loadClass(className);
		
		KpiExecutor kpiExecutor = null;
		//通过kqiCode获取本地缓存，缓存信息：key=版本号,value=KpiExecutor Object
		Map<Integer,KpiExecutor> dynamicKpiExecutor = dynamicKpiExecutors.get(kqi.getKqiCode());
		//如不为空，则认定为已经实例化
		if(dynamicKpiExecutor!=null) {
			//通过从缓存中获取到的kqi对象的版本号获取KpiExecutor Object
			kpiExecutor = dynamicKpiExecutor.get(kqi.getVersion());
			//如为空，说明版本号发生变化,重新生对象。否则直接返回本地缓存中的KpiExecutor Object实例
			if(kpiExecutor==null) {
				//变更execute方法定义
				Class<?> clazz = loadClass(kqi);
				//将本地缓存重置
				dynamicKpiExecutor.clear();
				
				kpiExecutor = (KpiExecutor)clazz.newInstance();
				dynamicKpiExecutor.put(kqi.getVersion(), kpiExecutor);
			}
		}else {//通过kqiCode未获取到本地缓存，认定为未加载过该类
			//加载类
			Class<?> clazz = loadClass(kqi);
			//创建KpiExecutor对象
			kpiExecutor = (KpiExecutor)clazz.newInstance();
			//将对象put至本地缓存，以便下次直接使用
			Map<Integer,KpiExecutor> kpiExecutorVersion = new HashMap<>();
			kpiExecutorVersion.put(kqi.getVersion(), kpiExecutor);
			dynamicKpiExecutors.put(kqi.getKqiCode(), kpiExecutorVersion);
		}
		return kpiExecutor;
	}
	
	
	private Class<?> loadClass(KQI kqi) throws NotFoundException, CannotCompileException {
		String name = String.format("%s.KQI_%S_%d", KpiCalculateBolt.class.getPackage().getName(),kqi.getKqiCode(),kqi.getVersion());
		CtClass ctClass = classPool.makeClass(name);
		ctClass.setInterfaces(new CtClass[] {
			classPool.getCtClass(KpiExecutor.class.getName()),
			classPool.getCtClass(Serializable.class.getName())
		});
		CtField serialVersionUID = CtField.make("private static final long serialVersionUID = 1L;", ctClass);
		ctClass.addField(serialVersionUID);
		addExecuteMethod(ctClass,kqi.getScript());
		Class<?> clazz = ctClass.toClass();
		ctClass.detach();
		System.out.println("class "+clazz.getName()+" load success");
		return clazz;
	}

	private void addExecuteMethod(CtClass ctClass,String script) throws CannotCompileException {
		StringBuffer methodBody = new StringBuffer("public void execute(lab.storm.kqi.ExecutorApi executorOptions){");
		methodBody.append(script);
		methodBody.append("}");
		ctClass.addMethod(CtMethod.make(methodBody.toString(), ctClass));
	}

}
