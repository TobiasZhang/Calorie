package cn.ft.calorie.util;

import java.text.SimpleDateFormat;
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
}
