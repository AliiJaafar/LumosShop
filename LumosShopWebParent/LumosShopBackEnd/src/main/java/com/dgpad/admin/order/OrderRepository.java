package com.dgpad.admin.order;

import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.Order_Phase;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>, CrudRepository<Order, Integer> {

    public Long countById(Integer id);

    @Query("select o from Order  o where concat(o.id,' ',o.customer.firstName,' ',o.customer.lastName," +
            "' ' ,o.city,' ' , o.nation,' ' ,o.phase,' ' , o.paymentChoice,' ' ,o.phoneNumber)like %?1%")
    public Page<Order> findAll(String keyWord, Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.phase = ?2 WHERE o.id = ?1")
    public void updatePhase(Integer orderId, Order_Phase newPhase);


    @Query("select new com.lumosshop.common.entity.order.Order(o.id,o.orderDate,o.totalPrice,o.productCost,o.InterSum) from Order o where " +
            "o.orderDate between ?1 and ?2 order by o.orderDate asc ")
    public List<Order> findOrdersByDate(Date startDate, Date endDate);



}
