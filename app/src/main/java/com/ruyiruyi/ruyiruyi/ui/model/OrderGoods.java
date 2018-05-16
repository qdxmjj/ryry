package com.ruyiruyi.ruyiruyi.ui.model;

import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;

import java.util.List;

public class OrderGoods {
    public int userId;
    public int storeId;
    public String storeName;
    public String totalPrice;
    public List<GoodsInfo> goodsInfoList;

    public OrderGoods(int userId, List<GoodsInfo> goodsInfoList) {
        this.userId = userId;
        this.goodsInfoList = goodsInfoList;
    }

    public OrderGoods(int userId, int storeId, String storeName, String totalPrice, List<GoodsInfo> goodsInfoList) {
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.goodsInfoList = goodsInfoList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }
}