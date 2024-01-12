package com.dgpad.order;

import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.entity.order.Order_Summary;
import com.lumosshop.common.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderSummaryRepository extends JpaRepository<Order_Summary, Integer> {

    @Query("SELECT COUNT(os) FROM Order_Summary os " +
            "JOIN OrderFollowUp F ON os.order.id = F.order.id " +
            "WHERE os.product.id = :productID AND os.order.customer.id = :customerID AND F.orderPhase = :phase")
    Long getProductCountForCustomerByOrderStatus(@Param("phase") Order_Phase phase,
                                                 @Param("customerID") Integer customerID,
                                                 @Param("productID") Integer productID);


    @Query(value = "select o.product ,SUM(o.Qty) as totalQuantity from Order_Summary o" +
            " GROUP BY o.product.id order by totalQuantity desc limit ?1")
    List<Product> findMostSellerProductDesc(Integer limit);

    @Query("select o.product.id, o.product.name, count(o.product.id) AS purchaseCount  from Order_Summary o" +
            " GROUP BY o.product.id,o.product.name " +
            "order by purchaseCount DESC ")
    List<Object[]> findPopularProducts();


}
