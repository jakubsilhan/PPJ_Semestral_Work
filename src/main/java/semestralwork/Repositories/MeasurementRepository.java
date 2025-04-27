package semestralwork.Repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semestralwork.DTOs.MeasurementAggregate;
import semestralwork.Models.Measurement;

import java.util.List;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Integer> {

    // Select
    @Query("SELECT m FROM Measurement m WHERE m.city.id = :City_id")
    public List<Measurement> findByCityId(@Param("City_id") int city_id);

    @Query("SELECT m FROM Measurement m WHERE m.city.id = :City_id ORDER BY m.datetime DESC")
    public List<Measurement> findLatestMeasurement(@Param("City_id") int city_id, Pageable pageable);

    @Query("SELECT new semestralwork.DTOs.MeasurementAggregate(" +
            "AVG(m.temp), AVG(m.pressure), AVG(m.humidity), " +
            "AVG(m.temp_min), AVG(m.temp_max), AVG(m.wind_speed)) " +
            "FROM Measurement m " +
            "WHERE m.city.id = :City_id " +
            "GROUP BY CAST(m.datetime AS date) " +
            "ORDER BY CAST(m.datetime AS date) DESC")
    public List<MeasurementAggregate> findDailyAverage(@Param("City_id") int cityId, Pageable pageable);

    @Query("SELECT new semestralwork.DTOs.MeasurementAggregate(" +
            "AVG(m.temp), AVG(m.pressure), AVG(m.humidity), " +
            "AVG(m.temp_min), AVG(m.temp_max), AVG(m.wind_speed)) " +
            "FROM Measurement m " +
            "WHERE m.city.id = :City_id " +
            "GROUP BY FUNCTION('YEAR', m.datetime), FUNCTION('WEEK', m.datetime) " +
            "ORDER BY FUNCTION('YEAR', m.datetime) DESC, FUNCTION('WEEK', m.datetime) DESC")
    public List<MeasurementAggregate> findWeeklyAverage(@Param("City_id") int cityId, Pageable pageable);

    // Delete
    @Modifying
    @Transactional
    @Query("DELETE FROM Measurement m WHERE m.city.id = :City_id")
    public void deleteByCityId(@Param("City_id") int city_id);
}
