package com.galaxy.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeUtils<pattern> {
    final static String format = "yyyy-MM-dd HH:mm:ss";

    //获取当前时间的指定格式
    public static String formatNow() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern(pattern));
        return format;
    }

    public static LocalDateTime string2parse(String time,String pattern) {
        LocalDateTime result = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
        return result;
    }

    /**
     * 自己指定格式
     * @param pattern
     * @return
     */
    public static String formatOther(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));

    }
    public static String format2string(LocalDateTime localDateTime){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String format = localDateTime.format(DateTimeFormatter.ofPattern(pattern));
        return format;
    }


    /**
     * 开始时间
     */
    public static LocalDateTime getStartTime(String day){
        day += " 00:00:00";
       return  string2parse(day, format);
    }
    /**
     * 结束时间
     */

    public static LocalDateTime getEndTime(String day){
        day += " 23:59:59";
        return string2parse(day, format);
    }

}
