package com.pachia.balat.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Global {

    private static final String TAG = "Global";
    private static SharedPreferences JJSharePre;
    public static final String SHARE_NAME = "SHARE_NAME";
    public static final String PURCHASE_STATE = "PURCHASE_STATE";
    public static final String CURRENT_ROWS = "CURRENT_ROWS";
    public static final String CURRENT_COLUMNS = "CURRENT_COLUMNS";
    public static final String ASSETS_NAME = "ASSETS_NAME";
    public static final String PHOTO_PATH = "PHOTO_PATH";
    public static final String PHOTO_URI = "PHOTO_URI";
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 4;
    public static final int TAKE_PHOTO = 2;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 6;
    public static final int CHOOSE_PHOTO = 8;

    private static SharedPreferences getJJSharePre(Context context){
        if (JJSharePre == null){
            return JJSharePre = context.getSharedPreferences(SHARE_NAME,Context.MODE_PRIVATE);
        }
        return JJSharePre;
    }

    public static void savePurchaseState(Context context, boolean purchaseState){
        getJJSharePre(context).edit().putBoolean(PURCHASE_STATE,purchaseState).commit();
        Log.d(TAG, "savePurchaseState: "+getPurchaseState(context));
    }

    public static boolean getPurchaseState(Context context){
        return getJJSharePre(context).getBoolean(PURCHASE_STATE,false);
    }

    public static void saveRows(Context context, int rows){
        getJJSharePre(context).edit().putInt(CURRENT_ROWS,rows).commit();
        Log.d(TAG, "saveRows: "+getRows(context));
    }

    public static int getRows(Context context){
        return getJJSharePre(context).getInt(CURRENT_ROWS,3);
    }

    public static void saveColumns(Context context, int column){
        getJJSharePre(context).edit().putInt(CURRENT_COLUMNS,column).commit();
        Log.d(TAG, "saveColumns: "+getColumns(context));
    }

    public static int getColumns(Context context){
        return getJJSharePre(context).getInt(CURRENT_COLUMNS,3);
    }

}
