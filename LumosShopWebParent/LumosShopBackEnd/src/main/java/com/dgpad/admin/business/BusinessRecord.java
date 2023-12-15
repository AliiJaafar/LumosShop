package com.dgpad.admin.business;

import java.util.Objects;

public class BusinessRecord {
    private String code;
    private float totalRevenue;
    private float netRevenue;
    private int totalTransactions;
    private int totalProducts;


    public BusinessRecord() {
    }

    public BusinessRecord(String code) {
        this.code = code;
    }

    public BusinessRecord(String code, float totalRevenue, float netRevenue) {
        this.code = code;
        this.totalRevenue = totalRevenue;
        this.netRevenue = netRevenue;
    }

    public BusinessRecord(String code, float totalRevenue, float netRevenue, int totalProducts) {
        this.code = code;
        this.totalRevenue = totalRevenue;
        this.netRevenue = netRevenue;
        this.totalProducts = totalProducts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public void increaseTotalRevenue(float value) {
        this.totalRevenue += value;
    }

    public float getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(float netRevenue) {
        this.netRevenue = netRevenue;
    }
    public void increaseNetRevenue(float value) {
        this.netRevenue += value;
    }


    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public void raiseTotalTransactions() {
        this.totalTransactions++;
    }
    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public void raiseTotalProduct(int num) {
        this.totalProducts += num;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessRecord that = (BusinessRecord) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "BusinessRecord{" +
                "code='" + code + '\'' +
                ", totalRevenue=" + totalRevenue +
                ", netRevenue=" + netRevenue +
                ", totalTransactions=" + totalTransactions +
                ", totalProducts=" + totalProducts +
                '}';
    }
}
