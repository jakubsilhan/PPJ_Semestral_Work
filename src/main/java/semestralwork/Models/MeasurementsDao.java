package semestralwork.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import semestralwork.Services.WeatherService;

public class MeasurementsDao {
    // TODO Implement CRUD and aggregation operations

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private NamedParameterJdbcOperations jdbc;
}
