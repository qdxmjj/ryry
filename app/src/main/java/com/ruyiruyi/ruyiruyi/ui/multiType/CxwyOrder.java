package com.ruyiruyi.ruyiruyi.ui.multiType;

public class CxwyOrder {
    public int cxwyCount;
    public String cxwyPrice;

    public CxwyOrder(int cxwyCount, String cxwyPrice) {
        this.cxwyCount = cxwyCount;
        this.cxwyPrice = cxwyPrice;
    }

    public CxwyOrder() {
    }

    public int getCxwyCount() {
        return cxwyCount;
    }

    public void setCxwyCount(int cxwyCount) {
        this.cxwyCount = cxwyCount;
    }

    public String getCxwyPrice() {
        return cxwyPrice;
    }

    public void setCxwyPrice(String cxwyPrice) {
        this.cxwyPrice = cxwyPrice;
    }
}