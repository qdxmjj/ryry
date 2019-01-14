package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * Created by Lenovo on 2019/1/11.
 */
public class ExchangeGoodsOrder {
    public int id;
    public String orderTime;
    public String orderNo;
    public String goodsImage;
    public String goodsName;
    public String goodsPrice;
    public String goodsIntegral;
    public String orderType;   // 1:兑换普通商品订单 2:兑换优惠券订单
    public String orderState;   //1 交易完成  5 待发货 2 待收货

    public ExchangeGoodsOrder(int id, String orderTime, String orderNo, String goodsImage, String goodsName, String goodsPrice, String goodsIntegral, String orderType, String orderState) {
        this.id = id;
        this.orderTime = orderTime;
        this.orderNo = orderNo;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsIntegral = goodsIntegral;
        this.orderType = orderType;
        this.orderState = orderState;
    }

    public ExchangeGoodsOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsIntegral() {
        return goodsIntegral;
    }

    public void setGoodsIntegral(String goodsIntegral) {
        this.goodsIntegral = goodsIntegral;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
}