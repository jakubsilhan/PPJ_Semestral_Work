package semestralwork.Services;

import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * Updates weather data for city (replaces old ones)
     */
    @Transactional
    public void updateMeasurements(City city, Long from, Long to) {

        city = cityRepository.findById(city.getId()).orElse(null);
        if(city == null) {
            throw new RuntimeException("City not found");
        }

        Pageable pageable = PageRequest.of(0, 1);
        try {
            Measurement latestMeasurement = measurementRepository.findLatestMeasurement(city.getId(), pageable).get(0);
            if (!latestMeasurement.getDatetime().toLocalDate().isBefore(LocalDate.now())) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Delete previous saved data
        measurementRepository.deleteByCityId(city.getId());

        // Acquire new data
        String response = getHistoricalWeather(city, from, to);
        List<Measurement> measurements = parseResponse(response, city);

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
            System.out.println(e.getMessage());
        }
        return measurements;
    }
}
