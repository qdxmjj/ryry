package com.ruyiruyi.merchant.ui.multiType.modle;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/3 15:24
 */

public class OrderForShipment {
    private int storeId;
    private int userId;
    private String orderNo;
    private long orderTime;
    private boolean isShoeConsistent;
    private String shoeTitle_front;
    private String shoePicUrl_front;
    private double shoePrice_front;
    private String shoeTitle_rear;
    private String shoePicUrl_rear;
    private double shoePrice_rear;
    private String carNumber;
    private String userPhone;

    public OrderForShipment() {
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public boolean isShoeConsistent() {
        return isShoeConsistent;
    }

    public void setShoeConsistent(boolean shoeConsistent) {
        isShoeConsistent = shoeConsistent;
    }

    public String getShoeTitle_front() {
        return shoeTitle_front;
    }

    public void setShoeTitle_front(String shoeTitle_front) {
        this.shoeTitle_front = shoeTitle_front;
    }

    public String getShoePicUrl_front() {
        return shoePicUrl_front;
    }

    public void setShoePicUrl_front(String shoePicUrl_front) {
        this.shoePicUrl_front = shoePicUrl_front;
    }

    public double getShoePrice_front() {
        return shoePrice_front;
    }

    public void setShoePrice_front(double shoePrice_front) {
        this.shoePrice_front = shoePrice_front;
    }

    public String getShoeTitle_rear() {
        return shoeTitle_rear;
    }

    public void setShoeTitle_rear(String shoeTitle_rear) {
        this.shoeTitle_rear = shoeTitle_rear;
    }

    public String getShoePicUrl_rear() {
        return shoePicUrl_rear;
    }

    public void setShoePicUrl_rear(String shoePicUrl_rear) {
        this.shoePicUrl_rear = shoePicUrl_rear;
    }

    public double getShoePrice_rear() {
        return shoePrice_rear;
    }

    public void setShoePrice_rear(double shoePrice_rear) {
        this.shoePrice_rear = shoePrice_rear;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
