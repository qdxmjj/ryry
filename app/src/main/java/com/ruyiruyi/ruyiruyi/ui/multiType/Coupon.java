package com.ruyiruyi.ruyiruyi.ui.multiType;

public class Coupon {
    public int couponId;
    public String couponName;
    public int couponType;      //1 服务券   2现金券
    public int couponViewTypeId;    //2精致洗车券  3四轮定位券  7 10元现金券
    public int couponStates;  // 1已使用  2未使用  3已过期
    public String startTime;
    public String endTime;
    public String carNumber;
    public boolean isCanUse;


    public Coupon(int couponId, String couponName, int couponType, int couponViewTypeId, int couponStates, String startTime, String endTime, String carNumber, boolean isCanUse) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponViewTypeId = couponViewTypeId;
        this.couponStates = couponStates;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carNumber = carNumber;
        this.isCanUse = isCanUse;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public int getCouponViewTypeId() {
        return couponViewTypeId;
    }

    public void setCouponViewTypeId(int couponViewTypeId) {
        this.couponViewTypeId = couponViewTypeId;
    }

    public int getCouponStates() {
        return couponStates;
    }

    public void setCouponStates(int couponStates) {
        this.couponStates = couponStates;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}