package com.pachia.comon.tracking;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.pachia.comon.utils.LogUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Firebase {
    public static final String TAG = Firebase.class.getSimpleName();
    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static Firebase newInstance(Application context) {
        return new Firebase(context);
    }

    private Firebase(Application context) {
        this.context = context;
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
    }

    public void firebaseEventTrack(String labelEvent, HashMap<String, Object> params) {
        Bundle bundle = new Bundle();

        if (params != null) {
            Set mapSet = (Set) params.entrySet();
            Iterator mapIterator = mapSet.iterator();
            while (mapIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                String key = (String) (mapEntry.getKey() + "");
                String value = (String) (mapEntry.getValue() + "");
                LogUtils.d(TAG, "key : " + key + " , value:" + value);
                bundle.putString(key, value);
            }

            mFirebaseAnalytics.logEvent(labelEvent, bundle);
        } else {
            mFirebaseAnalytics.logEvent(labelEvent, null);
        }

    }

}
