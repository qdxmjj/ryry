package com.ruyiruyi.merchant.bean;

public class StorePingJiaBean {
    private int pingjia_id;
    private int user_id;
    private String user_img_url;
    private String pingjia_pica_url;
    private String pingjia_picb_url;
    private String pingjia_picc_url;
    private String user_name;
    private String star;     //0 1 2 3 4 5
    private String pj_time;
    private String pj_txt;

    public StorePingJiaBean() {
    }

    public void setPingjia_pica_url(String pingjia_pica_url) {
        this.pingjia_pica_url = pingjia_pica_url;
    }

    public void setPingjia_picb_url(String pingjia_picb_url) {
        this.pingjia_picb_url = pingjia_picb_url;
    }

    public void setPingjia_picc_url(String pingjia_picc_url) {
        this.pingjia_picc_url = pingjia_picc_url;
    }

    public String getPingjia_pica_url() {
        return pingjia_pica_url;
    }

    public String getPingjia_picb_url() {
        return pingjia_picb_url;
    }

    public String getPingjia_picc_url() {
        return pingjia_picc_url;
    }

    public void setPingjia_id(int pingjia_id) {
        this.pingjia_id = pingjia_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_img_url(String user_img_url) {
        this.user_img_url = user_img_url;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setPj_time(String pj_time) {
        this.pj_time = pj_time;
    }

    public void setPj_txt(String pj_txt) {
        this.pj_txt = pj_txt;
    }

    public int getPingjia_id() {
        return pingjia_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_img_url() {
        return user_img_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getStar() {
        return star;
    }

    public String getPj_time() {
        return pj_time;
    }

    public String getPj_txt() {
        return pj_txt;
    }
}