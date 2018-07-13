package lab.kafka.rs;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	@KafkaListener(topics = "test")
	public void processMessage(String content) {
		System.out.println(content);
	}
}
