package semestralwork.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semestralwork.Models.City;
import semestralwork.Models.Country;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.CountryRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CityService {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CountryRepository countryRepository;

    private final Logger log = LoggerFactory.getLogger(CityService.class);

    // Retrieve
    public List<City> getCities() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<City> getCitiesByCountry(String countryName) {
        Country country = countryRepository.findByName(countryName).orElse(null);
        if (country == null) {
            return Collections.emptyList();
        }
        return cityRepository.findByCountryId(country.getId());
    }

    public City getCityByName(String name){
        return cityRepository.findByName(name).orElse(null);
    }

    public City getCityById(int id) {
        return cityRepository.findById(id).orElse(null);
    }

    // Insert/Update
    public void create(City city) {
        if (cityRepository.existsById(city.getId())) {
            throw new IllegalArgumentException("City with this ID already exists.");
        }
        try {
            cityRepository.save(city);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Incorrect input data");
        }
    }

    public void update(City city) {
        if (!cityRepository.existsById(city.getId())) {
            throw new IllegalArgumentException("City does not exist, cannot update.");
        }
        try {
            cityRepository.save(city);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Incorrect input data");
        }
    }

    // Delete
    public void delete(City city) {
        cityRepository.delete(city);
    }

    public void deleteByName(String cityName) {
        cityRepository.findByName(cityName).ifPresent(city -> cityRepository.delete(city));
    }

}
