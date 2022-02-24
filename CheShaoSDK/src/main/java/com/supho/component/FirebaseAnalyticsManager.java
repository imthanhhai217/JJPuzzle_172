package com.supho.component;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by khaitran on 3/1/18.
 */

public class FirebaseAnalyticsManager {

    private static final String TAG = FirebaseAnalyticsManager.class.getSimpleName();
    private static FirebaseAnalyticsManager INSTANCE;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static FirebaseAnalyticsManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FirebaseAnalyticsManager();
        }
        return INSTANCE;
    }

    public void firebaseEventTrack(Context context, String labelEvent , HashMap<String , Object> params){
        Bundle bundle = new Bundle();
        if(mFirebaseAnalytics == null){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        if(params != null){
            Set mapSet = (Set) params.entrySet();
            Iterator mapIterator = mapSet.iterator();
            while (mapIterator.hasNext()){
                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                String key = (String) (mapEntry.getKey() + "");
                String value = (String) (mapEntry.getValue()+ "");
                Log.d(TAG , "key : " + key + " , value:" + value);
                bundle.putString(key , value);
            }

            mFirebaseAnalytics.logEvent(labelEvent , bundle);
        }else{
            mFirebaseAnalytics.logEvent(labelEvent , null);
        }

    }
}