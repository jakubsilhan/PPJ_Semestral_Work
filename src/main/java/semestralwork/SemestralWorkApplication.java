package semestralwork;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import semestralwork.Configurations.AppConfiguration;
import semestralwork.Models.*;
import semestralwork.Services.WeatherService;


public class SemestralWorkApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AppConfiguration.class);
		ApplicationContext ctx = app.run(args);
		System.out.println("SemestralWork Application Started");

		MeasurementsDao measurementsDao = ctx.getBean(MeasurementsDao.class);
		CountriesDao countriesDao = ctx.getBean(CountriesDao.class);
		CitiesDao citiesDao = ctx.getBean(CitiesDao.class);
		WeatherService weatherService = ctx.getBean(WeatherService.class);

		// Countries
		countriesDao.getCountries().forEach(System.out::println);
		Country country = countriesDao.getCountry("Germany");
		System.out.println(country);


		// Cities
		citiesDao.getCities().forEach(System.out::println);
		citiesDao.getCitiesByCountryId(country.getId()).forEach(System.out::println);
		City city = citiesDao.getCity("Hamburg");
		System.out.println(city);


		//Measurements
		measurementsDao.getMeasurements().forEach(System.out::println);
		measurementsDao.getMeasurementsByCityId(city.getId()).forEach(System.out::println);
	}

}
