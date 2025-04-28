package semestralwork.ServiceTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.MeasurementRepository;
import semestralwork.Services.MeasurementService;
import semestralwork.Services.WeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class MeasurementServiceTests {
    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private MeasurementService measurementService;

    private List<Measurement> mockMeasurements;
    private City testCity;

    @Before
    public void setUp() {
        mockMeasurements = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Measurement m = new Measurement();
            m.setId(i);
            mockMeasurements.add(m);
        }

        testCity = new City(1, "City 1", 12.34, 56.78);
    }

    @Test
    public void testGetMeasurements() {
        when(measurementRepository.findAll()).thenReturn(mockMeasurements);

        List<Measurement> measurements = measurementService.getMeasurements();

        assertFalse(measurements.isEmpty());
        assertEquals(5, measurements.size());
    }

    @Test
    public void testGetMeasurementsByCityName_CityExists() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(testCity));
        when(measurementRepository.findByCityId(1)).thenReturn(mockMeasurements);

        List<Measurement> result = measurementService.getMeasurementsByCityName("City 1");

        assertFalse(result.isEmpty());
        assertEquals(5, result.size());
    }

    @Test
    public void testGetMeasurementsByCityName_CityNotFound() {
        when(cityRepository.findByName("Unknown")).thenReturn(Optional.empty());

        List<Measurement> result = measurementService.getMeasurementsByCityName("Unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLatestMeasurement_CityExists() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(testCity));
        when(measurementRepository.findLatestMeasurement(eq(1), any())).thenReturn(List.of(mockMeasurements.get(0)));

        Measurement result = measurementService.getLatestMeasurement("City 1");

        assertNotNull(result);
        assertEquals(mockMeasurements.get(0), result);
        verify(weatherService).updateMeasurements(eq(testCity));
    }

    @Test
    public void testGetLatestMeasurement_CityNotFound() {
        when(cityRepository.findByName("Unknown")).thenReturn(Optional.empty());

        Measurement result = measurementService.getLatestMeasurement("Unknown");

        assertNull(result);
    }

    @Test
    public void testGetDailyAverage_CityExists() {
        MeasurementAggregate mockAggregate = new MeasurementAggregate();
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(testCity));
        when(measurementRepository.findDailyAverage(eq(1), any())).thenReturn(List.of(mockAggregate));

        MeasurementAggregate result = measurementService.getDailyAverage("City 1");

        assertNotNull(result);
        assertEquals(mockAggregate, result);
        verify(weatherService).updateMeasurements(eq(testCity));
    }

    @Test
    public void testGetDailyAverage_CityNotFound() {
        when(cityRepository.findByName("Unknown")).thenReturn(Optional.empty());

        MeasurementAggregate result = measurementService.getDailyAverage("Unknown");

        assertNull(result);
    }

    @Test
    public void testGetWeeklyAverage_CityExists() {
        MeasurementAggregate mockAggregate = new MeasurementAggregate();
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(testCity));
        when(measurementRepository.findWeeklyAverage(eq(1), any())).thenReturn(List.of(mockAggregate));

        MeasurementAggregate result = measurementService.getWeeklyAverage("City 1");

        assertNotNull(result);
        assertEquals(mockAggregate, result);
        verify(weatherService).updateMeasurements(eq(testCity));
    }

    @Test
    public void testGetWeeklyAverage_CityNotFound() {
        when(cityRepository.findByName("Unknown")).thenReturn(Optional.empty());

        MeasurementAggregate result = measurementService.getWeeklyAverage("Unknown");

        assertNull(result);
    }

    @Test
    public void testCreate_Successful() {
        Measurement newMeasurement = new Measurement();
        when(measurementRepository.existsById(newMeasurement.getId())).thenReturn(false);

        measurementService.create(newMeasurement);

        verify(measurementRepository).save(newMeasurement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_MeasurementAlreadyExists() {
        Measurement newMeasurement = new Measurement();
        when(measurementRepository.existsById(newMeasurement.getId())).thenReturn(true);

        measurementService.create(newMeasurement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_SaveThrowsException() {
        Measurement newMeasurement = new Measurement();
        when(measurementRepository.existsById(newMeasurement.getId())).thenReturn(false);
        when(measurementRepository.save(newMeasurement)).thenThrow(new RuntimeException("DB error"));

        measurementService.create(newMeasurement);
    }

    @Test
    public void testUpdate_Successful() {
        Measurement existingMeasurement = new Measurement();
        when(measurementRepository.existsById(existingMeasurement.getId())).thenReturn(true);

        measurementService.update(existingMeasurement);

        verify(measurementRepository).save(existingMeasurement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_MeasurementDoesNotExist() {
        Measurement nonExistingMeasurement = new Measurement();
        when(measurementRepository.existsById(nonExistingMeasurement.getId())).thenReturn(false);

        measurementService.update(nonExistingMeasurement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_SaveThrowsException() {
        Measurement existingMeasurement = new Measurement();
        when(measurementRepository.existsById(existingMeasurement.getId())).thenReturn(true);
        when(measurementRepository.save(existingMeasurement)).thenThrow(new RuntimeException("DB error"));

        measurementService.update(existingMeasurement);
    }

    @Test
    public void testDelete() {
        Measurement measurement = new Measurement();
        measurementService.delete(measurement);

        verify(measurementRepository).delete(measurement);
    }

    @Test
    public void testDeleteByCity_CityExists() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(testCity));

        measurementService.deleteByCity("City 1");

        verify(measurementRepository).deleteByCityId(1);
    }

    @Test
    public void testDeleteByCity_CityNotFound() {
        when(cityRepository.findByName("Unknown")).thenReturn(Optional.empty());

        measurementService.deleteByCity("Unknown");

        verify(measurementRepository, never()).deleteByCityId(anyInt());
    }
}
