package com.dgpad.admin.Shipping;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShippingRepository extends PagingAndSortingRepository<Shipping,Integer>,CrudRepository<Shipping, Integer> {
    public Long countById(Integer id);

    @Query("select sh from Shipping sh where concat(sh.id,' ',sh.nation.name,' ',sh.city,' ',sh.dayLong,' ')like %?1%")
    public Page<Shipping> findAll(String keyWord, Pageable pageable);

    @Modifying
    @Query("update Shipping sh set sh.CashOnDelivery = ?2 where sh.id = ?1")
    public void updateCashOnDeliveryAbility(Integer id, boolean ability);

    @Query("select sh from Shipping sh where sh.nation.id = ?1 and sh.city = ?2")
    public Shipping findByNationAndCity(Integer nationID, String city);
}
