package com.dgpad.shoppingBag;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShoppingBagRepository extends CrudRepository<ShoppingBag, Integer> {

    @Modifying
    @Query("delete ShoppingBag s where s.customer.id = ?1")
    public void deleteByCustomer(Integer customerId);
    public ShoppingBag findByCustomerAndProduct(Customer customer, Product product);
    public List<ShoppingBag> findByCustomer(Customer customer);

    @Modifying
    @Query("delete from ShoppingBag s where s.product.id = ?1 and s.customer.id = ?2")
    public void deleteByCustomerAndProduct(Integer productID,Integer customerID);

    @Modifying
    @Query("update ShoppingBag s set s.quantity = ?3 where s.customer.id = ?1 and s.product.id = ?2")
    public void changeQuantity( Integer customerID, Integer productID,Integer quantity);
}
