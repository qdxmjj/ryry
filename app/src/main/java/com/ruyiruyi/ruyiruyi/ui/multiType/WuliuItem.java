package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * Created by Lenovo on 2019/1/11.
 */
public class WuliuItem {
    public String time;
    public String wuliuStr;
    public boolean hasShangLine;
    public boolean hasXiaLan;

    public boolean isHasShangLine() {
        return hasShangLine;
    }

    public void setHasShangLine(boolean hasShangLine) {
        this.hasShangLine = hasShangLine;
    }

    public boolean isHasXiaLan() {
        return hasXiaLan;
    }

    public void setHasXiaLan(boolean hasXiaLan) {
        this.hasXiaLan = hasXiaLan;
    }

    public WuliuItem(String time, String wuliuStr, boolean hasShangLine, boolean hasXiaLan) {

        this.time = time;
        this.wuliuStr = wuliuStr;
        this.hasShangLine = hasShangLine;
        this.hasXiaLan = hasXiaLan;
    }

    public WuliuItem(String time, String wuliuStr) {
        this.time = time;
        this.wuliuStr = wuliuStr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWuliuStr() {
        return wuliuStr;
    }

    public void setWuliuStr(String wuliuStr) {
        this.wuliuStr = wuliuStr;
    }
}