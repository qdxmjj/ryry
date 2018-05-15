package com.ruyiruyi.ruyiruyi.ui.multiType;

public class RechargeMoney {
    public int moneyId;
    public String rechargeMoney;
    public String giveMoney;
    public boolean ischeck;
    public int type;

    public RechargeMoney(int moneyId, String rechargeMoney, String giveMoney, boolean ischeck, int type) {
        this.moneyId = moneyId;
        this.rechargeMoney = rechargeMoney;
        this.giveMoney = giveMoney;
        this.ischeck = ischeck;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RechargeMoney() {
    }

    public int getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(int moneyId) {
        this.moneyId = moneyId;
    }

    public String getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(String rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getGiveMoney() {
        return giveMoney;
    }

    public void setGiveMoney(String giveMoney) {
        this.giveMoney = giveMoney;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
}