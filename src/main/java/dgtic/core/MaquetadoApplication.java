package dgtic.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class MaquetadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaquetadoApplication.class, args);
	}

}
