package com.dgpad.checkout;

import java.util.Calendar;
import java.util.Date;

public class CheckoutModel {
    private int DeliveryETA;
    private boolean CashOnDeliveryAbility;

    private float cost;
    private float billTotal;

    private float totalPrice;
    private float shippingCharge;

    public Date getShippingDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, DeliveryETA);
        return calendar.getTime();
    }

    public void setShippingDate(Date shippingDate) {
    }

    public int getDeliveryETA() {
        return DeliveryETA;
    }

    public void setDeliveryETA(int deliveryETA) {
        DeliveryETA = deliveryETA;
    }

    public boolean isCashOnDeliveryAbility() {
        return CashOnDeliveryAbility;
    }

    public void setCashOnDeliveryAbility(boolean cashOnDeliveryAbility) {
        CashOnDeliveryAbility = cashOnDeliveryAbility;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(float billTotal) {
        this.billTotal = billTotal;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(float shippingCharge) {
        this.shippingCharge = shippingCharge;
    }
}
