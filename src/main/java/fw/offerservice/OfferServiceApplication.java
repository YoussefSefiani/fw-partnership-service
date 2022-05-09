package fw.offerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
@EnableEurekaClient
public class OfferServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfferServiceApplication.class, args);
	}

}
