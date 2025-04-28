package semestralwork.Services;

import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;
import semestralwork.Repositories.CityRepository;
import semestralwork.Repositories.MeasurementRepository;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Value("${openweather.api.key}")
    private String API_KEY;

    @Value("${openweather.baseurl}")
    private String base_url;
    @Autowired
    private CityRepository cityRepository;

    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService() {
    }

    public String getHistoricalWeather(City city, Long from, Long to) {

        try {
            WebClient webClient = WebClient.builder().baseUrl(base_url).build();
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/history/city")
                            .queryParam("lat", city.getLatitude())
                            .queryParam("lon", city.getLongitude())
                            .queryParam("start", from)
                            .queryParam("end", to)
                            .queryParam("units", "metric")
                            .queryParam("appid", API_KEY)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Fetching weather data failed");
        }
    }

    @Transactional
    public void updateMeasurements(City city) {

        city = cityRepository.findById(city.getId()).orElse(null);
        if(city == null) {
            logger.warn("City not found");
            throw new RuntimeException("City not found");
        }

        Pageable pageable = PageRequest.of(0, 1);
        try {
            Measurement latestMeasurement = measurementRepository.findLatestMeasurement(city.getId(), pageable).get(0);
            if (!latestMeasurement.getDatetime().toLocalDate().isBefore(LocalDate.now())) {
                return;
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch latest measurement from database: {}", e.getMessage());
        }
        // Delete previous saved data
        measurementRepository.deleteByCityId(city.getId());

        // Set times
        Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long middle = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC);
        Long start = LocalDateTime.now().minusDays(14).toEpochSecond(ZoneOffset.UTC);

        // Acquire new data
        String response;
        response = getHistoricalWeather(city, start, middle); // First week
        List<Measurement> measurements = new ArrayList<>(parseResponse(response, city));
        response = getHistoricalWeather(city, middle, end); // Second week
        measurements.addAll(parseResponse(response, city));

        // Save data
        measurementRepository.saveAll(measurements);
    }

    public List<Measurement> parseResponse(String response, City city) {
        int index = 0;
        List<Measurement> measurements = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(response);

            JSONArray list = root.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject weatherItem = list.getJSONObject(i);

                long dt = weatherItem.getLong("dt");
                LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault());

                JSONObject main = weatherItem.getJSONObject("main");
                double temp = main.getDouble("temp");
                double temp_min = main.getDouble("temp_min");
                double temp_max = main.getDouble("temp_max");
                int pressure = main.getInt("pressure");
                int humidity = main.getInt("humidity");

                JSONObject wind = weatherItem.getJSONObject("wind");
                double windSpeed = wind.getDouble("speed");
                int windDeg = wind.getInt("deg");

                JSONArray weatherArray = weatherItem.getJSONArray("weather");
                JSONObject weatherObj = weatherArray.getJSONObject(0);
                String weather = weatherObj.getString("main");
                String description = weatherObj.getString("description");

                Measurement tempMeasure = new Measurement(0, date, temp, pressure, humidity, temp_min, temp_max, weather, description, windSpeed, windDeg, city);
                index++;
                measurements.add(tempMeasure);
            }
        } catch (Exception e) {
            logger.error("Parsing weather response error: {}", e.getMessage());
            throw new RuntimeException("Parsing weather response failed");
        }
        return measurements;
    }
}
