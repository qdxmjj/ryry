package com.ruyiruyi.ruyiruyi.ui.multiType;

public class Order {
    public String orderImage;
    public String orderName;
    public String orderNo;
    public String orderPrice;
    public String orderState;
    public String orderTime;
    public String orderType;
    public String storeId;

    public Order(String orderImage, String orderName, String orderNo, String orderPrice, String orderState, String orderTime, String orderType, String storeId) {
        this.orderImage = orderImage;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.orderPrice = orderPrice;
        this.orderState = orderState;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.storeId = storeId;
    }

    public Order() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}