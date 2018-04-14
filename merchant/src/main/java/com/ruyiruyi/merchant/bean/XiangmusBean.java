package com.ruyiruyi.merchant.bean;

public class XiangmusBean {
    private int id ;
    private String text;

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {

        return id;
    }

    public String getText() {
        return text;
    }

    public XiangmusBean() {
    }

    public XiangmusBean(int id, String text) {

        this.id = id;
        this.text = text;
    }
}