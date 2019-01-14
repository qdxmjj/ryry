package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.io.Serializable;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2019/1/7 9:12
 */

public class MyAddress implements Serializable{
    private int id;
    private String name;
    private String phone;
    private String sheng;
    private String shi;
    private String qu;
    private String address;
    private int isDefault; //0 非默认  1 默认

    public MyAddress() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
