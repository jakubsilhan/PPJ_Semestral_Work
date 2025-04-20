package semestralwork.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;

import java.sql.ResultSet;
import java.util.List;

public class CountriesDao {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    // Queries
    public List<Country> getCountries(){
        return jdbc.query("SELECT * FROM Country",
                (ResultSet rs, int rowNum) -> {
                    Country country = new Country();
                    country.setId(rs.getInt("id"));
                    country.setName(rs.getString("name"));
                    return country;
                });
    }

    public Country getCountry(int id){
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.queryForObject("SELECT * FROM Country WHERE id = :id", params,
                (ResultSet rs, int rowNum) -> {
                    Country country = new Country();
                    country.setId(rs.getInt("id"));
                    country.setName(rs.getString("name"));
                    return country;
                });
    }

    public Country getCountry(String name){
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return jdbc.queryForObject("SELECT * FROM Country WHERE name = :name", params,
                (ResultSet rs, int rowNum) -> {
                    Country country = new Country();
                    country.setId(rs.getInt("id"));
                    country.setName(rs.getString("name"));
                    return country;
                });
    }

    // Updates
    public boolean update(Country country) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(country);
        return jdbc.update("UPDATE Country SET name = :name where id = :id", params) == 1;
    }

    // Inserts
    public boolean create(Country country) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(country);
        return jdbc.update("INSERT INTO Country (name) VALUES (:name)", params) == 1;
    }

    public int[] create(List<Country> countries) {
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(countries.toArray());

        return jdbc.batchUpdate("INSERT INTO Country (name) VALUES (:name)", params);
    }

    // Deletes
    public boolean delete(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.update("DELETE FROM Country WHERE id = :id", params) == 1;
    }
}
