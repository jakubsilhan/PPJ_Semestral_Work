package semestralwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import semestralwork.Configurations.AppConfiguration;
import semestralwork.Models.CountriesDao;
import semestralwork.Models.Country;

import java.util.List;

public class SemestralWorkApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AppConfiguration.class);
		ApplicationContext ctx = app.run(args);
		System.out.println("SemestralWork Application Started");

		CountriesDao countriesDao = ctx.getBean(CountriesDao.class);

		List<Country> countries = countriesDao.getCountries();
		System.out.println(countries);
	}

}
