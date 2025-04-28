package semestralwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

	private final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Main.class);
		ApplicationContext ctx = app.run(args);
		log.info("SemestralWork Application Started");
	}

}
