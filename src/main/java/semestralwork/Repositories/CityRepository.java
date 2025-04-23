package semestralwork.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semestralwork.Models.City;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

    @Query("SELECT c FROM City c WHERE c.name = :name")
    public Optional<City> findByName(@Param("name")String name);

    @Query("SELECT c FROM City c WHERE c.country.id = :Country_id")
    public List<City> findByCountryId(@Param("Country_id")int country_id);
}
