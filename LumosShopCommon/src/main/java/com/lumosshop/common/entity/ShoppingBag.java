package com.lumosshop.common.entity;

import com.lumosshop.common.entity.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "shopping_bag")
public class ShoppingBag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private int quantity;

    @Transient
    private float shippingCharge;

    public float getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(float shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public ShoppingBag() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ShoppingBag{" +
                "id=" + id +
                ", product=" + product +
                ", customer=" + customer +
                ", quantity=" + quantity +
                '}';
    }

    @Transient
    public float getThePriceMultiplyByQty() {
        return product.getDiscountPrice() * quantity;
    }
}
