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
    public int system;
    public String serviceDesc;
    public int discountFlag;    //(是否折扣商品 0 不是 1 是)
    public String originalPrice;    //(原价)
    public String stockDesc;    //(商品描述)

    public GoodsNew(int goodsId, String goodsImage, String goodsName, String goodsPrice, int goodsAmount, int currentGoodsAmount, int goodsClassId, int serviceTypeId) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
        this.currentGoodsAmount = currentGoodsAmount;
        this.goodsClassId = goodsClassId;
        this.serviceTypeId = serviceTypeId;
        this.system = 2;
        this.serviceDesc = "";
    }

    public GoodsNew(int goodsId, String goodsImage, String goodsName, String goodsPrice, int goodsAmount, int currentGoodsAmount, int goodsClassId, int serviceTypeId, int system, String serviceDesc, int discountFlag, String originalPrice, String stockDesc) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
        this.currentGoodsAmount = currentGoodsAmount;
        this.goodsClassId = goodsClassId;
        this.serviceTypeId = serviceTypeId;
        this.system = system;
        this.serviceDesc = serviceDesc;
        this.discountFlag = discountFlag;
        this.originalPrice = originalPrice;
        this.stockDesc = stockDesc;
    }

    public GoodsNew(int goodsId, String goodsImage, String goodsName, String goodsPrice, int goodsAmount, int currentGoodsAmount, int goodsClassId, int serviceTypeId, int system) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
        this.currentGoodsAmount = currentGoodsAmount;
        this.goodsClassId = goodsClassId;
        this.serviceTypeId = serviceTypeId;
        this.system = system;
        this.serviceDesc = "";
    }

    public GoodsNew(int goodsId, String goodsImage, String goodsName, String goodsPrice, int goodsAmount, int currentGoodsAmount, int goodsClassId, int serviceTypeId, int system, String serviceDesc) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
        this.currentGoodsAmount = currentGoodsAmount;
        this.goodsClassId = goodsClassId;
        this.serviceTypeId = serviceTypeId;
        this.system = system;
        this.serviceDesc = serviceDesc;
    }

    public int getDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(int discountFlag) {
        this.discountFlag = discountFlag;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getStockDesc() {
        return stockDesc;
    }

    public void setStockDesc(String stockDesc) {
        this.stockDesc = stockDesc;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
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