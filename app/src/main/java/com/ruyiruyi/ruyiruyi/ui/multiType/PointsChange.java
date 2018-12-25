package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/12/21 17:57
 */

public class PointsChange {
    private String title;
    private int points;
    private double price;
    private int changeNum;
    private String goodsPic;
    private int goodsId;

    public PointsChange() {
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }
}
