package com.ruyiruyi.ruyiruyi.ui.multiType;

public class TireFigure {
    public boolean isCheck ;
    public int tuijian;
    public String titleStr;
    public String oneImage;
    public String twoImage;
    public String threeImage;
    public String contentStr;

    public TireFigure() {
    }

    public TireFigure(boolean isCheck,int tuijian, String titleStr, String oneImage, String twoImage, String threeImage, String contentStr) {
        this.isCheck = isCheck;
        this.tuijian = tuijian;
        this.titleStr = titleStr;
        this.oneImage = oneImage;
        this.twoImage = twoImage;
        this.threeImage = threeImage;
        this.contentStr = contentStr;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getOneImage() {
        return oneImage;
    }

    public void setOneImage(String oneImage) {
        this.oneImage = oneImage;
    }

    public String getTwoImage() {
        return twoImage;
    }

    public void setTwoImage(String twoImage) {
        this.twoImage = twoImage;
    }

    public String getThreeImage() {
        return threeImage;
    }

    public void setThreeImage(String threeImage) {
        this.threeImage = threeImage;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }
}