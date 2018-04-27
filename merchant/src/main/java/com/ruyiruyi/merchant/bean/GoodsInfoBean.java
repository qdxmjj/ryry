package com.ruyiruyi.merchant.bean;

public class GoodsInfoBean {

    private int goodsId;
    private String img_url;
    private String goodsName;
    private String price;
    private String goodsType;
    private String kucun;
    private String goodsStatus;

    public GoodsInfoBean() {
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getPrice() {
        return price;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public String getKucun() {
        return kucun;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }
}