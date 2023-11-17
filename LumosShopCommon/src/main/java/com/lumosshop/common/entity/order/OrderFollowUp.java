package com.lumosshop.common.entity.order;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "order_followUp")
public class OrderFollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Date Timestamp;
    @Column(name = "remarks", length = 256)
    private String Remarks;

    @Enumerated(EnumType.STRING)
    private Order_Phase orderPhase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public Order_Phase getOrderPhase() {
        return orderPhase;
    }

    public void setOrderPhase(Order_Phase orderPhase) {
        this.orderPhase = orderPhase;
    }
}
