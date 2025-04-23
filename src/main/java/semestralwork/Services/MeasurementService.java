package semestralwork.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
		Long start = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC);
        weatherService.updateMeasurements(city, start, end);
        Pageable pageable = PageRequest.of(0,1);
        return measurementRepository.findLatestMeasurement(city.getId(), pageable).get(0);
    }

    public MeasurementAggregate getDailyAverage(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return null;
        }
        Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long start = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC);
        weatherService.updateMeasurements(city, start, end);
        Pageable pageable = PageRequest.of(0,1);
        return  measurementRepository.findDailyAverage(city.getId(), pageable).get(0);
    }

    public MeasurementAggregate getWeeklyAverage(String cityName){
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city == null){
            return null;
        }
        Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long start = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC);
        weatherService.updateMeasurements(city, start, end);
        Pageable pageable = PageRequest.of(0,1);
        return  measurementRepository.findWeeklyAverage(city.getId(), pageable).get(0);
    }

    // Insert/Update
    public void save(Measurement measurement){
        measurementRepository.save(measurement);
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
