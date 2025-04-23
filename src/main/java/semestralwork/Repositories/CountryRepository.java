package semestralwork.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semestralwork.Models.Country;

import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    @Query("SELECT c FROM Country c WHERE c.name = :name")
    public Optional<Country> findByName(@Param("name") String name);
}
