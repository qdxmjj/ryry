package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.util.List;

public class Coupon {
    public int couponId;
    public String couponName;
    public int couponType;      //1 服务券（全额）   2现金券  3满减券  4小额券  5抵扣券
    public int couponViewTypeId;    //2精致洗车券  3四轮定位券  7 10元现金券
    public int couponStates;  // 1已使用  2未使用  3已过期
    public String startTime;
    public String endTime;
    public String carNumber;
    public boolean isCanUse;
    public List<String> storeNameList;
    public String goodsName;
    public String moneyFull;        //type = 3 满减 满
    public String moneyMinus;       //type = 3 满减  减
    public String needPay;          //type = 4 还需支付金额
    public String deduction;    //type = 5 可抵扣金额



    public Coupon(int couponId, String couponName, int couponType, int couponViewTypeId, int couponStates, String startTime, String endTime, String carNumber, boolean isCanUse,String goodsName) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponViewTypeId = couponViewTypeId;
        this.couponStates = couponStates;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carNumber = carNumber;
        this.isCanUse = isCanUse;
        this.storeNameList = null;
        this.goodsName = goodsName;
    }
    public Coupon(int couponId, String couponName, int couponType, int couponViewTypeId, int couponStates, String startTime, String endTime, String carNumber, boolean isCanUse,String goodsName, String moneyFull, String moneyMinus, String needPay, String deduction) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponViewTypeId = couponViewTypeId;
        this.couponStates = couponStates;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carNumber = carNumber;
        this.isCanUse = isCanUse;
        this.storeNameList = null;
        this.goodsName = goodsName;
        this.moneyFull = moneyFull;
        this.moneyMinus = moneyMinus;
        this.needPay = needPay;
        this.deduction = deduction;
    }

    public Coupon(int couponId, String couponName, int couponType, int couponViewTypeId, int couponStates, String startTime, String endTime, String carNumber, boolean isCanUse, List<String> storeNameList, String goodsName, String moneyFull, String moneyMinus, String needPay, String deduction) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponViewTypeId = couponViewTypeId;
        this.couponStates = couponStates;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carNumber = carNumber;
        this.isCanUse = isCanUse;
        this.storeNameList = storeNameList;
        this.goodsName = goodsName;
        this.moneyFull = moneyFull;
        this.moneyMinus = moneyMinus;
        this.needPay = needPay;
        this.deduction = deduction;
    }

    public Coupon(int couponId, String couponName, int couponType, int couponViewTypeId, int couponStates, String startTime, String endTime, String carNumber, boolean isCanUse, List<String> storeNameList, String goodsName) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponViewTypeId = couponViewTypeId;
        this.couponStates = couponStates;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carNumber = carNumber;
        this.isCanUse = isCanUse;
        this.storeNameList = storeNameList;
        this.goodsName = goodsName;
    }

    public String getMoneyFull() {
        return moneyFull;
    }

    public void setMoneyFull(String moneyFull) {
        this.moneyFull = moneyFull;
    }

    public String getMoneyMinus() {
        return moneyMinus;
    }

    public void setMoneyMinus(String moneyMinus) {
        this.moneyMinus = moneyMinus;
    }

    public String getNeedPay() {
        return needPay;
    }

    public void setNeedPay(String needPay) {
        this.needPay = needPay;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public boolean isCanUse() {
        return isCanUse;
    }

    public void setCanUse(boolean canUse) {
        isCanUse = canUse;
    }

    public List<String> getStoreNameList() {
        return storeNameList;
    }

    public void setStoreNameList(List<String> storeNameList) {
        this.storeNameList = storeNameList;
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