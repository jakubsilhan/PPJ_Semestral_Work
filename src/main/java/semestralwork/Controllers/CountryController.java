package semestralwork.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semestralwork.Models.Country;
import semestralwork.Services.CountryService;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // Retrieve all countries
    @GetMapping("/all")
    public ResponseEntity<List<Country>> getCountries(){
        List<Country> countries = countryService.getCountries();
        return countries.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(countries);
    }

    // Retrieve a country
    @GetMapping("/byName/{countryName}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String countryName){
        Country country = countryService.getCountryByName(countryName);
        return country == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(country);
    }

    @GetMapping("/byId/{countryId}")
    public ResponseEntity<Country> getCountryById(@PathVariable int countryId){
        Country country = countryService.getCountryById(countryId);
        return country == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(country);
    }

    // Add or update a country
    @PutMapping("/create")
    public ResponseEntity<String> createCountry(@RequestBody Country country) {
        try {
            countryService.create(country); // Separate method for creation
            return ResponseEntity.ok("Country created successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating country: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCountry(@RequestBody Country country) {
        try {
            countryService.update(country); // Separate method for updating
            return ResponseEntity.ok("Country updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating country: " + e.getMessage());
        }
    }


    // Delete a country
    @DeleteMapping("/delete/{countryName}")
    public ResponseEntity<String> deleteCountry(@PathVariable String countryName) {
        try {
            countryService.delete(countryName);
            return ResponseEntity.ok("Country deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting country: " + e.getMessage());
        }
    }
}
