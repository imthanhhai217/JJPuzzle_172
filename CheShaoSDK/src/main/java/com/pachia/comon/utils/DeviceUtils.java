package com.pachia.comon.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.appsflyer.AppsFlyerLib;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.sharePref.PrefManager;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.UUID;

public final class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();

    public static boolean isTablet(Context context) {
        try {
            boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
            boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
            return (xlarge || large);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasHardwareButtons() {
        try {
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
            boolean hasHardwareButtons = hasBackKey && hasHomeKey;
            Log.d(TAG, "hasHardwareButtons:" + hasHardwareButtons + " , hasBackKey: " + hasBackKey + " , " + hasHomeKey);
            return hasHardwareButtons;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidthInPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightInPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    @SuppressLint("NewApi")
    public static int getRealScreenWidthInPixels(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size.x;
    }


    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    @SuppressLint("NewApi")
    public static int getRealScreenHeightInPixels(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size.y;
    }

    public static int getStatusBarHeightInPixels(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeightInPixels(Context context) {
        return getRealScreenHeightInPixels(context)
                - getScreenHeightInPixels(context);
    }

    public static int getScreenOrientation(Context context) {
        // Query what the orientation currently really is.
        try {
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                return 1; // Portrait Mode

            } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return 2; // Landscape mode
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressLint("NewApi")
    public static boolean isNavigationBarOnBottom(Context context) {
        Resources res = context.getResources();
        Configuration cfg = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        boolean canMove = (dm.widthPixels != dm.heightPixels && cfg.smallestScreenWidthDp < 600);

        return (!canMove || dm.widthPixels < dm.heightPixels);
    }

    public static String getResolution(Context c) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            String wh = displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;

            /*
             * IOS wh = "1136x300"; wh = "960x250"; wh = "1536x650"; wh =
             * "2048x500"; wh = "768x325"; wh = "1024x250"; wh = "1334x350";
             * wh = "750x530"; wh = "1920x520"; wh = "1080x750";
             */

            /* Android */
            // Phone
            // wh = "320x480";
            // wh = "480x800";
            // wh = "480x854";
            // wh = "540x960";
            // wh = "1280x720";
            // wh = "1920x1080";
            // tablet
            // wh = "1280x800";
            // wh = "1024x600";

            return wh == null ? "" : wh;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getOSInfo() {
        return Build.MANUFACTURER + " " + Build.PRODUCT + " " + Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT;
    }

    public static String getDevice() {
        return Build.MODEL == null ? "" : Build.MODEL;
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String getUniqueID(Context context) {
        if (uniqueID == null) {
            uniqueID = PrefManager.getString(context, PREF_UNIQUE_ID);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                PrefManager.saveString(context, PREF_UNIQUE_ID, uniqueID);
            }
        }
        return uniqueID;
    }

    public static String getAndroidID(Context c) {
        return Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getUniqueDeviceID(Context c) {
        String id = getAndroidID(c);
        if (TextUtils.isEmpty(id)) {
            id = getUniqueID(c);
        }
        Log.i(TAG, "getUniqueDeviceID: " + id);
        return Utils.getSHACheckSum(id);
    }

    public static String getAppsflyerUID(final Context context) {
        try {
            String appslyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(context);
            return appslyerId;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String tmpAdvertisingID;

    public static String getAdvertisingID(final Context context) {
        try {
            if (TextUtils.isEmpty(tmpAdvertisingID) || tmpAdvertisingID == null) {
                tmpAdvertisingID = PrefManager.getString(context, "advertisingId", "");
                return "" + tmpAdvertisingID;
            } else {
                return "" + tmpAdvertisingID;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static void initAdvertising(Activity activity, Listener listener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity);
                    String adId = adInfo.getId();
                    Log.i("Google Advertising id:", "Advertising ID :" + adId);
                    tmpAdvertisingID = adId;
                    PrefManager.saveString(activity, "advertisingId", adId);
                    listener.onGetAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static String getLanguage() {
        return PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.APP_LANG, "en");
    }

    public static void setLocale(Activity ct, String lang) {
        PrefManager.saveSetting(ct, Constants.APP_LANG, lang);
        Locale myLocale = new Locale(lang);
        Resources res = ct.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
    }

    public static Locale getLocale() {
        String lang = getLanguage();
        return new Locale(lang);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static interface Listener {
        void onGetAds();
    }
}
