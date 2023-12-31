package com.lumosshop.common.entity.interactions;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.entity.product.Product;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "interactions")
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;
    private double value;
    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    private Date timestamp;

    public Interaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", interactionType=" + interactionType +
                ", value=" + value +
                ", customer=" + customer.getFullName() +
                ", product=" + product.getName() +
                ", timestamp=" + timestamp +
                '}';
    }
}
