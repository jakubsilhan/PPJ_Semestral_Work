package semestralwork.ControllerTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
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
import semestralwork.Controllers.CountryController;
import semestralwork.Models.Country;
import semestralwork.Services.CountryService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CountryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    private Country sampleCountry;
    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
        sampleCountry = new Country(1, "Country 1");
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void testGetCountries_ReturnsList() throws Exception {
        when(countryService.getCountries()).thenReturn(List.of(sampleCountry));

        mockMvc.perform(get("/api/countries/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.is("Country 1")));
    }

    @Test
    public void testGetCountries_ReturnsNoContent() throws Exception {
        when(countryService.getCountries()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/countries/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCountryByName_Found() throws Exception {
        when(countryService.getCountryByName("Country 1")).thenReturn(sampleCountry);

        mockMvc.perform(get("/api/countries/byName/Country 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Country 1")));
    }

    @Test
    public void testGetCountryByName_NotFound() throws Exception {
        when(countryService.getCountryByName("Unknown")).thenReturn(null);

        mockMvc.perform(get("/api/countries/byName/Unknown"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCountryById_Found() throws Exception {
        when(countryService.getCountryById(1)).thenReturn(sampleCountry);

        mockMvc.perform(get("/api/countries/byId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Country 1")));
    }

    @Test
    public void testGetCountryById_NotFound() throws Exception {
        when(countryService.getCountryById(2)).thenReturn(null);

        mockMvc.perform(get("/api/countries/byId/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSaveCountry_Success() throws Exception {
        String json = objectMapper.writeValueAsString(sampleCountry);

        mockMvc.perform(post("/api/countries/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Country saved successfully."));

        verify(countryService).save(any(Country.class));
    }

    @Test
    public void testSaveCountry_Failure() throws Exception {
        doThrow(new RuntimeException("Database error")).when(countryService).save(any(Country.class));
        String json = objectMapper.writeValueAsString(sampleCountry);

        mockMvc.perform(post("/api/countries/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error saving country")));
    }

    @Test
    public void testDeleteCountry_Success() throws Exception {
        mockMvc.perform(delete("/api/countries/delete/Testland"))
                .andExpect(status().isOk())
                .andExpect(content().string("Country deleted successfully."));

        verify(countryService).delete("Testland");
    }

    @Test
    public void testDeleteCountry_Failure() throws Exception {
        doThrow(new RuntimeException("Failed to delete")).when(countryService).delete("FailCountry");

        mockMvc.perform(delete("/api/countries/delete/FailCountry"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error deleting country")));
    }
}
