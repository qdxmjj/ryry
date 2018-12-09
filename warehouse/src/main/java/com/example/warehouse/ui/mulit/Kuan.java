package com.example.warehouse.ui.mulit;

/**
 * Created by Lenovo on 2018/11/19.
 */
public class Kuan {
    public String tireKuan;
    public boolean isClick;

    public Kuan(String tireKuan) {
        this.tireKuan = tireKuan;
    }

    public String getTireKuan() {
        return tireKuan;
    }

    public void setTireKuan(String tireKuan) {
        this.tireKuan = tireKuan;
    }

    public Kuan(String tireKuan, boolean isClick) {
        this.tireKuan = tireKuan;
        this.isClick = isClick;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}