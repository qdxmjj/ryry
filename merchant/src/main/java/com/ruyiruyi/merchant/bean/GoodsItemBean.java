package com.ruyiruyi.merchant.bean;


public class GoodsItemBean {

    private int amount;
    private int id;
    private String imgUrl;
    private String name;
    private double price;
    private double oldPrice;
    private boolean isSpecialPrice;
    private String goodsInfo;
    private int serviceId;
    private int serviceTypeId;
    private int soldNo;
    private int status;
    private int stockTypeId;
    private int storeId;
    private long time;


    public GoodsItemBean(String name) {
        this.name = name;
    }

    public GoodsItemBean() {
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public boolean isSpecialPrice() {
        return isSpecialPrice;
    }

    public void setSpecialPrice(boolean specialPrice) {
        isSpecialPrice = specialPrice;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public void setSoldNo(int soldNo) {
        this.soldNo = soldNo;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStockTypeId(int stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public int getSoldNo() {
        return soldNo;
    }

    public int getStatus() {
        return status;
    }

    public int getStockTypeId() {
        return stockTypeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public long getTime() {
        return time;
    }
}