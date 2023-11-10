package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.City;
import com.lumosshop.common.entity.control.Nation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Integer> {
    public List<City> findByNationOrderByNameAsc(Nation nation);
}
