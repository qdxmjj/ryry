package com.ruyiruyi.ruyiruyi.ui.multiType;

public class MeaageRy {

    public int orderType;
    public int orderState;
    public int orderStage;
    public String orderImage;
    public String orderName;
    public String orderNo;
    public String orderTime;
    public boolean isRead;

    public MeaageRy(int orderType, int orderState, int orderStage, String orderImage, String orderName, String orderNo, String orderTime, boolean isRead) {
        this.orderType = orderType;
        this.orderState = orderState;
        this.orderStage = orderStage;
        this.orderImage = orderImage;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.orderTime = orderTime;
        this.isRead = isRead;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getOrderStage() {
        return orderStage;
    }

    public void setOrderStage(int orderStage) {
        this.orderStage = orderStage;
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}