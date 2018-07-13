package lab.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import lab.kafka.config.ReceiverConfig;

public class Receive {
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ReceiverConfig.class, args);
//		KafkaTemplate<String,String> template = applicationContext.getBean(KafkaTemplate.class);
	}
}
