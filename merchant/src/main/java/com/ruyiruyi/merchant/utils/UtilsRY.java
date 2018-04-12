package com.ruyiruyi.merchant.utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UtilsRY {

    public static boolean isMobile(String number){
        String num = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(number)){
            return false;
        }else {
            return number.matches(num);
        }
    }

    public String getTimestampToString(long time) {
        Timestamp ts = new Timestamp(time);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //方法一:优势在于可以灵活的设置字符串的形式。
        String tsStr = sdf.format(ts);
        return tsStr;
    }
    public String getTimestampToStringAll(long time) {
        Timestamp ts = new Timestamp(time);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //方法一:优势在于可以灵活的设置字符串的形式。
        String tsStr = sdf.format(ts);
        return tsStr;
    }

    public Timestamp getStringToTimestamp(String time){
        Timestamp ts = Timestamp.valueOf(time);

        return ts;

    }

}