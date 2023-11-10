package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.Nation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NationRepository extends CrudRepository<Nation, Integer> {
    public List<Nation> findAllByOrderByNameAsc();

}
