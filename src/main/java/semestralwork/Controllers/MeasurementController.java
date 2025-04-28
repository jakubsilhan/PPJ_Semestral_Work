package semestralwork.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.Measurement;
import semestralwork.Services.MeasurementService;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
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
        try {
            Measurement measurement = measurementService.getLatestMeasurement(cityName);
            return measurement == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(measurement);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/average/daily/{cityName}")
    public ResponseEntity<MeasurementAggregate> getDailyAverage(@PathVariable String cityName){
        try {
            MeasurementAggregate mAggregate = measurementService.getDailyAverage(cityName);
            return mAggregate == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mAggregate);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/average/weekly/{cityName}")
    public ResponseEntity<MeasurementAggregate> getWeeklyAverage(@PathVariable String cityName){
        try {
            MeasurementAggregate mAggregate = measurementService.getWeeklyAverage(cityName);
            return mAggregate == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mAggregate);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/average/biweekly/{cityName}")
    public ResponseEntity<MeasurementAggregate> getBiWeeklyAverage(@PathVariable String cityName){
        try {
            MeasurementAggregate mAggregate = measurementService.getBiWeeklyAverage(cityName);
            return mAggregate == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mAggregate);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Add or update a city
    @PutMapping("/create")
    public ResponseEntity<String> createMeasurement(@RequestBody Measurement measurement) {
        try {
            measurementService.create(measurement);
            return ResponseEntity.ok("Measurement created successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating measurement: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateMeasurement(@RequestBody Measurement measurement) {
        try {
            measurementService.update(measurement);
            return ResponseEntity.ok("Measurement updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating measurement: " + e.getMessage());
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
