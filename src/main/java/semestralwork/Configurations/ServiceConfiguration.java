package semestralwork.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import semestralwork.Models.Measurement;
import semestralwork.Services.CityService;
import semestralwork.Services.CountryService;
import semestralwork.Services.MeasurementService;
import semestralwork.Services.WeatherService;

@Configuration
public class ServiceConfiguration {

    @Bean
    public MeasurementService measurementService() { return new MeasurementService(); }

    @Bean
    public CityService cityService() { return new CityService(); }

    @Bean
    public CountryService countryService() { return new CountryService(); }

    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }

}
