package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.Goods;

import java.util.List;

public class GoodsItem {
    public int goodsClassId;
    public String goodClassName;
    public Boolean isChooseGood;
    public List<GoodsHorizontal> goodsList;

    public GoodsItem(String goodClassName, Boolean isChooseGood) {
        this.goodClassName = goodClassName;
        this.isChooseGood = isChooseGood;
    }

    public GoodsItem(int goodsClassId, String goodClassName, Boolean isChooseGood, List<GoodsHorizontal> goodsList) {
        this.goodsClassId = goodsClassId;
        this.goodClassName = goodClassName;
        this.isChooseGood = isChooseGood;
        this.goodsList = goodsList;
    }

    public List<GoodsHorizontal> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsHorizontal> goodsList) {
        this.goodsList = goodsList;
    }

    public int getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(int goodsClassId) {
        this.goodsClassId = goodsClassId;
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