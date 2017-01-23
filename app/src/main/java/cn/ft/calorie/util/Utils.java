package cn.ft.calorie.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ft.calorie.event.UserInfoUpdateEvent;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.ui.LoginActivity;

/**
 * Created by TT on 2017/1/16.
 */
public class Utils {
    public static File sysRootDir = Environment.getExternalStorageDirectory();
    public static UserInfo loginUser = new UserInfo();// TODO: 2017/1/17
    static {
        loginUser.setId("587f43493468343120dba9e8");
        loginUser = null;
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
    //登录验证
    public static boolean isLogin(Context context){
        if(loginUser==null)
            context.startActivity(new Intent(context, LoginActivity.class));
        return loginUser!=null;
    }
    //登录后操作
    public static void doOnLogin(UserInfo user){
        Utils.loginUser = user;
        RxBus.getDefault().post(new UserInfoUpdateEvent(true,user));
    }
    //注销后操作
    public static void doOnLogout(){
        Utils.loginUser = null;
        RxBus.getDefault().post(new UserInfoUpdateEvent(true,null));
    }

    //获得版本号
    public static int getVersionCode(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //获得版本名称
    public static String getVersionName(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
