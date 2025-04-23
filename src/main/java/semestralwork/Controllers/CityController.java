package semestralwork.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semestralwork.Models.City;
import semestralwork.Services.CityService;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    // Retrieve all cities
    @GetMapping("/all")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getCities();
        return cities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cities);
    }

    @GetMapping("/byCountry/{countryName}")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable String countryName) {
        List<City> cities = cityService.getCitiesByCountry(countryName);
        return cities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cities);
    }

    // Retrieve a city
    @GetMapping("/byName/{cityName}")
    public ResponseEntity<City> getCityByName(@PathVariable String cityName){
        City city = cityService.getCityByName(cityName);
        return city == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(city);
    }

    @GetMapping("/byId/{cityId}")
    public ResponseEntity<City> getCityById(@PathVariable int cityId){
        City city = cityService.getCityById(cityId);
        return city == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(city);
    }

    // Add or update a city
    @PostMapping("/save")
    public ResponseEntity<String> saveCity(@RequestBody City city) {
        try {
            cityService.save(city);
            return ResponseEntity.ok("City saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving city: " + e.getMessage());
        }
    }

    // Delete a country
    @DeleteMapping("/delete/{cityName}")
    public ResponseEntity<String> deleteCity(@PathVariable String cityName) {
        try {
            cityService.deleteByName(cityName);
            return ResponseEntity.ok("City deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting city: " + e.getMessage());
        }
    }

}
