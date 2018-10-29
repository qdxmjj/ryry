package com.ruyiruyi.merchant.bean;

public class ItemNullBean {
    private String txt;
    private int resPicId;


    public ItemNullBean() {
        this.resPicId = 100100;
    }

    public ItemNullBean(String txt) {
        this.txt = txt;
        this.resPicId = 100100;
    }

    public ItemNullBean(String txt, int resPicId) {
        this.txt = txt;
        this.resPicId = resPicId;
    }

    public ItemNullBean(int resPicId) {
        this.resPicId = resPicId;
    }

    public int getResPicId() {
        return resPicId;
    }

    public void setResPicId(int resPicId) {
        this.resPicId = resPicId;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}