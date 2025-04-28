package semestralwork.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semestralwork.Models.Country;
import semestralwork.Repositories.CountryRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CountryService {

    @Autowired
    CountryRepository countryRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Retrieve
    public List<Country> getCountries() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Country getCountryByName(String name) {
        return countryRepository.findByName(name).orElse(null);
    }

    public Country getCountryById(int country_id) {
        return countryRepository.findById(country_id).orElse(null);
    }

    // Insert/Update
    public void create(Country country) {
        if (countryRepository.existsById(country.getId())) {
            throw new IllegalArgumentException("Country with this ID already exists.");
        }
        try {
            countryRepository.save(country);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Incorrect input data");
        }
    }

    public void update(Country country) {
        if (!countryRepository.existsById(country.getId())) {
            throw new IllegalArgumentException("Country does not exist, cannot update.");
        }
        try {
            countryRepository.save(country);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Incorrect input data");
        }
    }


    // Delete
    public void delete(String countryName) {
        countryRepository.findByName(countryName).ifPresent(country -> countryRepository.delete(country));
    }
}
