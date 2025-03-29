package semestralwork.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import semestralwork.Services.WeatherService;

@Configuration
public class ServiceConfiguration {

    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }

}
