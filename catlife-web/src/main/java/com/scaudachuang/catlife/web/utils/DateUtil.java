package com.scaudachuang.catlife.web.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hiluyx
 * @since 2021/10/20 15:51
 **/
public class DateUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long oneDayMills = 24L * 60 * 60 * 1000;

    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    public static Date alignment(Date date) {
        long time = date.getTime();
        time -= time % oneDayMills;
        return new Date(time - 8 * 60 * 60 * 1000);
    }

    public static Date addDays(Date date, int day) {
        return asDays(date, day);
    }

    protected static Date asDays(Date date, int day) {
        long time = date.getTime();
        time +=  day * oneDayMills;
        return new Date(time);
    }

    public static Date subDays(Date date, int day) {
        return asDays(date, - day);
    }

    public static void main(String[] args) throws ParseException {
        Date parse = sdf.parse("2020-10-21 11:22:56");
        System.out.println(subDays(alignment(parse), 10));
    }
}
