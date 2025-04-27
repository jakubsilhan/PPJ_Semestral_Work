package semestralwork.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import semestralwork.Controllers.MeasurementController;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;
import semestralwork.Services.MeasurementService;
import semestralwork.Services.WeatherService;
import semestralwork.DTOs.MeasurementAggregate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeasurementController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class MeasurementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeasurementService measurementService;

    @MockitoBean
    private WeatherService weatherService;

    private ObjectMapper objectMapper;
    private City sampleCity;
    private Measurement sampleMeasurement;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleCity = new City(1, "Sample City", 12.34, 45.67, null);

        sampleMeasurement = new Measurement(
                1,
                LocalDateTime.now(),
                25.5,
                1013,
                60,
                22.0,
                28.0,
                "Clear",
                "Clear sky",
                5.0,
                270,
                sampleCity
        );
    }

    @Test
    public void testGetAllMeasurements_Success() throws Exception {
        List<Measurement> measurements = Collections.singletonList(sampleMeasurement);
        when(measurementService.getMeasurements()).thenReturn(measurements);

        mockMvc.perform(get("/api/measurements/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city.name").value(sampleCity.getName()))
                .andExpect(jsonPath("$[0].temp").value(sampleMeasurement.getTemp()));

        verify(measurementService, times(1)).getMeasurements();
    }

    @Test
    public void testGetAllMeasurements_NoContent() throws Exception {
        when(measurementService.getMeasurements()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/measurements/all"))
                .andExpect(status().isNoContent());

        verify(measurementService, times(1)).getMeasurements();
    }

    @Test
    public void testGetMeasurementsByCity_Success() throws Exception {
        List<Measurement> measurements = Collections.singletonList(sampleMeasurement);
        when(measurementService.getMeasurementsByCityName(sampleCity.getName())).thenReturn(measurements);

        mockMvc.perform(get("/api/measurements/{cityName}", sampleCity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city.name").value(sampleCity.getName()))
                .andExpect(jsonPath("$[0].temp").value(sampleMeasurement.getTemp()));

        verify(measurementService, times(1)).getMeasurementsByCityName(sampleCity.getName());
    }

    @Test
    public void testGetMeasurementsByCity_NoContent() throws Exception {
        when(measurementService.getMeasurementsByCityName(sampleCity.getName())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/measurements/{cityName}", sampleCity.getName()))
                .andExpect(status().isNoContent());

        verify(measurementService, times(1)).getMeasurementsByCityName(sampleCity.getName());
    }

    @Test
    public void testGetLatestMeasurement_Success() throws Exception {
        when(measurementService.getLatestMeasurement(sampleCity.getName())).thenReturn(sampleMeasurement);

        mockMvc.perform(get("/api/measurements/latest/{cityName}", sampleCity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city.name").value(sampleCity.getName()))
                .andExpect(jsonPath("$.temp").value(sampleMeasurement.getTemp()));

        verify(measurementService, times(1)).getLatestMeasurement(sampleCity.getName());
    }

    @Test
    public void testGetLatestMeasurement_NoContent() throws Exception {
        when(measurementService.getLatestMeasurement(sampleCity.getName())).thenReturn(null);

        mockMvc.perform(get("/api/measurements/latest/{cityName}", sampleCity.getName()))
                .andExpect(status().isNoContent());

        verify(measurementService, times(1)).getLatestMeasurement(sampleCity.getName());
    }

    @Test
    public void testGetDailyAverage_Success() throws Exception {
        MeasurementAggregate aggregate = new MeasurementAggregate();
        aggregate.setAvg_temp(12.3);
        aggregate.setAvg_humidity(54);
        // Example values
        when(measurementService.getDailyAverage(sampleCity.getName())).thenReturn(aggregate);

        mockMvc.perform(get("/api/measurements/average/daily/{cityName}", sampleCity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avg_temp").value(aggregate.getAvg_temp()))
                .andExpect(jsonPath("$.avg_humidity").value(aggregate.getAvg_humidity()));

        verify(measurementService, times(1)).getDailyAverage(sampleCity.getName());
    }

    @Test
    public void testSaveMeasurement_Success() throws Exception {
        String json = objectMapper.writeValueAsString(sampleMeasurement);

        mockMvc.perform(post("/api/measurements/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Measurement saved successfully."));

        verify(measurementService, times(1)).save(any(Measurement.class));
    }

    @Test
    public void testSaveMeasurement_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Error saving measurement")).when(measurementService).save(any(Measurement.class));

        String json = objectMapper.writeValueAsString(sampleMeasurement);

        mockMvc.perform(post("/api/measurements/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error saving measurement: Error saving measurement"));

        verify(measurementService, times(1)).save(any(Measurement.class));
    }

    @Test
    public void testDeleteMeasurement_Success() throws Exception {
        mockMvc.perform(delete("/api/measurements/delete/{cityName}", sampleCity.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Country deleted successfully."));

        verify(measurementService, times(1)).deleteByCity(sampleCity.getName());
    }

    @Test
    public void testDeleteMeasurement_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Error deleting measurement")).when(measurementService).deleteByCity(sampleCity.getName());

        mockMvc.perform(delete("/api/measurements/delete/{cityName}", sampleCity.getName()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error deleting country: Error deleting measurement"));

        verify(measurementService, times(1)).deleteByCity(sampleCity.getName());
    }
}
