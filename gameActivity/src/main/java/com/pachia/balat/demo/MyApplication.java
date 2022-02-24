package com.pachia.balat.demo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.pachia.comon.game.CheShaoSdk;

public class MyApplication extends Application {

//    private String appKey = "4f1fcd8798dbfe46c4ae66b320fad670"; //SDK
    private String appKey = "8e33aa43380cbc61c55caa2a38e3d684"; //SDK 5
//    private String appKey = "4f1fcd8798dbfe46c4ae66b320fad670";
    private String facebookKey = "334086956714326";

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CheShaoSdk.getInstance().init(this, appKey, facebookKey);

//        FacebookManager.getInstance(this);
        //TODO handle error from Okhttp in OS 4.x
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
