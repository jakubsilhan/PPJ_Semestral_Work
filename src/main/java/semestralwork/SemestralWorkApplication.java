package semestralwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import semestralwork.Configurations.AppConfiguration;

public class SemestralWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppConfiguration.class, args);
		System.out.println("SemestralWork Application Started");
	}

}
