package lab.object.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lab.object.tools.config.ApplicationConfiguration;
import lab.object.tools.define.ClassDefine;
import lab.object.tools.task.SendThread;

/**
 * Hello world!
 *
 */
public class Run implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final ObjectMapper om = new ObjectMapper();
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Map<String,String> params = new HashMap<>();
		for(int i=0;i<args.length;i+=2) {
			params.put(args[i], args[i+1]);
		}
		String classDefinePath = params.get("-path");
		if(classDefinePath==null || "".equals(classDefinePath)) {
			System.out.println("you must assign -path param,format -path <source dir>");
			return;
		}
		File sourceDir = new File(classDefinePath);
		String fileFilterRegx = params.get("-filter");
		File[] files;
		if(fileFilterRegx==null || "".equals(fileFilterRegx)) {
			files = sourceDir.listFiles();
		}else {
			files = sourceDir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().matches(fileFilterRegx);
				}
			});
		}
		ApplicationContext applicationContext = SpringApplication.run(ApplicationConfiguration.class, args);
		KafkaTemplate<String,String> template = applicationContext.getBean(KafkaTemplate.class);
		for(File file:files) {
			ClassDefine classDefine = om.readValue(file, ClassDefine.class);
			SendThread sendThread = new SendThread(classDefine, template);
			Thread thread = new Thread(sendThread);
			thread.start();
		}
//		EntityService<EntityBean, EntityRepository<EntityBean>> e = null;
//		e.batchRemove(new Serializable[] {"1","2","4"});
//		e.find
	}
}
