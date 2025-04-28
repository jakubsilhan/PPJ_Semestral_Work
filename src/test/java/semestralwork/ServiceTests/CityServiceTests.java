package semestralwork.ServiceTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import semestralwork.Models.City;
import semestralwork.Models.Country;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.CountryRepository;
import semestralwork.Services.CityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CityServiceTests {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    private List<City> mockCities;

    @Before
    public void setUp() {
        mockCities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            City city = new City();
            city.setName("City " + i);
            city.setId(i);
            mockCities.add(city);
        }
    }

    @Test
    public void testGetCities() {
        when(cityRepository.findAll()).thenReturn(mockCities);
        List<City> cities = cityService.getCities();

        assertFalse(cities.isEmpty());
        assertEquals(5, cities.size());
    }

    @Test
    public void testGetCitiesByCountry_CountryExists() {
        Country country = new Country(1, "Country 1");
        when(countryRepository.findByName("Country 1")).thenReturn(Optional.of(country));
        when(cityRepository.findByCountryId(1)).thenReturn(mockCities);
        List<City> cities = cityService.getCitiesByCountry("Country 1");

        assertFalse(cities.isEmpty());
        assertEquals(5, cities.size());
    }

    @Test
    public void testGetCitiesByCountry_CountryNotFound() {
        when(countryRepository.findByName("Country 1")).thenReturn(Optional.empty());
        List<City> cities = cityService.getCitiesByCountry("Country 1");

        assertTrue(cities.isEmpty());
    }

    @Test
    public void testGetCityByName_CityExists() {
        City city = new City(1, "City 1", 12.34,56.78);
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));

        City result = cityService.getCityByName("City 1");

        assertEquals(city, result);
    }

    @Test
    public void testGetCityByName_CityNotFound() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.empty());

        City result = cityService.getCityByName("City 1");

        assertNull(result);
    }

    @Test
    public void testGetCityById_CityExists() {
        City city = new City(1, "City 1", 12.34,56.78);
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        City result = cityService.getCityById(1);

        assertEquals(city, result);
    }

    @Test
    public void testGetCityById_CityNotFound() {
        when(cityRepository.findById(1)).thenReturn(Optional.empty());
        City result = cityService.getCityById(1);

        assertNull(result);
    }

    @Test
    public void testCreate_Successful() {
        City city = new City(1, "City 1", 12.34, 56.78);
        when(cityRepository.existsById(city.getId())).thenReturn(false);

        cityService.create(city);

        verify(cityRepository).save(city);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_CityAlreadyExists() {
        City city = new City(1, "City 1", 12.34, 56.78);
        when(cityRepository.existsById(city.getId())).thenReturn(true);

        cityService.create(city);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_SaveThrowsException() {
        City city = new City(1, "City 1", 12.34, 56.78);
        when(cityRepository.existsById(city.getId())).thenReturn(false);
        when(cityRepository.save(city)).thenThrow(new RuntimeException("DB error"));

        cityService.create(city);
    }

    @Test
    public void testUpdate_Successful() {
        City city = new City(1, "City 1", 12.34, 56.78);
        when(cityRepository.existsById(city.getId())).thenReturn(true);

        cityService.update(city);

        verify(cityRepository).save(city);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_CityDoesNotExist() {
        City city = new City(1, "City 1", 12.34, 56.78);
        when(cityRepository.existsById(city.getId())).thenReturn(false);

        cityService.update(city);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_SaveThrowsException() {
        City city = new City(1, "City 1", 12.34, 56.78);
        when(cityRepository.existsById(city.getId())).thenReturn(true);
        when(cityRepository.save(city)).thenThrow(new RuntimeException("DB error"));

        cityService.update(city);
    }


    @Test
    public void testDelete() {
        City city = new City(1, "City 1", 12.34,56.78);
        cityService.delete(city);

        verify(cityRepository).delete(city);
    }

    @Test
    public void testDeleteByName_CityExists() {
        City city = new City(1, "City 1", 12.34,56.78);
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        cityService.deleteByName("City 1");

        verify(cityRepository).delete(city);
    }

    @Test
    public void testDeleteByName_CityNotFound() {
        when(cityRepository.findByName("Unknown City")).thenReturn(Optional.empty());
        cityService.deleteByName("Unknown City");

        verify(cityRepository, Mockito.never()).delete(Mockito.any(City.class));
    }
}
