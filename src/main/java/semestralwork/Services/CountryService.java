package semestralwork.Services;

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
    public void save(Country country) {
        countryRepository.save(country);
    }

    // Delete
    public void delete(String countryName) {
        countryRepository.findByName(countryName).ifPresent(country -> countryRepository.delete(country));
    }
}
