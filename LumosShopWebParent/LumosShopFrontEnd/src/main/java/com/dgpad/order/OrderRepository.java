package com.dgpad.order;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>, CrudRepository<Order, Integer> {


    public Order findByIdAndCustomer(Integer id, Customer customer);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderSummaries od JOIN od.product p "
            + "WHERE o.customer.id = :customerId "
            + "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(o.phase) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Order> findAllByCustomerAndKeyword(@Param("keyword") String keyword, @Param("customerId") Integer customerId, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o WHERE o.customer.id = :customerId")
    Page<Order> findAllByCustomer(@Param("customerId") Integer customerId, Pageable pageable);
}

