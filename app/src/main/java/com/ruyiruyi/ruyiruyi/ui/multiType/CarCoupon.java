package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * Created by Lenovo on 2018/9/13.
 */
public class CarCoupon {
    public String couponName;
    public String couponCount;

    public CarCoupon(String couponName, String couponCount) {
        this.couponName = couponName;
        this.couponCount = couponCount;
    }

    public CarCoupon() {
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }
}