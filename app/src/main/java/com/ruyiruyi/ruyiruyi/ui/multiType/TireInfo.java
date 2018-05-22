package com.ruyiruyi.ruyiruyi.ui.multiType;

public class TireInfo {
    public String tireImage;
    public String tireName;
    public int tireCount;
    public String tirePrice;
    public String fontRearFlag;  //0前后轮 1前轮 2后轮

    public TireInfo(String tireImage, String tireName, int tireCount, String tirePrice, String fontRearFlag) {
        this.tireImage = tireImage;
        this.tireName = tireName;
        this.tireCount = tireCount;
        this.tirePrice = tirePrice;
        this.fontRearFlag = fontRearFlag;
    }



    public String getFontRearFlag() {
        return fontRearFlag;
    }

    public void setFontRearFlag(String fontRearFlag) {
        this.fontRearFlag = fontRearFlag;
    }

    public String getTireImage() {
        return tireImage;
    }

    public void setTireImage(String tireImage) {
        this.tireImage = tireImage;
    }

    public String getTireName() {
        return tireName;
    }

    public void setTireName(String tireName) {
        this.tireName = tireName;
    }

    public int getTireCount() {
        return tireCount;
    }

    public void setTireCount(int tireCount) {
        this.tireCount = tireCount;
    }

    public String getTirePrice() {
        return tirePrice;
    }

    public void setTirePrice(String tirePrice) {
        this.tirePrice = tirePrice;
    }


}