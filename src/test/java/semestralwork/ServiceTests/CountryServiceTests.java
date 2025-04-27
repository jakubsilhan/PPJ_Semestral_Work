package semestralwork.ServiceTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import semestralwork.Models.Country;
import semestralwork.Repositories.CountryRepository;
import semestralwork.Services.CountryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CountryServiceTests {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private List<Country> mockCountries;

    @Before
    public void setUp() {
        mockCountries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Country country = new Country();
            country.setName("Country " + i);
            country.setId(i);
            mockCountries.add(country);
        }
    }

    @Test
    public void testGetCountries(){
        when(countryRepository.findAll()).thenReturn(mockCountries);

        List<Country> countries = countryService.getCountries();

        assertFalse(countries.isEmpty());
        assertEquals(5, countries.size());
    }

    @Test
    public void testGetCountryByName_CountryExists(){

        Country mockCountry = new Country(1, "Country 1");
        when(countryRepository.findByName("Country1")).thenReturn(Optional.of(mockCountry));
        Country country = countryService.getCountryByName("Country1");

        assertEquals(mockCountry, country);
    }

    @Test
    public void testGetCountryByName_CountryNotFound(){
        when(countryRepository.findByName("Country 1")).thenReturn(Optional.empty());
        Country country = countryService.getCountryByName("Country 1");

        assertNull(country);
    }

    @Test
    public void testGetCountryById_CountryExists() {
        Country mockCountry = new Country(1, "Country 1");
        when(countryRepository.findById(1)).thenReturn(Optional.of(mockCountry));
        Country country = countryService.getCountryById(1);

        assertEquals(mockCountry, country);
    }

    @Test
    public void testGetCountryById_CountryNotFound() {
        when(countryRepository.findById(1)).thenReturn(Optional.empty());
        Country country = countryService.getCountryById(1);

        assertNull(country);
    }

    @Test
    public void testSave() {
        Country newCountry = new Country(1, "Country 1");
        countryService.save(newCountry);

        verify(countryRepository).save(newCountry);
    }

    @Test
    public void testDeleteCountry_CountryExists() {
        Country country = new Country(1, "Country 1");
        when(countryRepository.findByName("Country 1")).thenReturn(Optional.of(country));
        countryService.delete("Country 1");

        verify(countryRepository).delete(country);
    }

    @Test
    public void testDeleteCountry_CountryNotFound() {
        when(countryRepository.findByName("Country 1")).thenReturn(Optional.empty());
        countryService.delete("Country 1");

        verify(countryRepository, Mockito.never()).delete(Mockito.any(Country.class));
    }


}
