package com.ruyiruyi.merchant.ui.multiType.modle;


public class Dingdan {
    public Dingdan(String dingdan_type, String car_num, String msg_state) {
        this.dingdan_type = dingdan_type;
        this.car_num = car_num;
        this.msg_state = msg_state;
    }

    private String dingdan_type;

    private String car_num;

    private String msg_state;


    public String getDingdan_type() {
        return dingdan_type;
    }

    public String getCar_num() {
        return car_num;
    }

    public String getMsg_state() {
        return msg_state;
    }


    public void setDingdan_type(String dingdan_type) {
        this.dingdan_type = dingdan_type;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }

    public void setMsg_state(String msg_state) {
        this.msg_state = msg_state;
    }
}