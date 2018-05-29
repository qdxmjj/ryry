package com.ruyiruyi.ruyiruyi.ui.multiType;

public class GoodsShop {
    public int goodsId;
    public String goodsImage;
    public String goodsName;
    public String goodsPrice;
    public String goodsAddress;
    public String goodsDistance;

    public GoodsShop(int goodsId, String goodsImage, String goodsName, String goodsPrice, String goodsAddress, String goodsDistance) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAddress = goodsAddress;
        this.goodsDistance = goodsDistance;
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

    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    public String getGoodsDistance() {
        return goodsDistance;
    }

    public void setGoodsDistance(String goodsDistance) {
        this.goodsDistance = goodsDistance;
    }
}