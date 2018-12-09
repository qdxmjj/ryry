package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.Goods;

public class GoodsInfo{
    public int currentCount;
    public int goodsId;
    public String goodsName;
    public String goodsImage;
    public String goodsPrice;
    public int goodsStock;
    public int goodsCount;
    public int goodsClassId;
    public int serviceTypeId;
    public int discountFlag; // 1 是特殊商品  2普通商品


    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(int discountFlag) {
        this.discountFlag = discountFlag;
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

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
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