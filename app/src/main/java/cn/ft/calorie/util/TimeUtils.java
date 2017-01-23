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
}
