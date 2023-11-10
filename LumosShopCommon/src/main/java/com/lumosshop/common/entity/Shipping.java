package com.lumosshop.common.entity;

import com.lumosshop.common.entity.control.Nation;
import jakarta.persistence.*;

@Entity
@Table(name = "Shipping")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float feeRate;
    private int dayLong;
    @Column(name = "CashOnDelivery")
    private boolean CashOnDelivery;
    @ManyToOne
    @JoinColumn(name = "nation_id")
    private Nation nation;

    @Column(name = "city")
    private String city;

    public Shipping() {
    }

    public Shipping(Integer id, float feeRate, int dayLong, boolean cashOnDelivery, Nation nation, String city) {
        this.id = id;
        this.feeRate = feeRate;
        this.dayLong = dayLong;
        CashOnDelivery = cashOnDelivery;
        this.nation = nation;
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(float feeRate) {
        this.feeRate = feeRate;
    }

    public int getDayLong() {
        return dayLong;
    }

    public void setDayLong(int dayLong) {
        this.dayLong = dayLong;
    }

    public boolean isCashOnDelivery() {
        return CashOnDelivery;
    }

    public void setCashOnDelivery(boolean cashOnDelivery) {
        CashOnDelivery = cashOnDelivery;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
