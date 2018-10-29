package com.ruyiruyi.merchant.ui.multiType;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/30 9:18
 */
public class SaleIncome {
    private String orderImg;
    private String shoeTitle;
    private int shoeNum;
    private long orderTime;
    private double orderPrice;

    private int userId;
    private int storeId;
    private String storeName;
    private String orderNo;

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public String getShoeTitle() {
        return shoeTitle;
    }

    public void setShoeTitle(String shoeTitle) {
        this.shoeTitle = shoeTitle;
    }

    public int getShoeNum() {
        return shoeNum;
    }

    public void setShoeNum(int shoeNum) {
        this.shoeNum = shoeNum;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}