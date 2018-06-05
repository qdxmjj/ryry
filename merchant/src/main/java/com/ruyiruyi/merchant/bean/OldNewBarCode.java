package com.ruyiruyi.merchant.bean;

public class OldNewBarCode {

    private String oldBarCode;
    private String newBarCode;

    public OldNewBarCode(String oldBarCode, String newBarCode) {
        this.oldBarCode = oldBarCode;
        this.newBarCode = newBarCode;
    }

    public OldNewBarCode() {
    }

    public void setOldBarCode(String oldBarCode) {
        this.oldBarCode = oldBarCode;
    }

    public void setNewBarCode(String newBarCode) {
        this.newBarCode = newBarCode;
    }

    public String getOldBarCode() {
        return oldBarCode;
    }

    public String getNewBarCode() {
        return newBarCode;
    }


    @Override
    public String toString() {
        String str = "";
        str = "{" + "\"oldBarCode\"" + ":" + "\"" + oldBarCode + "\"" + "," + "\"newBarCode\"" + ":" + "\"" + newBarCode + "\"" + "}";

        return str;
    }
}