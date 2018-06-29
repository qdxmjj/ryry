package com.ruyiruyi.ruyiruyi.ui.model;

import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;

import java.util.List;

public class OrderGoods {
    public int userId;
    public int storeId;
    public String storeName;
    public String totalPrice;
    public List<GoodsInfo> goodsInfoList;
    public int salesId;
    public String actuallyPrice;

    public OrderGoods(int userId, List<GoodsInfo> goodsInfoList) {
        this.userId = userId;
        this.goodsInfoList = goodsInfoList;
    }

    public OrderGoods(int userId, int storeId, String storeName, String totalPrice, List<GoodsInfo> goodsInfoList, int salesId) {
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.goodsInfoList = goodsInfoList;
        this.salesId = salesId;
    }

    public OrderGoods(int userId, int storeId, String storeName, String totalPrice, List<GoodsInfo> goodsInfoList, int salesId, String actuallyPrice) {
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.goodsInfoList = goodsInfoList;
        this.salesId = salesId;
        this.actuallyPrice = actuallyPrice;
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