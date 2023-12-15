package com.dgpad.order;

public class Response_Reverted {
    private Integer OrderID;

    public Response_Reverted() {
    }

    public Response_Reverted(Integer orderID) {
        OrderID = orderID;
    }

    public Integer getOrderID() {
        return OrderID;
    }

    public void setOrderID(Integer orderID) {
        OrderID = orderID;
    }
}
