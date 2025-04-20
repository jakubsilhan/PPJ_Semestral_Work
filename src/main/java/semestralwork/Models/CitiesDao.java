package semestralwork.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class CityRowMapper implements RowMapper<City> {
    @Override
    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("id"));
        city.setName(rs.getString("name"));
        city.setLatitude(rs.getDouble("latitude"));
        city.setLongitude(rs.getDouble("longitude"));
        city.setCountry_id(rs.getInt("country_id"));
        return city;
    }
}

public class CitiesDao {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    // Queries
    public List<City> getCities(){
        return jdbc.query("SELECT * FROM City", new CityRowMapper());
    }

    public List<City> getCitiesByCountryId(int country_id){
        MapSqlParameterSource params = new MapSqlParameterSource("country_id", country_id);
        return jdbc.query("SELECT * FROM City WHERE Country_id = :country_id", params, new CityRowMapper());
    }

    public City getCity(int id){
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.query("SELECT * FROM City WHERE id = :id", params, new CityRowMapper()).get(0);
    }

    public City getCity(String name){
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return jdbc.query("SELECT * FROM City WHERE name = :name", params, new CityRowMapper()).get(0);
    }

    // Updates
    public boolean update(City city){
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(city);
        return jdbc.update("UPDATE City SET name = :name, latitude = :latitude, longitude = :longitude, Country_id = :country_id WHERE id = :id ", params) == 1;
    }

    // Inserts
    public boolean create(City city){
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(city);
        return jdbc.update("INSERT INTO City (name, latitude, longitude, Country_id) VALUES (:name, :latitude, :longitude, :country_id)", params) == 1;
    }

    public int[] create(List<City> cities){
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(cities.toArray());
        return jdbc.batchUpdate("INSERT INTO City (name, latitude, longitude, Country_id) VALUES (:name, :latitude, :longitude, :country_id)", params);
    }

    // Deletes
    public boolean delete(int id){
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.update("DELETE FROM City WHERE id = :id", params) == 1;
    }
}
