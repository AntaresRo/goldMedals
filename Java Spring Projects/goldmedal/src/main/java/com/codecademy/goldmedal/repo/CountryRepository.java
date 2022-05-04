package com.codecademy.goldmedal.repo;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository <Country, Long> {
    Optional<Country> findCountriesByName(String countryName);

    List<Country> getAllByOrderByNameAsc();
    List<Country> getAllByOrderByNameDesc();
    List<Country> getAllByOrderByGdpAsc();
    List<Country> getAllByOrderByGdpDesc();
    List<Country> getAllByOrderByPopulationAsc();
    List<Country> getAllByOrderByPopulationDesc();
    Optional<Country> getByName(String name);
}
