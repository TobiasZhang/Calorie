package cn.ft.calorie.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.ft.calorie.pojo.UserInfo;
import rx.Subscription;

/**
 * Created by TT on 2017/1/16.
 */
public class Utils {
    public static File sysRootDir = Environment.getExternalStorageDirectory();
    public static UserInfo loginUser = new UserInfo();// TODO: 2017/1/17
    static {
        loginUser.setId("587f43493468343120dba9e8");
    }
    //toast
    public static void toast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    //电话号码验证
    public static boolean isTelValid(String tel, EditText source){
        return true;
    }
    //密码验证
    public static boolean isPasswordValid(String password, EditText source){
        boolean isValid = false;
        if(TextUtils.isEmpty(password)){
            source.setError("密码长度位于6~12位之间");
        }else if(false){
            // TODO: 2017/1/16
        }else{
            isValid = true;
        }
        if(!isValid)
            source.requestFocus();
        return isValid;
    }
}
