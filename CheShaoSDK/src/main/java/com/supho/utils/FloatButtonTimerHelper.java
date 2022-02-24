package com.supho.utils;

/**
 * Created by Khai Tran on 5/26/2017.
 */
import android.content.Context;
import android.util.Log;

import com.supho.model.TimerData;
import com.google.gson.Gson;

public class FloatButtonTimerHelper {
    private static String TAG = "FloatButtonTimerHelper";
    private static boolean isRuningTimer;
    private static boolean isOpenedTimer;
    public static void saveFloatButtonTimer(Context context, String timers){
        Log.d(TAG, "save: " + timers);
        Preference.remove(context, Constants.SHARED_PREF_TIMER);
        Preference.save(context, Constants.SHARED_PREF_TIMER
                , timers);
    }

    public static TimerData getFloatButtonTimerData(Context context){
        String timersVal = Preference.getString(context, Constants.SHARED_PREF_TIMER);
        Log.d("getFloatButtonTimerData","dkm : " + timersVal);
        String str = "{\"listTimerObject\":[]}";
        if(timersVal == null || timersVal.equals("") || timersVal.equals(str) ){
            return null;
        }
        return  new Gson().fromJson(timersVal, TimerData.class);
    }

    public static void enableFloatButtonTimer(Context context, boolean enable) {
        Preference.remove(context, Constants.SHARED_PREF_ENABLE_TIMER);
        Preference.save(context, Constants.SHARED_PREF_ENABLE_TIMER, enable);
    }

    public static boolean isEnableFloatButtonTimer(Context context){
        boolean enable = Preference.getBoolean(context, Constants.SHARED_PREF_ENABLE_TIMER, false);
        Log.d(TAG, "isEnableFloatButtonTimer: " + enable);
        return enable;
    }
    public static void setisRuningTimer(boolean isRuningTimers){
        isRuningTimer = isRuningTimers;
    }
    public static boolean isRuningTimer(){
        return isRuningTimer;
    }

    public static void setcanRunTimer(boolean isOpenedTimers){
        isOpenedTimer = isOpenedTimers;
    }
    public static boolean canRunTimer(){
        return isOpenedTimer;
    }
}
