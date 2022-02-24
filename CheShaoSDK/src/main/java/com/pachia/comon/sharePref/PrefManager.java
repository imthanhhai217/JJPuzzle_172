package com.pachia.comon.sharePref;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.pachia.comon.object.ListPurchaseHistoryObj;
import com.pachia.comon.object.PurchaseHistoryObj;
import com.pachia.comon.object.UserObj;
import com.pachia.comon.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Set;


/**
 * Manage App settings stored in {@link SharedPreferences}
 * Created by DungNV.
 */
public class PrefManager {

    private static String PREF_FILE_NAME = "vParentalControl";

    //    public static String URL="http://bbj-dev-lb-1496414834.ap-northeast-1.elb.amazonaws.com/";
    //

    /**
     * Logged in when has Access token saved in Preference
     *
     * @return
     */
    public static boolean isLoggedIn(Context context) {
        return getAccessToken(context) != null;
    }

    /**
     * Save user as json data
     */
    public static void saveUser(Context context, UserObj customerObject) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String userJson = new Gson().toJson(customerObject);
        pref.edit().putString(Constants.PrefKey.PREF_USER_JSON, userJson).commit();
    }

    public static void saveUsePurchaseHistory(Context context, String account_id, PurchaseHistoryObj obj) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        ListPurchaseHistoryObj list = getHistoryPurchase(context, account_id);
        list.getData().add(obj);
        String userJson = new Gson().toJson(list);
        pref.edit().putString(Constants.PURCHASE + account_id, userJson).commit();
    }

    public static void saveUsePurchaseHistory(Context context, String account_id, ListPurchaseHistoryObj obj) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String userJson = new Gson().toJson(obj);
        pref.edit().putString(Constants.PURCHASE + account_id, userJson).commit();
    }

    public static ListPurchaseHistoryObj getHistoryPurchase(Context context, String account_id) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String userJson = pref.getString(Constants.PURCHASE + account_id, null);
        ListPurchaseHistoryObj list = null;
        if (userJson == null || userJson.isEmpty()) {
            list = new ListPurchaseHistoryObj();
            list.setData(new ArrayList<>());
            return list;
        }
        try {
            list = new Gson().fromJson(userJson, ListPurchaseHistoryObj.class);
            if (list == null)
                list = new ListPurchaseHistoryObj();
            if (list.getData() == null)
                list.setData(new ArrayList<>());
            return list;
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }

        return list;
    }


    public static UserObj getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String userJson = pref.getString(Constants.PrefKey.PREF_USER_JSON, null);
        if (userJson == null || userJson.isEmpty()) {
            return null;
        }

        try {
            UserObj user = new Gson().fromJson(userJson, UserObj.class);
            return user;
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Save Access token to {@link SharedPreferences}
     *
     * @param context     Context provide SharePreference
     * @param accessToken to be saved
     */
    public static void saveAccessToken(Context context, String accessToken) {
        saveSetting(context, Constants.PrefKey.PREF_TOKEN, accessToken);
    }

    public static String getAccessToken(Context context) {
        if (context == null)
            return "";
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
                .getString(Constants.PrefKey.PREF_TOKEN, "");
    }
    public static void saveAdsKey(Context context, String adsKey){
        if (context == null)
            return;
        saveSetting(context, Constants.PrefKey.ADS_KEY, adsKey);
    }

    public static String getAdsKey(@NonNull Context context) {
        if (context == null)
            return "";
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
                .getString(Constants.PrefKey.ADS_KEY, "");
    }
    public static void saveAppKey(Context context, String appKey) {
        if (context == null)
            return;
        saveSetting(context, Constants.PrefKey.APP_KEY, appKey);
    }

    public static String getAppKey(@NonNull Context context) {
        if (context == null)
            return "";
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
                .getString(Constants.PrefKey.APP_KEY, "");
    }

    public static void saveSetting(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveSetting(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveSetting(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveSetting(Context context, String key, float value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void saveSetting(Context context, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void saveSetting(Context context, String key, Set<String> value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }


    public static void saveString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveLong(Context context, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * ============== get method =====================
     */
    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue) == null ? "" : pref.getString(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
            return pref.getLong(key, defaultValue);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }

    public static void logoutApp(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constants.PrefKey.PREF_USER_JSON);
//        editor.clear();
        editor.commit();
    }


    public static void saveLastPurchaseGGSuccess(Context context, String account_id, String PurchaseToken) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        pref.edit().putString(Constants.LAST_PURCHASE + account_id, PurchaseToken).commit();
    }

    public static String getLastPurchaseGGSuccess(Context context, String account_id) {
        if (context == null)
            return "";
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
                .getString(Constants.LAST_PURCHASE + account_id, "");
    }
}
