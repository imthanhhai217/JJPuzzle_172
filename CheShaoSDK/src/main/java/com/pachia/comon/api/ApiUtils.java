package com.pachia.comon.api;


import android.content.Context;
import android.text.TextUtils;

import com.pachia.comon.api.request.GameRequest;
import com.pachia.comon.api.request.LoginRequest;
import com.pachia.comon.api.request.NotificationRequest;
import com.pachia.comon.api.request.PaymentRequest;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Retrofit;

/**
 * Created by dungnv
 */

public class ApiUtils {

    public static LoginRequest getLoginRequest() {
        if (RetrofitClient.getInstance() == null)
            return null;
        return RetrofitClient.getInstance().create(LoginRequest.class);
    }

    public static GameRequest getGameRequest() {
        if (RetrofitClient.getInstance() == null)
            return null;
        return RetrofitClient.getInstance().create(GameRequest.class);
    }

    public static NotificationRequest getNotificationRequest() {
        Retrofit rft = RetrofitClient.getInteractInstance(ConstantApi.URL_BASE_INTERACT);
        if (rft == null)
            return null;
        return rft.create(NotificationRequest.class);
    }

    public static PaymentRequest getPaymentRequest() {
        Retrofit rft = RetrofitClient.getPaymentInstance(ConstantApi.URL_BASE_PAYTECH);
        if (rft == null)
            return null;
        return rft.create(PaymentRequest.class);
    }

    public static String getUrlWvRegister(Context ct) {
        String appKey = PrefManager.getAppKey(ct);
        String appVersion = Utils.getGameVersion(ct);
        String url = PrefManager.getString(ct, ConstantApi.APP_UR, ConstantApi.BASE_URL) + ConstantApi.URL_WEBVIEW_REGISTER;
        url += "?sdk_version=" + Utils.stringNormalize(Utils.getSDKVersion(ct));
        url += "&device_network=" + Utils.stringNormalize(Utils.getNetwork(ct));
        url += "&appsflyer_id=" + DeviceUtils.getAppsflyerUID(ct);
        url += "&advertising_id=" + DeviceUtils.getAdvertisingID(ct);
        url += "&device_os=" + Utils.stringNormalize(DeviceUtils.getOSInfo());
        url += "&device_name=" + Utils.stringNormalize(DeviceUtils.getDevice());
        return url.replace("{app_key}", appKey).replace("{app_version}", appVersion);
    }

    public static String getUrlWvPayment(Context ct, String state) {
        VerifyPurchaseRequestObj obj = new VerifyPurchaseRequestObj();
        obj.setMethod("2");

        HashMap<String, String> params = new HashMap<String, String>();
        Set<Map.Entry<String, String>> set = params.entrySet();

        params.put("x_request", GameConfigs.getInstance().getAppKey());
        params.put("device_id", DeviceUtils.getUniqueDeviceID(ct));
        params.put("method", "2");
        params.put("character_id", PrefManager.getString(ct, Constants.ROLE_ID, ""));
        params.put("area_id", PrefManager.getString(ct, Constants.AREA_ID, ""));
        params.put("app_version", Utils.getGameVersion(ct));
        params.put("sdk_version", Utils.getSDKVersion(ct));
        params.put("device_name", DeviceUtils.getDevice());
        params.put("device_os", DeviceUtils.getOSInfo());
        params.put("resolution", DeviceUtils.getResolution(ct));
        params.put("network", Utils.getNetwork(ct));
        params.put("advertising_id", DeviceUtils.getAdvertisingID(ct));
        params.put("appsflyer_id", DeviceUtils.getAppsflyerUID(ct));
        params.put("locale", GameConfigs.getInstance().getLang());
        params.put("access_token", AuthenConfigs.getInstance().getAccessToken());

        String url = ConstantApi.URL_BASE_PAYTECH + ConstantApi.URL_PURCHASE_PRODUCTS;
        if (!TextUtils.isEmpty(state)) {
            url += "?payload=" + state;
        } else {
            url += "?hl=en";
        }
        for (Map.Entry<String, String> param : set) {
            if (!TextUtils.isEmpty(param.getValue())) {
                url += "&" + param.getKey() + "=" + param.getValue();
            }
        }
        return url.replace(" ", "%20");
    }

    public static String getUrlDashboard(Context ct) {
        String appKey = PrefManager.getAppKey(ct);
        String appVersion = Utils.getGameVersion(ct);
        String url = PrefManager.getString(ct, ConstantApi.APP_UR, ConstantApi.BASE_URL) + ConstantApi.URL_WEBVIEW_DASHBOARD;
        url += "?sdk_version=" + Utils.stringNormalize(Utils.getSDKVersion(ct));
        url += "&device_network=" + Utils.stringNormalize(Utils.getNetwork(ct));
        url += "&appsflyer_id=" + DeviceUtils.getAppsflyerUID(ct);
        url += "&advertising_id=" + DeviceUtils.getAdvertisingID(ct);
        url += "&device_os=" + Utils.stringNormalize(DeviceUtils.getOSInfo());
        url += "&device_name=" + Utils.stringNormalize(DeviceUtils.getDevice());
        return url.replace("{app_key}", appKey).replace("{app_version}", appVersion);
    }

    public static String getUrlTerm(Context ct) {
        String appKey = PrefManager.getAppKey(ct);
        String appVersion = Utils.getGameVersion(ct);
        String url = PrefManager.getString(ct, ConstantApi.APP_UR, ConstantApi.BASE_URL) + ConstantApi.URL_WEBVIEW_TERM;
        return url.replace("{app_key}", appKey).replace("{app_version}", appVersion);
    }


    public static String getUrlBlockIP(Context context){
        String url= ConstantApi.BASE_URL + ConstantApi.URL_BLOCK_IP;
        String appKey = PrefManager.getAppKey(context);
        String appVersion = Utils.getGameVersion(context);
        return url.replace("{app_key}", appKey).replace("{app_version}", appVersion);
    }
}
