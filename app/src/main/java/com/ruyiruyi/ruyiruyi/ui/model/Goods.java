package com.ruyiruyi.ruyiruyi.ui.model;

import java.io.Serializable;

public class Goods  implements Serializable {
    public int goodsId;
    public String goodsName;
    public String goodsImage;
    public String goodsPrice;
    public int goodsStock;
    public int goodsCount;
    public int goodsClassId;
    public int serviceTypeId;

    public Goods(int goodsId, String goodsName, String goodsImage, String goodsPrice, int goodsStock, int goodsCount,int serviceTypeId) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsImage = goodsImage;
        this.goodsPrice = goodsPrice;
        this.goodsStock = goodsStock;
        this.goodsCount = goodsCount;
        this.serviceTypeId = serviceTypeId;
    }

    public Goods() {
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(int goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }
}