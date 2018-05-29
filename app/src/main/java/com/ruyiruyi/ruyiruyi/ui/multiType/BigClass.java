package com.ruyiruyi.ruyiruyi.ui.multiType;

public class BigClass {

    public String bigClassName;
    public boolean isCheck;

    public BigClass(String bigClassName, boolean isCheck) {
        this.bigClassName = bigClassName;
        this.isCheck = isCheck;
    }

    public String getBigClassName() {
        return bigClassName;
    }

    public void setBigClassName(String bigClassName) {
        this.bigClassName = bigClassName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}