package com.dgpad.admin.order;

public class OrderDTO {
    private Integer orderId;
    private String phase;

    public OrderDTO(Integer orderId, String phase) {
        this.orderId = orderId;
        this.phase = phase;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
