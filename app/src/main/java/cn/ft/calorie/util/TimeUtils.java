package cn.ft.calorie.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TT on 2017/1/22.
 */
public class TimeUtils {
    public static SimpleDateFormat sdf = new SimpleDateFormat();
    public static String getFormatDate(Date date, String pattern){
        sdf.applyPattern(pattern);
        return sdf.format(date);
    }

    public static String getTodayGMTString(int dayOffset){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DATE,dayOffset);
        return calendar.getTime().toGMTString();
    }
    public static String getTodayGMTString(){
        return getTodayGMTString(0);
    }


    //秒转换时间字符串00：00：00
    public static String formatSeconds(int var) {
        String secondStr;
        String minuteStr;
        String hourStr;
        int second = 0;
        int minute = 0;
        int hour = 0;
        second = var % 60;
        if (var / 60 > 0) {
            minute = var / 60 % 60;
            hour = var / 60 / 60;
        }
        secondStr = second + "";
        minuteStr = minute + "";
        hourStr = hour + "";
        if (second < 10)
            secondStr = "0" + secondStr;
        if (minute < 10)
            minuteStr = "0" + minuteStr;
        if (hour < 10)
            hourStr = "0" + hourStr;

        return hourStr + ":" + minuteStr + ":" + secondStr;
    }
}
