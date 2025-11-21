package com.burrito.king.burrito_king_3;

public class Order {

    private String orderDetails;
    private int orderId;
    private String orderTime;
    private String orderStatus;

    public Order(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Order(int orderId, String orderStatus, String orderDetails) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}