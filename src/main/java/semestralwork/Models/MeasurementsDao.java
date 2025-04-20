package semestralwork.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class measurementRowMapper implements RowMapper<Measurement> {

    @Override
    public Measurement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Measurement m = new Measurement();
        m.setId(rs.getInt("id"));
        m.setCity_id(rs.getInt("City_id"));
        m.setDatetime(rs.getTimestamp("datetime").toLocalDateTime());
        m.setTemp(rs.getDouble("temp"));
        m.setPressure(rs.getInt("pressure"));
        m.setHumidity(rs.getInt("humidity"));
        m.setTemp_min(rs.getDouble("temp_min"));
        m.setTemp_max(rs.getDouble("temp_max"));
        m.setWeather(rs.getString("weather"));
        m.setWeather_desc(rs.getString("weather_desc"));
        m.setWind_speed(rs.getDouble("wind_speed"));
        m.setWind_deg(rs.getInt("wind_deg"));
        return m;
    }
}

public class MeasurementsDao {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    // Queries
    public List<Measurement> getMeasurements() {
        return jdbc.query("SELECT * FROM Measurement", new measurementRowMapper());
    }

    public List<Measurement> getMeasurementsByCityId(int city_id) {
        MapSqlParameterSource params = new MapSqlParameterSource("city_id", city_id);
        return jdbc.query("SELECT * FROM Measurement WHERE City_id = :city_id", params, new measurementRowMapper());
    }

    public Measurement getMeasurementById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.query("SELECT * FROM Measurement WHERE id = :id", params, new measurementRowMapper()).get(0);
    }

    // Updates
    public boolean update(Measurement measurement){
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(measurement);
        return jdbc.update(
                "UPDATE Measurement SET " +
                        "datetime = :datetime, " +
                        "temp = :temp, " +
                        "pressure = :pressure, " +
                        "humidity = :humidity, " +
                        "temp_min = :temp_min, " +
                        "temp_max = :temp_max, " +
                        "weather = :weather, " +
                        "weather_desc = :weather_desc, " +
                        "wind_speed = :wind_speed, " +
                        "wind_deg = :wind_deg, " +
                        "City_id = :city_id", params) == 1;
    }

    // Inserts
    public boolean create(Measurement measurement) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(measurement);
        return jdbc.update("INSERT INTO Measurement " +
                "(datetime, temp, pressure, humidity, temp_min, temp_max, weather, weather_desc, wind_speed, wind_deg, City_id) Values " +
                "(:datetime, :temp, :pressure, :humidity, :temp_min, :temp_max, :weather, :weather_desc, :wind_speed, :wind_deg, :city_id)", params) == 1;
    }

    public int[] create(List<Measurement> measurements){
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(measurements.toArray());
        return jdbc.batchUpdate("INSERT INTO Measurement " +
                "(datetime, temp, pressure, humidity, temp_min, temp_max, weather, weather_desc, wind_speed, wind_deg, City_id) Values " +
                "(:datetime, :temp, :pressure, :humidity, :temp_min, :temp_max, :weather, :weather_desc, :wind_speed, :wind_deg, :city_id)", params);
    }

    // Deletes
    public boolean delete(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.update("DELETE FROM Measurement WHERE id = :id", params) == 1;
    }

}
