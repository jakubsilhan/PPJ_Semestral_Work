package semestralwork;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import semestralwork.Configurations.AppConfiguration;
import semestralwork.Models.*;
import semestralwork.Services.WeatherService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class SemestralWorkApplication {
	// TODO rework DB (allow multiple weathers)
	// TODO load city data using coordinates and save json to DB
	// TODO Replace jdbc with Spring.Data
	// TODO Add endpoints
	// TODO Create tests

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AppConfiguration.class);
		ApplicationContext ctx = app.run(args);
		System.out.println("SemestralWork Application Started");

		MeasurementsDao measurementsDao = ctx.getBean(MeasurementsDao.class);
		CountriesDao countriesDao = ctx.getBean(CountriesDao.class);
		CitiesDao citiesDao = ctx.getBean(CitiesDao.class);
		WeatherService weatherService = ctx.getBean(WeatherService.class);

		// Countries
		System.out.println("Countries:");
		countriesDao.getCountries().forEach(System.out::println);
		Country country = countriesDao.getCountry("Germany");
		System.out.println(country + "\n");


		// Cities
		System.out.println("Cities: ");
		citiesDao.getCities().forEach(System.out::println);
		citiesDao.getCitiesByCountryId(country.getId()).forEach(System.out::println);
		City city = citiesDao.getCity("Prague");
		System.out.println(city + "\n");
		Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
		Long start = LocalDateTime.now().minusDays(14).toEpochSecond(ZoneOffset.UTC);

		weatherService.updateMeasurements(city, start, end);

		//Measurements
		System.out.println("Weather:");
		measurementsDao.getMeasurements().forEach(System.out::println);
		measurementsDao.getMeasurementsByCityId(city.getId()).forEach(System.out::println);
	}

}
