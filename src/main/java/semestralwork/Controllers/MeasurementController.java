package semestralwork.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;
import semestralwork.Services.MeasurementService;
import semestralwork.Services.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final WeatherService weatherService;

    public MeasurementController(MeasurementService measurementService, WeatherService weatherService) {
        this.measurementService = measurementService;
        this.weatherService = weatherService;
    }

    // Retrieve all cities
    @GetMapping("/all")
    public ResponseEntity<List<Measurement>> getAllMeasurements() {
        List<Measurement> measurements = measurementService.getMeasurements();
        return measurements.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(measurements);
    }

    @GetMapping("/{cityName}")
    public ResponseEntity<List<Measurement>> getMeasurementsByCity(@PathVariable String cityName) {
        List<Measurement> measurements = measurementService.getMeasurementsByCityName(cityName);
        return measurements.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(measurements);
    }

    // Retrieve a city
    @GetMapping("/latest/{cityName}")
    public ResponseEntity<Measurement> getLatestMeasurement(@PathVariable String cityName){
        Measurement measurement = measurementService.getLatestMeasurement(cityName);
        return measurement == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(measurement);
    }

    @GetMapping("/average/daily/{cityName}")
    public ResponseEntity<MeasurementAggregate> getDailyAverage(@PathVariable String cityName){
        MeasurementAggregate mAggregate = measurementService.getDailyAverage(cityName);
        return mAggregate == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mAggregate);
    }

    @GetMapping("/average/weekly/{cityName}")
    public ResponseEntity<MeasurementAggregate> getWeeklyAverage(@PathVariable String cityName){
        MeasurementAggregate mAggregate = measurementService.getWeeklyAverage(cityName);
        return mAggregate == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mAggregate);
    }

    // Add or update a city
    @PostMapping("/save")
    public ResponseEntity<String> saveMeasurement(@RequestBody Measurement measurement) {
        try {
            measurementService.save(measurement);
            return ResponseEntity.ok("Measurement saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving measurement: " + e.getMessage());
        }
    }

    // Delete a country
    @DeleteMapping("/delete/{cityName}")
    public ResponseEntity<String> deleteByCity(@PathVariable String cityName) {
        try {
            measurementService.deleteByCity(cityName);
            return ResponseEntity.ok("Country deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting country: " + e.getMessage());
        }
    }
}
