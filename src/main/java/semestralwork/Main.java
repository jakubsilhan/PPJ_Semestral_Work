package semestralwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("semestralwork.Repositories")
public class Main {
	// TODO Create tests

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Main.class);
		ApplicationContext ctx = app.run(args);
		System.out.println("SemestralWork Application Started");
	}

}
