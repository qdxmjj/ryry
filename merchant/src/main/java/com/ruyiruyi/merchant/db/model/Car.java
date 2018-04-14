package com.ruyiruyi.merchant.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "car")
public class Car {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "nick")
    private String nick;

    public Car() {
    }

    public Car(int id, String nick) {
        this.id = id;
        this.nick = nick;
    }
}