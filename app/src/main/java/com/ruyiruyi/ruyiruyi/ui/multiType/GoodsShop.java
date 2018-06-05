package com.ruyiruyi.ruyiruyi.ui.multiType;

public class GoodsShop {
    public int goodsId;
    public String goodsImage;
    public String goodsName;
    public String goodsPrice;
    public String goodsDistance;
    public int serviceId;
    public int serviceTypeId;
    public int storeId;
    public String storeName;
    public String storeImage;


    public GoodsShop(int goodsId, String goodsImage, String goodsName, String goodsPrice, String goodsDistance, int serviceId, int serviceTypeId, int storeId, String storeName, String storeImage) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsDistance = goodsDistance;
        this.serviceId = serviceId;
        this.serviceTypeId = serviceTypeId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }



    public String getGoodsDistance() {
        return goodsDistance;
    }

    public void setGoodsDistance(String goodsDistance) {
        this.goodsDistance = goodsDistance;
    }
}