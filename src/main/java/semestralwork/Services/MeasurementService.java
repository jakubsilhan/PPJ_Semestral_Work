package semestralwork.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.MeasurementRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WeatherService weatherService;

    private final Logger log = LoggerFactory.getLogger(MeasurementService.class);

    // Retrieve
    public List<Measurement> getMeasurements(){
        return StreamSupport.stream(measurementRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<Measurement> getMeasurementsByCityName(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return Collections.emptyList();
        }
        return measurementRepository.findByCityId(city.getId());
    }

    public Measurement getLatestMeasurement(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return null;
        }
        weatherService.updateMeasurements(city);
        Pageable pageable = PageRequest.of(0,1);
        return measurementRepository.findLatestMeasurement(city.getId(), pageable).get(0);
    }

    public MeasurementAggregate getDailyAverage(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return null;
        }
        weatherService.updateMeasurements(city);
        Pageable pageable = PageRequest.of(0,1);
        return  measurementRepository.findDailyAverage(city.getId(), pageable).get(0);
    }

    public MeasurementAggregate getWeeklyAverage(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return null;
        }
        weatherService.updateMeasurements(city);
        Pageable pageable = PageRequest.of(0,1);
        return  measurementRepository.findWeeklyAverage(city.getId(), pageable).get(0);
    }

    public MeasurementAggregate getBiWeeklyAverage(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return null;
        }
        weatherService.updateMeasurements(city);
        Pageable pageable = PageRequest.of(0,2);
        List<MeasurementAggregate> lastTwoWeeks = measurementRepository.findWeeklyAverage(city.getId(), pageable);

        if (lastTwoWeeks.size() < 2) {
            throw new RuntimeException("2 week data not available");
        }

        MeasurementAggregate week1 = lastTwoWeeks.get(0);
        MeasurementAggregate week2 = lastTwoWeeks.get(1);

        // Calculate two-week average
        return new MeasurementAggregate(
                (week1.getAvg_temp() + week2.getAvg_temp()) / 2,
                (week1.getAvg_pressure() + week2.getAvg_pressure()) / 2,
                (week1.getAvg_humidity() + week2.getAvg_humidity()) / 2,
                (week1.getAvg_temp_min() + week2.getAvg_temp_min()) / 2,
                (week1.getAvg_temp_max() + week2.getAvg_temp_max()) / 2,
                (week1.getAvg_wind_speed() + week2.getAvg_wind_speed()) / 2
        );
    }

    // Insert/Update
    public void create(Measurement measurement) {
        if (measurementRepository.existsById(measurement.getId())) {
            throw new IllegalArgumentException("Measurement with this ID already exists.");
        }
        try {
            measurementRepository.save(measurement);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Incorrect input data");
        }
    }

    public void update(Measurement measurement) {
        if (!measurementRepository.existsById(measurement.getId())) {
            throw new IllegalArgumentException("Measurement does not exist, cannot update.");
        }
        try {
            measurementRepository.save(measurement);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Incorrect input data");
        }
    }

    // Delete
    public void delete(Measurement measurement){
        measurementRepository.delete(measurement);
    }

    public void deleteByCity(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return;
        }
        measurementRepository.deleteByCityId(city.getId());
    }


}
