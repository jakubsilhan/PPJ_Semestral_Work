package semestralwork.Services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import semestralwork.Models.City;
import semestralwork.Models.Measurement;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Value("${openweather.api.key}")
    private String API_KEY;

    @Value("${openweather.baseurl}")
    private String base_url;

    public WeatherService() {
    }

    //TODO add option to chose time interval
    public String getHistoricalWeather(String city) {
        WebClient webClient = WebClient.builder().baseUrl(base_url).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/history/city")
                        .queryParam("q", city)
                        .queryParam("appid", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Updates weather data for city (replaces old ones)
     * @param city
     */
    public void updateMeasurements(City city) {
        // Delete previous saved data
        MapSqlParameterSource delParams = new MapSqlParameterSource()
                .addValue("city_id", city.getId());
        String sql = "DELETE FROM Measurement WHERE City_id = :city_id";
        jdbc.update(sql, delParams);

        // Acquire new data
        String response = getHistoricalWeather(city.getName());
        List<Measurement> measurements = parseResponse(response, city);

        // Insert new data
        List<MapSqlParameterSource> batchParams = new ArrayList<>();

        for (Measurement measurement : measurements) {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("datetime", measurement.getDatetime())
                    .addValue("temp", measurement.getTemp())
                    .addValue("pressure", measurement.getPressure())
                    .addValue("humidity", measurement.getHumidity())
                    .addValue("temp_min", measurement.getTemp_min())
                    .addValue("temp_max", measurement.getTemp_max())
                    .addValue("weather", measurement.getWeather())
                    .addValue("weather_desc", measurement.getWeather_desc())
                    .addValue("wind_speed", measurement.getWind_speed())
                    .addValue("wind_deg", measurement.getWind_deg())
                    .addValue("city_id", measurement.getCity_id());

            batchParams.add(parameters);
        }
        String insertSql = "INSERT INTO Measurement (datetime, temp, pressure, humidity, temp_min, temp_max, weather, weather_desc, wind_speed, wind_deg, City_id) " +
                "VALUES (:datetime, :temp, :pressure, :humidity, :temp_min, :temp_max, :weather, :weather_desc, :wind_speed, :wind_deg, :city_id)";
        jdbc.batchUpdate(insertSql, batchParams.toArray(new MapSqlParameterSource[0]));
    }

    private List<Measurement> parseResponse(String response, City city) {
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
                JSONObject weatherObj = weatherArray.getJSONObject(0); // Assume only one weather object
                String weather = weatherObj.getString("main");
                String description = weatherObj.getString("description");

                Measurement tempMeasure = new Measurement(index, date, temp, pressure, humidity, temp_min, temp_max, weather, description, windSpeed, windDeg, city.getId());
                index++;
                measurements.add(tempMeasure);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return measurements;
    }
}
