package lab.storm;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan({"lab.storm.loader"})
@PropertySource(value = { "classpath:application.properties"})
public class StormKafkaLauncher {
	public static void main(String[] args) {
//		GsonBuilder.class;
		SpringApplicationBuilder application = new SpringApplicationBuilder(StormKafkaLauncher.class);
		application.run(args);
	}
}
