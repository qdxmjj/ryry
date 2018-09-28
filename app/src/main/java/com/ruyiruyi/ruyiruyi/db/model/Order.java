package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Lenovo on 2018/9/5.
 */
@Table(name = "order")
public class Order {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "ordertype")
    private int orderType;     //订单类型 0轮胎订单  1其他订单


    public Order() {
    }

    public Order(int id, int orderType) {
        this.id = id;
        this.orderType = orderType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
