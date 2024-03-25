package com.api.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String date_today(String format) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    public static String date_after_year(String format, int offset) {
        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime newDate = localDate.plusYears((long) offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return newDate.format(formatter);
    }

    public static String date_after_month(String format, int offset) {
        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime newDate = localDate.plusMonths((long) offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return newDate.format(formatter);
    }

    public static String date_after_day(String format, int offset) {
        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime newDate = localDate.plusDays((long) offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return newDate.format(formatter);
    }

    public static String to_timestamp(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date newDate = dateFormat.parse(date);
        SimpleDateFormat standardFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Timestamp ts = Timestamp.valueOf(standardFormat.format(newDate));
        return String.valueOf(ts.getTime() / 1000L);
    }

}
