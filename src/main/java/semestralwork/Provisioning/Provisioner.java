package semestralwork.Provisioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import semestralwork.Models.CitiesDao;
import semestralwork.Models.City;
import semestralwork.Services.WeatherService;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class Provisioner {

    private static final Logger log = LoggerFactory.getLogger(Provisioner.class);

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private CitiesDao citiesDao;

    public void doProvision() {
        List<String> allTables;

        allTables = jdbc.getJdbcOperations().queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES", String.class);
        if (!allTables.contains("MEASUREMENT")) {
            log.warn("DB provisioner: No  tables exist and will be created");
            createDb();
            allTables = jdbc.getJdbcOperations().queryForList("SELECT TABLE_NAME FROM  INFORMATION_SCHEMA.TABLES", String.class);
            System.out.println(allTables);
        } else {
            log.info("DB Provisioner: Table OFFERS exists, all existing tables: " + allTables);
        }
    }

    public void createDb() {
        Resource rc = new ClassPathResource("create_tables.sql");
        try{
            ScriptUtils.executeSqlScript(dataSource.getConnection(), rc);
            List<City> cities = citiesDao.getCities();
            Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            Long start = LocalDateTime.now().minusDays(14).toEpochSecond(ZoneOffset.UTC);
            for (City city : cities) {
                weatherService.updateMeasurements(city, start, end);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
