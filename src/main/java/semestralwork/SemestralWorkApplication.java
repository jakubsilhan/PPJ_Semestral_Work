package semestralwork;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import semestralwork.Configurations.AppConfiguration;
import semestralwork.Models.City;
import semestralwork.Models.MeasurementsDao;
import semestralwork.Services.WeatherService;


public class SemestralWorkApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AppConfiguration.class);
		ApplicationContext ctx = app.run(args);
		System.out.println("SemestralWork Application Started");

		MeasurementsDao measurementsDao = ctx.getBean(MeasurementsDao.class);
		WeatherService weatherService = ctx.getBean(WeatherService.class);

		City city = new City();
		city.setId(3);
		city.setName("Hamburg");
		weatherService.updateMeasurements(city);
	}

}
