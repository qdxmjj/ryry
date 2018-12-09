package com.example.warehouse.ui.mulit;

/**
 * Created by Lenovo on 2018/11/19.
 */
public class Bian {
    public String bianNmae;
    public boolean isClick;

    public Bian(String bianNmae) {
        this.bianNmae = bianNmae;
    }

    public Bian(String bianNmae, boolean isClick) {
        this.bianNmae = bianNmae;
        this.isClick = isClick;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getBianNmae() {
        return bianNmae;
    }

    public void setBianNmae(String bianNmae) {
        this.bianNmae = bianNmae;
    }
}