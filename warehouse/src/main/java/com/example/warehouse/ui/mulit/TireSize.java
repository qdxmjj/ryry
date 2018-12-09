package com.example.warehouse.ui.mulit;

/**
 * Created by Lenovo on 2018/11/20.
 */
public class TireSize {
    public int id;
    public String size;
    public String flgureName;
    public int count;

    public TireSize(int id, String size, String flgureName, int count) {
        this.id = id;
        this.size = size;
        this.flgureName = flgureName;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFlgureName() {
        return flgureName;
    }

    public void setFlgureName(String flgureName) {
        this.flgureName = flgureName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}