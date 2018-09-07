package com.ruyiruyi.merchant.ui.multiType;

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
    private double shoePrice_front;
    private String shoeTitle_rear;
    private double shoePrice_rear;
    private String carNumber;
    private String userPhone;

    private int shoeNum_front;
    private int shoeNum_rear;

    private int tyreId_front;
    private int tyreId_rear;

    private String orderImg_front;
    private String orderImg_rear;

    private int fontRearFlag_front;
    private int fontRearFlag_rear;

    private int orderId;

    public OrderForShipment() {
        orderTime = 0;
        shoeNum_front = 0;//默认为0
        shoeNum_rear = 0;//默认为0
        shoeTitle_front = "-1";
        shoeTitle_rear = "-1";
        tyreId_front = -1;
        tyreId_rear = -1;
        orderImg_front = "-1";
        orderImg_rear = "-1";
        fontRearFlag_front = -1;
        fontRearFlag_rear = -1;
        orderId = -1;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFontRearFlag_rear() {
        return fontRearFlag_rear;
    }

    public void setFontRearFlag_rear(int fontRearFlag_rear) {
        this.fontRearFlag_rear = fontRearFlag_rear;
    }

    public int getFontRearFlag_front() {
        return fontRearFlag_front;
    }

    public void setFontRearFlag_front(int fontRearFlag_front) {
        this.fontRearFlag_front = fontRearFlag_front;
    }

    public String getOrderImg_front() {
        return orderImg_front;
    }

    public void setOrderImg_front(String orderImg_front) {
        this.orderImg_front = orderImg_front;
    }

    public String getOrderImg_rear() {
        return orderImg_rear;
    }

    public void setOrderImg_rear(String orderImg_rear) {
        this.orderImg_rear = orderImg_rear;
    }

    public int getTyreId_front() {
        return tyreId_front;
    }

    public void setTyreId_front(int tyreId_front) {
        this.tyreId_front = tyreId_front;
    }

    public int getTyreId_rear() {
        return tyreId_rear;
    }

    public void setTyreId_rear(int tyreId_rear) {
        this.tyreId_rear = tyreId_rear;
    }

    public int getShoeNum_front() {
        return shoeNum_front;
    }

    public void setShoeNum_front(int shoeNum_front) {
        this.shoeNum_front = shoeNum_front;
    }

    public int getShoeNum_rear() {
        return shoeNum_rear;
    }

    public void setShoeNum_rear(int shoeNum_rear) {
        this.shoeNum_rear = shoeNum_rear;
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
