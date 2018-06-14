package com.ruyiruyi.merchant.ui.multiType.modle;


public class Dingdan {

    private String orderImage;
    private String orderName;
    private String platNumber;
    private String orderType;
    private String orderStage;
    private String orderNo;
    private String orderState;
    private long orderTime;
    private String isRead;

    public void setOrderStage(String orderStage) {
        this.orderStage = orderStage;
    }

    public String getOrderStage() {

        return orderStage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setPlatNumber(String platNumber) {
        this.platNumber = platNumber;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Dingdan() {


    }

    public String getOrderImage() {
        return orderImage;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getPlatNumber() {
        return platNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderState() {
        return orderState;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public String getIsRead() {
        return isRead;
    }
}