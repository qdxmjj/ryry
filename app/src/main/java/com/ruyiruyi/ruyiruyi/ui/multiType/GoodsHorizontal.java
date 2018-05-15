package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.Goods;

import java.io.Serializable;

public class GoodsHorizontal extends Goods implements Serializable{

    public int currentCount;

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
}