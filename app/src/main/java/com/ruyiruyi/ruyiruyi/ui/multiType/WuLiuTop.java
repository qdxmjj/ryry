package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * Created by Lenovo on 2019/1/14.
 */
public class WuLiuTop {
    public String goodsImage;
    public String goodsName;
    public String wuliuName;
    public String wuliuNo;
    public String wuliuPhone;
    public String addredd;

    public WuLiuTop(String goodsImage, String goodsName, String wuliuName, String wuliuNo, String wuliuPhone) {
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.wuliuName = wuliuName;
        this.wuliuNo = wuliuNo;
        this.wuliuPhone = wuliuPhone;
    }

    public WuLiuTop(String goodsImage, String goodsName, String wuliuName, String wuliuNo, String wuliuPhone, String addredd) {
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.wuliuName = wuliuName;
        this.wuliuNo = wuliuNo;
        this.wuliuPhone = wuliuPhone;
        this.addredd = addredd;
    }

    public String getAddredd() {
        return addredd;
    }

    public void setAddredd(String addredd) {
        this.addredd = addredd;
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

    public String getWuliuName() {
        return wuliuName;
    }

    public void setWuliuName(String wuliuName) {
        this.wuliuName = wuliuName;
    }

    public String getWuliuNo() {
        return wuliuNo;
    }

    public void setWuliuNo(String wuliuNo) {
        this.wuliuNo = wuliuNo;
    }

    public String getWuliuPhone() {
        return wuliuPhone;
    }

    public void setWuliuPhone(String wuliuPhone) {
        this.wuliuPhone = wuliuPhone;
    }
}