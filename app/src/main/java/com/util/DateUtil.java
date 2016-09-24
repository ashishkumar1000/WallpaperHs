package com.util;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * @return absolute rounded off difference in days
     */
    public static int getRoundedDifferenceInDays(long timeOne, long timeTwo) {
        long Millis24Hrs = 24 * 60 * 60 * 1000;
        double difference = timeOne - timeTwo;
        if (difference < 0) {
            difference *= -1;
        }
        return (int) ((difference / Millis24Hrs) + 0.5);
    }

    /**
     * Returns Current Date in string.
     *
     * @return current Date.
     */
    public static String getCurrentDate() {
        Format formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(new Date());
    }
}