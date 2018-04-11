package com.ruyiruyi.merchant.ui.multiType.modle;


public class Dianpu {


    private String dianpu_type;

    private String dianpu_time;

    private String dianpu_money;
    private String dianpu_state;

    public Dianpu(String dianpu_type, String dianpu_time, String dianpu_money, String dianpu_state) {
        this.dianpu_type = dianpu_type;
        this.dianpu_time = dianpu_time;
        this.dianpu_money = dianpu_money;
        this.dianpu_state = dianpu_state;
    }

    public String getDianpu_type() {
        return dianpu_type;
    }

    public String getDianpu_time() {
        return dianpu_time;
    }

    public String getDianpu_money() {
        return dianpu_money;
    }

    public String getDianpu_state() {
        return dianpu_state;
    }

    public void setDianpu_type(String dianpu_type) {
        this.dianpu_type = dianpu_type;
    }

    public void setDianpu_time(String dianpu_time) {
        this.dianpu_time = dianpu_time;
    }

    public void setDianpu_money(String dianpu_money) {
        this.dianpu_money = dianpu_money;
    }

    public void setDianpu_state(String dianpu_state) {
        this.dianpu_state = dianpu_state;
    }
}