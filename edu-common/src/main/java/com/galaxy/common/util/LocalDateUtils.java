package com.galaxy.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {



    public static LocalDate string2Date(String day){
        LocalDate parse = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return parse;
    }

    public static String date2String(LocalDate date){
         return date.toString();
    }
}
