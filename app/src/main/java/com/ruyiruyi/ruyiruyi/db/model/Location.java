package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "location")
public class Location {
    @Column(name = "id", isId = true, autoGen = false)
    private int id;

    @Column(name = "city")
    private String city;

    @Column(name = "jingdu")
    private Double jingdu;

    @Column(name = "weidu")
    private Double weidu;

    @Column(name = "shi")
    private String shi;

    @Column(name = "qu")
    private String qu;


    public Location() {
    }

    public Location(int id, String shi, String qu) {
        this.id = id;
        this.shi = shi;
        this.qu = qu;
    }

    public Location(int id, String city, Double jingdu, Double weidu) {
        this.id = id;
        this.city = city;
        this.jingdu = jingdu;
        this.weidu = weidu;
    }

    public Location(int id, String city, Double jingdu, Double weidu, String shi, String qu) {
        this.id = id;
        this.city = city;
        this.jingdu = jingdu;
        this.weidu = weidu;
        this.shi = shi;
        this.qu = qu;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getJingdu() {
        return jingdu;
    }

    public void setJingdu(Double jingdu) {
        this.jingdu = jingdu;
    }

    public Double getWeidu() {
        return weidu;
    }

    public void setWeidu(Double weidu) {
        this.weidu = weidu;
    }
}