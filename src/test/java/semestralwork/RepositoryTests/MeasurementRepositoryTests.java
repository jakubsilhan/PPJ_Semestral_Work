package semestralwork.RepositoryTests;

import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.City;
import semestralwork.Models.Country;
import semestralwork.Models.Measurement;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class MeasurementRepositoryTests {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private CityRepository cityRepository;

    private City testCity;

    @Before
    public void setup() {
        Country testCountry = new Country();
        testCountry.setId(1);
        testCity = new City();
        testCity.setName("TestCity");
        testCity.setCountry(testCountry);
        cityRepository.save(testCity);

        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 10; i++) {
            Measurement m = new Measurement();
            m.setCity(testCity);
            m.setDatetime(now.minusDays(i));
            m.setTemp(20 + i);
            m.setTemp_min(15 + i);
            m.setTemp_max(25 + i);
            m.setPressure(1010 + i);
            m.setHumidity(60 + i);
            m.setWind_speed(5 + i);
            measurementRepository.save(m);
        }
    }

    @Test
    public void testFindByCityId() {
        List<Measurement> measurements = measurementRepository.findByCityId(testCity.getId());
        assertEquals(10, measurements.size());
    }

    @Test
    public void testFindLatestMeasurement() {
        List<Measurement> latest = measurementRepository.findLatestMeasurement(testCity.getId(), PageRequest.of(0, 1));
        assertEquals(1, latest.size());
        assertEquals(LocalDateTime.now().withNano(0).getDayOfYear(), latest.get(0).getDatetime().withNano(0).getDayOfYear());
    }

    @Test
    public void testFindDailyAverage() {
        List<MeasurementAggregate> dailyAvg = measurementRepository.findDailyAverage(testCity.getId(), PageRequest.of(0, 5));
        assertFalse(dailyAvg.isEmpty());
        assertTrue(dailyAvg.size() <= 5);
    }

    @Test
    public void testFindWeeklyAverage() {
        List<MeasurementAggregate> weeklyAvg = measurementRepository.findWeeklyAverage(testCity.getId(), PageRequest.of(0, 5));
        assertFalse(weeklyAvg.isEmpty());
        assertTrue(weeklyAvg.size() <= 5);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteByCityId() {
        measurementRepository.deleteByCityId(testCity.getId());
        List<Measurement> afterDelete = measurementRepository.findByCityId(testCity.getId());
        assertTrue(afterDelete.isEmpty());
    }
}
