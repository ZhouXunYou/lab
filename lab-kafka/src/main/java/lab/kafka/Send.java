package lab.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import lab.kafka.config.SendConfig;

public class Send {
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SendConfig.class, args);
		KafkaTemplate<String,String> template = applicationContext.getBean(KafkaTemplate.class);
		for(int i=0;i<100;i++) {
			template.send("test", "message "+ i);
		}
		
	}
}
