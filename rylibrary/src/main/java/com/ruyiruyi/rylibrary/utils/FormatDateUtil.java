package com.ruyiruyi.rylibrary.utils;

/*
* 将 int year, int month, int day   转化为 2000-02-02格式
* */
public class FormatDateUtil {
    public String formatDateAll(int year, int month, int day) {
        String dates = "";
        dates = year + "-";
        if (month < 10) {
            dates = dates + "0" + month + "-";
        } else {
            dates = dates + month + "-";
        }
        if (day < 10) {
            dates = dates + "0" + day;
        } else {
            dates = dates + day;
        }

        return dates;
    }

    /*
    * 将 int month ,转化为 02
    * */
    public String formatMonthOrDay(int month) {
        String dates = "";
        if (month < 10) {
            dates = dates + "0" + month;
        } else {
            dates = dates + month;
        }

        return dates;
    }
}