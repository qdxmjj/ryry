package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.util.List;

public class CountOne {
    public int cxwyCount;
    public int currentCxwyCount;
    public List<Double> priceList;
    public Double allPrice;


    public CountOne(int cxwyCount, int currentCxwyCount, List<Double> priceList, Double allPrice) {
        this.cxwyCount = cxwyCount;
        this.currentCxwyCount = currentCxwyCount;
        this.priceList = priceList;
        this.allPrice = allPrice;
    }

    public Double getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(Double allPrice) {
        this.allPrice = allPrice;
    }

    public List<Double> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Double> priceList) {
        this.priceList = priceList;
    }

    public int getCurrentCxwyCount() {
        return currentCxwyCount;
    }

    public void setCurrentCxwyCount(int currentCxwyCount) {
        this.currentCxwyCount = currentCxwyCount;
    }

    public int getCxwyCount() {
        return cxwyCount;
    }

    public void setCxwyCount(int cxwyCount) {
        this.cxwyCount = cxwyCount;
    }
}