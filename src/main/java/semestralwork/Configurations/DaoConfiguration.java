package semestralwork.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import semestralwork.Models.CountriesDao;

@Configuration
public class DaoConfiguration {
    @Bean
    public CountriesDao countriesDao() {
        return new CountriesDao();
    }
}
