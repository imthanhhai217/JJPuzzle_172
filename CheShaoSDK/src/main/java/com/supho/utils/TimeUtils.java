package com.supho.utils;

/**
 * Created by Khai Tran on 5/26/2017.
 */
import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
public class TimeUtils {
    public static long getMilisecondByTimestamp(long timestampMilisecond){
        return new Date((long)timestampMilisecond * 1000).getTime();
    }
    public static String getTimeIso(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        format.setTimeZone(timeZone);
        String timeIso = format.format(date);
        return timeIso;
    }

    public static long parseTimeStringToMiliSec(String givenDateString, String formatPattern) {
//        String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); HH:mm dd-MM-yyyy
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        try {
            Date mDate = sdf.parse(givenDateString);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
