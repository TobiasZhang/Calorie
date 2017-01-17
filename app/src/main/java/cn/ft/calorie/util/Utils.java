package cn.ft.calorie.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TT on 2017/1/16.
 */
public class Utils {
    public static void toast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
