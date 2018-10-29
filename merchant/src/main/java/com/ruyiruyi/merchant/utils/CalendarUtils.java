package com.ruyiruyi.merchant.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/10/8 11:00
 */


/**
 * 注意: 月份是从0开始(0-11) ， 星期是从星期日开始(1-7) 此工具类内部已处理但输出的calendar对象自行取数据时需注意
 */
public class CalendarUtils {
    Calendar calendar = Calendar.getInstance();


    /**
     * 获取今天或者之后几天的日期是xxxx
     *
     * @param after
     * @return
     */
    public Calendar getAfterDate(int after) {
        calendar.clear();//避免继承当前系统的时间
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + after);

        return calendar;//返回日期
    }

    /**
     * 计算某一个月的天数是xxxx
     *
     * @param year
     * @param month
     * @return
     */
    public int getMonthmaxDay(int year, int month) {
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//默认1月为0月
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return day;
    }

    /**
     * 计算某一天是该年或该月的第xxxx个星期
     *
     * @param year
     * @param month
     * @param day
     */
    public Calendar getWeekNum(int year, int month, int day) {
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar; //Calendar.WEEK_OF_YEAR 或者Calendar.WEEK_OF_MONTH
    }


    /**
     * 计算一年中的第几个星期几是xxxx
     *
     * @param year
     * @param week
     * @param weekday
     * @return
     */
    public Calendar dayNum(int year, int week, int weekday) {
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        if (weekday == 7) {// 周一到周日  2 3 4 5 6 7 1
            weekday = 1;
        }
        calendar.set(Calendar.DAY_OF_WEEK, weekday + 1);

        return calendar;//返回日期
    }

    /**
     * 查询日期的的后几天，前几天
     *
     * @param year
     * @param month
     * @param day
     * @param num
     * @return
     */
    public Calendar getDateAfter(int year, int month, int day, int num) {
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();
        calendar.add(Calendar.DATE, num);

        return calendar;//返回日期
    }


    /**
     * 计算两个任意时间中间相隔的天数
     *
     * @param year1
     * @param month1
     * @param days1
     * @param year2
     * @param month2
     * @param days2
     * @return
     */
    public int getDaysBetween(int year1, int month1, int days1, int year2, int month2, int days2) {

        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        day1.set(year1, month1, days1);
        day2.set(year2, month2, days2);

        if (day1.after(day2)) {
            Calendar swap = day1;
            day1 = day2;
            day2 = swap;
        }
        int days = day2.get(Calendar.DAY_OF_YEAR) - day1.get(Calendar.DAY_OF_YEAR);
        int y2 = day2.get(Calendar.YEAR);
        if (day1.get(Calendar.YEAR) != y2) {
            day1 = (Calendar) day1.clone();
            do {
                days += day1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                day1.add(Calendar.YEAR, 1);
            } while (day1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获取任意一个月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public Calendar getMonthLastDay(int year, int month) {
        calendar.set(year, month, 1);//获得下个月的第一天
        calendar.add(Calendar.DATE, -1);//前移一天

        return calendar;
    }

    /**
     * 几年后的今天(去年今日)
     *
     * @param num
     * @return
     */
    public Calendar getDateBeforeYears(int num) {
        calendar.add(Calendar.YEAR, num);

        return calendar;
    }

}
