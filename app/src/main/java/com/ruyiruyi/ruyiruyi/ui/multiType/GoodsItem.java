package com.ruyiruyi.ruyiruyi.ui.multiType;

public class GoodsItem {
    public String goodClassName;
    public Boolean isChooseGood;

    public GoodsItem(String goodClassName, Boolean isChooseGood) {
        this.goodClassName = goodClassName;
        this.isChooseGood = isChooseGood;
    }

    public String getGoodClassName() {
        return goodClassName;
    }

    public void setGoodClassName(String goodClassName) {
        this.goodClassName = goodClassName;
    }

    public Boolean getChooseGood() {
        return isChooseGood;
    }

    public void setChooseGood(Boolean chooseGood) {
        isChooseGood = chooseGood;
    }
}