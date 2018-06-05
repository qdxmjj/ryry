package com.ruyiruyi.ruyiruyi.ui.multiType;

public class Left {

    public String bigClassName;
    public String classId;
    public int classAmount;
    public boolean isCheck;

    public Left(String bigClassName, boolean isCheck) {
        this.bigClassName = bigClassName;
        this.isCheck = isCheck;
        classAmount = 0;
    }

    public String getClassId() {
        return classId;
    }

    public int getClassAmount() {
        return classAmount;
    }

    public void setClassAmount(int classAmount) {
        this.classAmount = classAmount;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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