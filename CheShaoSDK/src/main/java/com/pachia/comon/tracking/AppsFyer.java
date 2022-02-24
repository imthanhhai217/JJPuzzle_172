package com.pachia.comon.tracking;

import android.app.Application;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerTrackingRequestListener;

import java.util.HashMap;
import java.util.Map;

public class AppsFyer {
    public static String TAG = AppsFyer.class.getName();
    private Application context;
    private AppsFlyerConversionListener conversionListener;


    public AppsFyer(Application context, String key, AppsFlyerConversionListener conversionListener) {
        this.context = context;
        this.conversionListener = conversionListener;
        init(key);
    }

    public static AppsFyer newInstance(Application context, String key, AppsFlyerConversionListener conversionListener) {
        return new AppsFyer(context, key, conversionListener);
    }

    public void init(String key) {
        AppsFlyerLib.getInstance().setCollectIMEI(false);
        AppsFlyerLib.getInstance().setCollectAndroidID(false);
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().init(key, conversionListener, context);
        AppsFlyerLib.getInstance().startTracking(context);
    }

    public void trackEventWithCountFailed(String eventName, Map<String, Object> eventValues, AppsFlyerTrackingRequestListener listener) {
        HashMap<String, Object> map = new HashMap<>();
        if (eventValues != null)
            map.putAll(eventValues);
        AppsFlyerLib.getInstance().trackEvent(context, eventName, map, listener);

    }

    public void trackEvent(String eventName, Map<String, Object> eventValues) {
        HashMap<String, Object> map = new HashMap<>();
        if (eventValues != null)
            map.putAll(eventValues);
        AppsFlyerLib.getInstance().trackEvent(context, eventName, map);

    }


}
