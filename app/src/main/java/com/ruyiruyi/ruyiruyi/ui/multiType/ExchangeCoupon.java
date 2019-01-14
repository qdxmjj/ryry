package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * Created by Lenovo on 2018/12/25.
 */
public class ExchangeCoupon {
    public int id;
    public String couponName;
    public String couponPrice;
    public String couponAmount;
    public String score;

    public ExchangeCoupon(int id, String couponName, String couponPrice, String couponAmount, String score) {
        this.id = id;
        this.couponName = couponName;
        this.couponPrice = couponPrice;
        this.couponAmount = couponAmount;
        this.score = score;
    }

    public ExchangeCoupon(int id, String couponName, String couponPrice, String couponAmount) {
        this.id = id;
        this.couponName = couponName;
        this.couponPrice = couponPrice;
        this.couponAmount = couponAmount;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }
}