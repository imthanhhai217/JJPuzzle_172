package com.pachia.comon.config;

import android.text.TextUtils;
import android.util.Log;

import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.AuthenConfigObj;
import com.pachia.comon.sharePref.PrefManager;

public class AuthenConfigs {
    private static AuthenConfigs authenConfigs;

    AuthenConfigObj authenConfigObj;

    private String accessToken;

    public static AuthenConfigs getInstance() {
        if (authenConfigs == null)
            authenConfigs = new AuthenConfigs();
        return authenConfigs;
    }

    private AuthenConfigs() {
    }

    public AuthenConfigObj getAuthenConfigObj() {
        return authenConfigObj;
    }

    public void setAuthenConfigObj(AuthenConfigObj authenConfigObj) {
        this.authenConfigObj = authenConfigObj;
    }

    public static AuthenConfigs getAuthenConfigs() {
        return authenConfigs;
    }

    public static void setAuthenConfigs(AuthenConfigs authenConfigs) {
        AuthenConfigs.authenConfigs = authenConfigs;
    }

    public String getAccessToken() {
        if (accessToken == null) {
            accessToken = PrefManager.getAccessToken(CheShaoSdk.getInstance().getApplication());
        }
        return TextUtils.isEmpty(accessToken) ? "" : accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        PrefManager.saveAccessToken(CheShaoSdk.getInstance().getApplication(), accessToken);
        Log.d("Access token", accessToken);
    }


}
