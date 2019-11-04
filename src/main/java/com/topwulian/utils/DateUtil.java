package com.topwulian.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: szz
 * @Date: 2019/1/7 下午11:10
 * @Version 1.0
 */
public class DateUtil {

    /**
     * 得到当前时间前面的任意小时
     * @param ihour
     * @return
     */
    public static Date getBeforeByHourTime(int ihour){
        String returnstr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return calendar.getTime();
    }
    /**
     * 得到当前时间前面的任意月
     * @param imonth
     * @return
     */
    public static Date getBeforeByMonth(int imonth){
        String returnstr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - imonth);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return calendar.getTime();
    }

    /**
     * 日期转换为字符串
     * @param date
     */
    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        System.out.println(getBeforeByMonth(1));

    }
}
