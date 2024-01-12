package com.dgpad.admin.order;

import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.Order_Summary;
import com.lumosshop.common.entity.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public interface OrderSummaryRepository extends PagingAndSortingRepository<Order_Summary, Integer>, CrudRepository<Order_Summary, Integer> {

    @Query("select new com.lumosshop.common.entity.order.Order_Summary(o.productCost,o.InterSum,o.shippingCharge,o.Qty,o.product.category.name) " +
            "from Order_Summary o where " +
            "o.order.orderDate between ?1 and ?2 order by o.order.orderDate asc ")
    public List<Order_Summary> findOrdersByCategoryAndDate(Date startDate, Date endDate);
    @Query("select new com.lumosshop.common.entity.order.Order_Summary(o.product.name,o.productCost,o.InterSum,o.shippingCharge,o.Qty) " +
            "from Order_Summary o where " +
            "o.order.orderDate between ?1 and ?2 order by o.order.orderDate asc ")
    public List<Order_Summary> findOrdersByProductAndDate(Date startDate, Date endDate);

    @Query("select o.product ,SUM(o.Qty) as totalQuantity from Order_Summary o" +
            " GROUP BY o.product.id order by totalQuantity desc limit 5")
    List<Product> findMostSellerProductAsc();

}
