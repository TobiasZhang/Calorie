package cn.ft.calorie.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by TT on 2017/1/17.
 */
public class FragmentUtils {
    public static void addFragment(FragmentActivity activity, int container, Fragment fragment, Bundle args,boolean addToBackStack,String tag){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        ft.add(container,fragment,tag);
        if(addToBackStack)
            ft.addToBackStack(null);
        //// TODO: 2017/1/17 animation
        ft.commit();
    }
    public static void addFragment(FragmentActivity activity, int container, Fragment fragment){
        addFragment(activity,container,fragment,null,false,null);
    }
    public static void addFragment(FragmentActivity activity, int container, Fragment fragment,boolean addToBackStack,String tag){
        addFragment(activity,container,fragment,null,addToBackStack,tag);
    }
}
