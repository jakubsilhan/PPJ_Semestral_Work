package semestralwork.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.ResultSet;
import java.util.List;

public class CountriesDao {

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    public List<Country> getCountries(){
        return jdbc.query("select * from Country",
                (ResultSet rs, int rowNum) -> {
                    Country country = new Country();
                    country.setId(rs.getInt("id"));
                    country.setName(rs.getString("name"));
                    return country;
                });
    }
}
