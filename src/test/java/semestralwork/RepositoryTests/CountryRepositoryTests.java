package semestralwork.RepositoryTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import semestralwork.Models.Country;
import semestralwork.Repositories.CountryRepository;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CountryRepositoryTests {

    @Autowired
    private CountryRepository countryRepository;

    @Before
    public void setUp() {
        Country country = new Country();
        country.setName("USA");
        countryRepository.save(country);
    }

    @Test
    public void testFindByName_CountryExists() {
        Optional<Country> foundCountry = countryRepository.findByName("USA");

        assertTrue(foundCountry.isPresent());
        assertEquals("USA", foundCountry.get().getName());
    }

    @Test
    public void testFindByName_CountryNotFound() {
        Optional<Country> foundCountry = countryRepository.findByName("Canada");

        assertFalse(foundCountry.isPresent());
    }
}
