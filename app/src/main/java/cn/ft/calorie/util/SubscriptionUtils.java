package cn.ft.calorie.util;

import android.app.Activity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rx.Subscription;

/**
 * Created by TT on 2017/1/18.
 */
public class SubscriptionUtils {
    public static Map<Class,Set<Subscription>> subscriptionSet = new HashMap<>();
    public static void register(Activity activity, Subscription subscription){
        if(!subscriptionSet.containsKey(activity.getClass())){
            subscriptionSet.put(activity.getClass(),new HashSet<>());
        }
        subscriptionSet.get(activity.getClass()).add(subscription);
    }
    public static void unregister(Activity activity){
        Set<Subscription> targetSet = subscriptionSet.get(activity.getClass());
        if(targetSet==null||targetSet.size()==0)
            return;
        for(Subscription s:targetSet){
            if(!s.isUnsubscribed()) {
                s.unsubscribe();
            }
        }
        targetSet.clear();
    }
}
