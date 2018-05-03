package com.ruyiruyi.ruyiruyi.ui.multiType;

public class GoodsVertical {
    public int goodsId;
    public String goodsImage;
    public String goodsName;
    public String goodsPrice;
    public int goodsAmount;

    public GoodsVertical() {
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public GoodsVertical(int goodsId, String goodsImage, String goodsName, String goodsPrice, int goodsAmount) {

        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsAmount = goodsAmount;
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


}