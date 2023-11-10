package com.dgpad.controlCenter;

import com.lumosshop.common.entity.control.Nation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NationRepository extends CrudRepository<Nation, Integer> {
    public List<Nation> findAllByOrderByNameAsc();

    @Query("select n from Nation n where n.code = ?1")
    public Nation findByCode(String code);


}
