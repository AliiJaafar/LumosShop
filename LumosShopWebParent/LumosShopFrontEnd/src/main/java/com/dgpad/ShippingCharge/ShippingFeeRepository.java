package com.dgpad.ShippingCharge;

import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.control.Nation;
import org.springframework.data.repository.CrudRepository;

public interface ShippingFeeRepository extends CrudRepository<Shipping, Integer> {


    public Shipping findByNationAndCity(Nation nation , String city);
    public Shipping findByNation(Nation nation);

    public Shipping findByCity(String city);

}
