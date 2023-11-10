package com.dgpad.controlCenter;

import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlRepository extends CrudRepository<Control, String> {

    public List<Control> findByType(ControlType type);

    public Control findByKey(String key);


    @Query("select c from Control c where c.type = ?1 or c.type = ?2")
    public List<Control> findByManyType(ControlType type1,ControlType type2);

}
