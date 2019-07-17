package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * Created by 86135 on 2019/5/28.
 */
public class RenewYear {
    public int year;
    public String price;
    public boolean isChoose;

    public RenewYear(int year, String price) {
        this.year = year;
        this.price = price;
        isChoose = false;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {

        this.price = price;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}