package com.pachia.comon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by dungnv
 */
public class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    /**
     * Check internet connecttion
     *
     * @param context
     * @return
     */
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                LogUtils.d(TAG, "activeNetwork TYPE_WIFI");
                return TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                LogUtils.d(TAG, "activeNetwork TYPE_MOBILE");
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }


    /**
     * Check internet connection
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        boolean isCon = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isCon = true;
            }
        }
        return isCon;
    }


    /**
     * Check internet connection
     *
     * @param activity
     * @return
     */
    public static boolean checkNetwork(final Activity activity) {
        boolean isCon = isConnect(activity);
        if (!isCon) {
            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (activity != null && !activity.isFinishing()) {
                        activity.startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                        activity.finish();
                    }
                }
            };
        }
        return isCon;
    }


}
