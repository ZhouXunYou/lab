package lab.kafka.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages= {"lab.kafka.rs"})
@Configuration
@ConfigurationProperties(prefix = "xxx", ignoreUnknownFields = false)
@PropertySource("classpath:application.properties")
public class ReceiverConfig {
public static final String GROUP_ID = UUID.randomUUID().toString().replace("-", "");
	private String kafkaAddress;
	public String getKafkaAddress() {
		return kafkaAddress;
	}

	public void setKafkaAddress(String kafkaAddress) {
		this.kafkaAddress = kafkaAddress;
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Serializable>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Serializable> factory = new ConcurrentKafkaListenerContainerFactory<String, Serializable>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(3000);
		return factory;
	}
	
	@Bean
	public ConsumerFactory<String, Serializable> consumerFactory() {
		return new DefaultKafkaConsumerFactory<String, Serializable>(consumerConfigs());
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
	}
}
