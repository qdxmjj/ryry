package com.ruyiruyi.merchant.ui.multiType.modle;

public class PromotionHasperson {
    private String userId;
    private String userName;
    private String userPhone;
    private String userState;
    private String userTime;
    private boolean isFirst;

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getUserTime() {

        return userTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserState() {
        return userState;
    }


    public PromotionHasperson() {

    }
}