package com.pachia.comon.config;

import android.text.TextUtils;

import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.object.UserObj;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.supho.CheShaoSDK;

public class GameConfigs {

    private static GameConfigs gameConfigs;
    private String appKey;
    private String adsKey;
    private SdkConfigObj sdkConfig;
    private boolean show_qc;
    //    private String FCM_APP_API_KEY = "e83661bc7c7f639e5f0334471c8188deb8d5542a";
    private String FCM_APP_API_KEY = "d5f66e393eaab40f72e6b7d3ca5f32f275db5bd5";


    UserObj user;

    public static GameConfigs getInstance() {
        if (gameConfigs == null)
            gameConfigs = new GameConfigs();
        return gameConfigs;
    }

    private GameConfigs() {
    }

    public static void clearInstance() {
        getInstance().setUser(null);
        getInstance().setSdkConfig(null);
    }


    public String getAppKey() {
        if (appKey == null) {
            return PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        }
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
        PrefManager.saveAppKey(CheShaoSdk.getInstance().getApplication(), appKey);
    }

    public String getAdsKey() {
        if (adsKey == null) {
            return PrefManager.getAdsKey(CheShaoSdk.getInstance().getApplication());
        }
        return adsKey;
    }

    public void setAdsKey(String adsKey) {
        this.adsKey = appKey;
        PrefManager.saveAdsKey(CheShaoSdk.getInstance().getApplication(), adsKey);
    }

    public UserObj getUser() {
        if (user == null) {
            return PrefManager.getUser(CheShaoSdk.getInstance().getApplication());
        }
        return user;
    }

    public void setUser(UserObj user) {
        this.user = user;
        PrefManager.saveUser(CheShaoSdk.getInstance().getApplication(), user);
    }


    public SdkConfigObj getSdkConfig() {
        return sdkConfig;
    }

    public void setSdkConfig(SdkConfigObj sdkConfig) {
        if (sdkConfig == null) {
            this.sdkConfig = null;
            return;
        }
        this.sdkConfig = sdkConfig;
        if (sdkConfig.getEx() != null && sdkConfig.getEx().isShowLogo()) {
            CheShaoSDK.getInstance().requestOverlayPermission(CheShaoSDK.activity);
        }
        GameConfigs.getInstance().setShow_qc(true);
        PrefManager.saveBoolean(CheShaoSdk.getInstance().getApplication(),
                com.supho.utils.Constants.SHARED_PREF_DB_ENABLED,
                true);
        PrefManager.saveSetting(CheShaoSdk.getInstance().getApplication(), Constants.SESSION_OUT_TIME, sdkConfig.getSessionOutTime());
    }


    public boolean isLogin() {
        return !TextUtils.isEmpty(PrefManager.getAccessToken(CheShaoSdk.getInstance().getApplication()));
    }

    public void initFacebook(String facebookAppId) {
        FacebookManager.getInstance(CheShaoSdk.getInstance().getApplication()).init(facebookAppId);

    }

    public boolean getShow_qc() {
        return show_qc;
    }

    public void setShow_qc(boolean show_qc) {
        this.show_qc = show_qc;
    }

    public SdkConfigObj.Pop getPopup() {
        if (sdkConfig == null || sdkConfig.getPop() == null) {
            return new SdkConfigObj.Pop();
        }
        return sdkConfig.getPop();
    }

    public String getLang() {
        return DeviceUtils.getLanguage();
    }

    public SdkConfigObj.GoogleIAB getGgIABConfig() {
        return sdkConfig == null ? null : new SdkConfigObj.GoogleIAB();
    }


    public String getFcmApiKey() {
        return FCM_APP_API_KEY;
    }
}
