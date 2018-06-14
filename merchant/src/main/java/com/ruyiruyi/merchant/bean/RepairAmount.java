package com.ruyiruyi.merchant.bean;

public class RepairAmount {
    private String barCode;//轮胎条形码
    private String repairAmount;//修补个数

    public RepairAmount() {
    }

    public RepairAmount(String barCode, String repairAmount) {
        this.barCode = barCode;
        this.repairAmount = repairAmount;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setRepairAmount(String repairAmount) {
        this.repairAmount = repairAmount;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getRepairAmount() {
        return repairAmount;
    }

    @Override
    public String toString() {
        String str = "";
        str = "{" + "\"barCode\"" + ":" + "\"" + barCode + "\"" + "," + "\"repairAmount\"" + ":" + "\"" + repairAmount  + "\""+ "}";

        return str;
    }
}