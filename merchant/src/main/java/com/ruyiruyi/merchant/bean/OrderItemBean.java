package com.ruyiruyi.merchant.bean;


public class OrderItemBean {

    private String storeId;
    private String goodsId;

    private String imgUrl;
    private String title;
    private String bianhao;
    private String price;
    private String status;
    private long orderTime;
    private String orderType;

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public long getOrderTime() {

        return orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public OrderItemBean() {
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getBianhao() {
        return bianhao;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}