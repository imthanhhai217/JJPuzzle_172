package com.pachia.comon.utils;

import android.util.Log;


public class LogUtils {
    private static boolean DEBUG = true;
//    private static boolean DEBUG = BuildConfig.DEBUG;

    public static void e(String tag, String message) {
        if (DEBUG)
            Log.e(tag, message);
    }

    public static void i(String tag, String message) {
        if (DEBUG)
            Log.i(tag, message);
    }

    public static void d(String tag, String message) {
        if (DEBUG)
            Log.d(tag, message);
    }

    public static void v(String tag, String message) {
        if (DEBUG)
            Log.v(tag, message);
    }

    public static void w(String tag, String message) {
        if (DEBUG)
            Log.w(tag, message);
    }

    public static void printStackTrace(Exception e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }
}
