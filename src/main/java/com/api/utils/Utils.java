package com.api.utils;

import java.math.BigDecimal;

public class Utils {

    private Utils() {
    }

    public static Integer getInt(Object number) {
        if (number instanceof BigDecimal) {
            return ((BigDecimal)number).intValue();
        } else {
            return number instanceof Integer ? (Integer)number : (new BigDecimal((String)number)).intValue();
        }
    }

    public static Double getDouble(Object number) {
        if (number instanceof BigDecimal) {
            return ((BigDecimal)number).doubleValue();
        } else {
            return number instanceof Double ? (Double)number : (new BigDecimal((String)number)).doubleValue();
        }
    }

    public static Long getLong(Object number) {
        if (number instanceof BigDecimal) {
            return ((BigDecimal)number).longValue();
        } else {
            return number instanceof Double ? (Long)number : (new BigDecimal((String)number)).longValue();
        }
    }
}
