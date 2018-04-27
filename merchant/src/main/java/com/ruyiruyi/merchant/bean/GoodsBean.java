package com.ruyiruyi.merchant.bean;

public class GoodsBean {
    private int goods_id;
    private String img_url;
    private String txt;
    private String kucun;
    private String yishou;
    private String money;

    public GoodsBean() {
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public void setYishou(String yishou) {
        this.yishou = yishou;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTxt() {
        return txt;
    }

    public String getKucun() {
        return kucun;
    }

    public String getYishou() {
        return yishou;
    }

    public String getMoney() {
        return money;
    }
}