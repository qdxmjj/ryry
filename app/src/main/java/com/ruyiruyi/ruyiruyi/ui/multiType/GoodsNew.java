package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.io.Serializable;

public class GoodsNew implements Serializable {
    public int goodsId;
    public String goodsImage;
    public String goodsName;
    public String goodsPrice;
    public int goodsAmount;
    public int currentGoodsAmount;
    public int goodsClassId;
    public int serviceTypeId;

    public GoodsNew(int goodsId, String goodsImage, String goodsName, String goodsPrice, int goodsAmount, int currentGoodsAmount, int goodsClassId, int serviceTypeId) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
        this.currentGoodsAmount = currentGoodsAmount;
        this.goodsClassId = goodsClassId;
        this.serviceTypeId = serviceTypeId;
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

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getCurrentGoodsAmount() {
        return currentGoodsAmount;
    }

    public void setCurrentGoodsAmount(int currentGoodsAmount) {
        this.currentGoodsAmount = currentGoodsAmount;
    }

    public int getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(int goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}