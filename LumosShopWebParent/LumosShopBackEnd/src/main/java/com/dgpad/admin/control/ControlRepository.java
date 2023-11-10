package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlRepository extends CrudRepository<Control, String> {

    public List<Control> findByType(ControlType type);


}
