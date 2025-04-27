package semestralwork.RepositoryTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import semestralwork.Models.City;
import semestralwork.Models.Country;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.CountryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CityRepositoryTests {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    private Country czechia;

    @Before
    public void setUp(){
        Country country = new Country();
        country.setName("Czechia");
        countryRepository.save(country);
        czechia = countryRepository.findByName("Czechia").get();
        City city = new City();
        city.setName("Jablonec");
        city.setCountry(czechia);
        city.setLatitude(12.123);
        city.setLongitude(34.456);
        cityRepository.save(city);
    }

    @Test
    public void testFindByName_CityExists() {
        Optional<City> foundCity = cityRepository.findByName("Jablonec");

        assertTrue(foundCity.isPresent());
        assertEquals("Jablonec", foundCity.get().getName());
    }

    @Test
    public void testFindByName_CityNotFound() {
        Optional<City> foundCity = cityRepository.findByName("Litvinov");

        assertFalse(foundCity.isPresent());
    }

    @Test
    public void testFindByCountryId_CountryExists(){
        List<City> foundCity = cityRepository.findByCountryId(czechia.getId());

        assertEquals(1, foundCity.size());
        assertEquals("Jablonec", foundCity.get(0).getName());
    }

    @Test
    public void testFindByCountryId_CountryNotFound() {
        List<City> foundCity = cityRepository.findByCountryId(32);
        assertTrue(foundCity.isEmpty());
    }
}
