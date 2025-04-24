package semestralwork.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import semestralwork.Controllers.CityController;
import semestralwork.Models.City;
import semestralwork.Models.Country;
import semestralwork.Services.CityService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    private ObjectMapper objectMapper;

    private Country sampleCountry;
    private City sampleCity;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        sampleCountry = new Country(1, "Country 1");
        sampleCity = new City(1, "City 1", 12.34, 45.67, sampleCountry);
    }

    @Test
    public void testGetAllCities_Success() throws Exception {
        List<City> cities = Collections.singletonList(sampleCity);
        when(cityService.getCities()).thenReturn(cities);

        // Perform GET request and check the result
        mockMvc.perform(get("/api/cities/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(sampleCity.getName()))
                .andExpect(jsonPath("$[0].country.id").value(sampleCountry.getId()));

        verify(cityService, times(1)).getCities(); // Ensure the service method is called once
    }

    @Test
    public void testGetAllCities_NoContent() throws Exception {
        // Prepare mock behavior for empty list
        when(cityService.getCities()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cities/all"))
                .andExpect(status().isNoContent());

        verify(cityService, times(1)).getCities(); // Ensure the service method is called once
    }

    @Test
    public void testGetCitiesByCountry_Success() throws Exception {
        List<City> cities = Collections.singletonList(sampleCity);
        when(cityService.getCitiesByCountry(sampleCountry.getName())).thenReturn(cities);

        mockMvc.perform(get("/api/cities/byCountry/{countryName}", sampleCountry.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(sampleCity.getName()))
                .andExpect(jsonPath("$[0].country.id").value(sampleCountry.getId()));

        verify(cityService, times(1)).getCitiesByCountry(any(String.class));
    }

    @Test
    public void testGetCitiesByCountry_NoContent() throws Exception {
        when(cityService.getCitiesByCountry(sampleCountry.getName())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cities/byCountry/{countryName}", sampleCountry.getName()))
                .andExpect(status().isNoContent());

        verify(cityService, times(1)).getCitiesByCountry(sampleCountry.getName());
    }

    @Test
    public void testGetCityByName_Success() throws Exception {
        when(cityService.getCityByName(sampleCity.getName())).thenReturn(sampleCity);

        mockMvc.perform(get("/api/cities/byName/{cityName}", sampleCity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sampleCity.getName()))
                .andExpect(jsonPath("$.country.id").value(sampleCountry.getId()));

        verify(cityService, times(1)).getCityByName(sampleCity.getName());
    }

    @Test
    public void testGetCityByName_NoContent() throws Exception {
        when(cityService.getCityByName("Nonexistent City")).thenReturn(null);

        mockMvc.perform(get("/api/cities/byName/{cityName}", "Nonexistent City"))
                .andExpect(status().isNoContent());

        verify(cityService, times(1)).getCityByName("Nonexistent City");
    }

    @Test
    public void testSaveCity_Success() throws Exception {
        String json = objectMapper.writeValueAsString(sampleCity);

        mockMvc.perform(post("/api/cities/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("City saved successfully."));

        verify(cityService, times(1)).save(any(City.class));
    }

    @Test
    public void testSaveCity_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Error saving city")).when(cityService).save(any(City.class));

        String json = objectMapper.writeValueAsString(sampleCity);

        mockMvc.perform(post("/api/cities/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error saving city: Error saving city"));

        verify(cityService, times(1)).save(any(City.class));
    }

    @Test
    public void testDeleteCity_Success() throws Exception {
        mockMvc.perform(delete("/api/cities/delete/{cityName}", "Sample City"))
                .andExpect(status().isOk())
                .andExpect(content().string("City deleted successfully."));

        verify(cityService, times(1)).deleteByName("Sample City");
    }

    @Test
    public void testDeleteCity_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Error deleting city")).when(cityService).deleteByName("Sample City");

        mockMvc.perform(delete("/api/cities/delete/{cityName}", "Sample City"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error deleting city: Error deleting city"));

        verify(cityService, times(1)).deleteByName("Sample City");
    }
}