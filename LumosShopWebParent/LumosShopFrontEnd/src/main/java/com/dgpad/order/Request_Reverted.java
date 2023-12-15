package com.dgpad.order;

public class Request_Reverted {

    private Integer OrderID;
    private String justification;
    private String comment;

    public Request_Reverted(Integer orderID, String justification, String comment) {
        OrderID = orderID;
        this.justification = justification;
        this.comment = comment;
    }

    public Integer getOrderID() {
        return OrderID;
    }

    public void setOrderID(Integer orderID) {
        OrderID = orderID;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
