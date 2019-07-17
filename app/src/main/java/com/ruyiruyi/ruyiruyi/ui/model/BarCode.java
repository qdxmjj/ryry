package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by 86135 on 2019/5/28.
 */

public class BarCode {
    public String barCode;
    public String usedDays;

    public BarCode(String barCode, String usedDays) {
        this.barCode = barCode;
        this.usedDays = usedDays;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(String usedDays) {
        this.usedDays = usedDays;
    }

}
