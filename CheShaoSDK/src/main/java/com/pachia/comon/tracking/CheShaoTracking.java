package com.pachia.comon.tracking;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerTrackingRequestListener;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantAppsFyer;
import com.pachia.comon.constants.ConstantTrackEvent;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.VerifyPurchaseObj;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.LogUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CheShaoTracking {
    public static String TAG = "=====Tracking=====";
    private String appsflyerDevkey = "W3k5j6miEBKbGBbteanNPM";
    private AppsFyer appsFyer;
    private Context context;
    private Firebase firebase;
    private long beginTime;
    FirebaseAnalytics mfirebaseAnalytics;

    private static CheShaoTracking cheShaoTracking;


    public void init(Application application) {
        context = application;
        appsFyer = AppsFyer.newInstance(application, appsflyerDevkey, conversionListener);
        firebase = Firebase.newInstance(application);
    }

    public static CheShaoTracking getInstance() {
        if (cheShaoTracking == null) {
            cheShaoTracking = new CheShaoTracking();
        }
        return cheShaoTracking;
    }

    /**
     * Lần đầu user mở game (mỗi install count 1 lần)
     */
    public void trackFirstLaunch() {
        beginTime = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("time_first_launcher", beginTime);
        trackEvent(ConstantTrackEvent.ACTION_FIRST_LAUNCHED, map);
    }

    /**
     * User mở game or quay lại game
     */
    public void trackAppLaunch() {
        beginTime = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("time_app_launcher", String.valueOf(beginTime));
        trackEvent(ConstantTrackEvent.ACTION_APP_LAUNCHED, map);
    }

    public void trackFromWeb(String label, String value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("TYPE_LOGIN", value);
        trackEvent(label, map);
    }

    /**
     * Logout thành công trên SDK
     *
     * @param userId
     */
    public void trackLogoutSuccess(int userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        trackEvent(ConstantTrackEvent.ACTION_LOGOUT_SUCCESS, map);

    }

    /**
     * User kill app
     */
/*    public void trackAppClose() {
        HashMap<String, Object> map = new HashMap<>();
        trackEvent(ConstantTrackEvent.TRACK_APP_CLOSE, map);

    }*/

    /**
     * Log các info liên quan tới in-app purchase (request gói nạp lỗi, pay lỗi ....)
     */
/*    public void trackPurchaseLog() {
        HashMap<String, Object> map = new HashMap<>();
        trackEvent(ConstantTrackEvent.TRACK_PURCHASE_LOG, map);

    }*/

    /**
     * Game Khi load màn hình payment thành công
     */
    public void trackPaymentScreenOpened() {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        trackEvent(ConstantTrackEvent.ACTION_PAYMENT_SCREEN_OPENED, hashMap);
    }

    /**
     * Game Khi load màn hình payment thành công
     */
    public void trackPaymentLoadScreenSuccess() {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        trackEvent(ConstantTrackEvent.ACTION_PAYMENT_SCREEN_LOAD_SUCCESS, hashMap);
    }

    /**
     * Game Khi load màn hình payment Fail
     */
    public void trackPaymentLoadScreenFail(String error_code, String error_message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        hashMap.put("error_code", error_code);
        hashMap.put("error_message", error_message);
        trackEvent(ConstantTrackEvent.ACTION_PAYMENT_SCREEN_LOAD_FAIL, hashMap);
    }


    /**
     * Game Khi load màn hình payment thành công
     */
    public void trackPaymentBeforVerify(String order_no) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        long currentTime = System.currentTimeMillis();
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        hashMap.put("order_no", order_no);
        hashMap.put("timestamp", String.valueOf(currentTime));
        PrefManager.saveLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_PAYMENT_BEFORE_VERIFY, currentTime);
        trackEvent(ConstantTrackEvent.ACTION_PAYMENT_BEFORE_VERIFY, hashMap);
    }

    /**
     * track when retry purrchase failed
     */
    public void trackRetryPurchaseFail(String code, String message) {
        long current = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("err_code", String.valueOf(code));
        hashMap.put("message", String.valueOf(message));
        trackEvent(ConstantTrackEvent.TRACK_RETRY_PURCHASE_FAIL, hashMap);
    }

    /**
     * Game Khi load màn hình payment thành công
     */
    public void trackPaymentVerifySuccess(VerifyPurchaseObj obj) {
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
            String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
            String user_id = "";
            long currentTime = System.currentTimeMillis();
            long startCall = PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_PAYMENT_BEFORE_VERIFY, currentTime);
            long deta_call = currentTime - startCall;
            try {
                user_id += GameConfigs.getInstance().getUser().getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            hashMap.put("user_id", user_id);
            hashMap.put("role_id", character_id);
            hashMap.put("area_id", area_id);
            hashMap.put("order_no", obj.getOrder_no());
            hashMap.put("timestamp", String.valueOf(currentTime));
            hashMap.put("delta_call", String.valueOf(deta_call));
            double price = 0.01 * obj.getPlatform_price();
            hashMap.put(AFInAppEventParameterName.REVENUE, price);
            hashMap.put(AFInAppEventParameterName.CONTENT_TYPE,
                    "category_rev");
            hashMap.put(AFInAppEventParameterName.CONTENT_ID,
                    "123456");
            hashMap.put(AFInAppEventParameterName.CURRENCY, "USD");
            hashMap.put(AFInAppEventParameterName.ORDER_ID, obj.getOrder_no());
            trackEventAF(AFInAppEventType.PURCHASE, hashMap);

/**
 * Track event Purchase firebase
 */
            HashMap<String, Object> hashMapFB = new HashMap<>();
            hashMapFB.put("user_id", user_id);
            hashMapFB.put("role_id", character_id);
            hashMapFB.put("area_id", area_id);
            hashMapFB.put("order_no", obj.getOrder_no());
            hashMapFB.put("timestamp", String.valueOf(currentTime));
            hashMapFB.put("delta_call", String.valueOf(deta_call));
            hashMapFB.put(FirebaseAnalytics.Param.COUPON, "SummerPromo");
            hashMapFB.put(FirebaseAnalytics.Param.CURRENCY, "USD");
            hashMapFB.put(FirebaseAnalytics.Param.VALUE, price);
            hashMapFB.put(FirebaseAnalytics.Param.TRANSACTION_ID, obj.getOrder_no());
            trackEventFireBase(FirebaseAnalytics.Event.PURCHASE, hashMapFB);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Game Khi load màn hình payment thành công
     */
    public void trackPaymentVerifyFail(String order_no, String type_error, String err_code, String err_mesage) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        long currentTime = System.currentTimeMillis();
        long startCall = PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_PAYMENT_BEFORE_VERIFY, currentTime);
        long deta_call = currentTime - startCall;
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        hashMap.put("order_no", order_no);
        hashMap.put("type_error", type_error);
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("delta_call", String.valueOf(deta_call));
        hashMap.put("error_code", err_code);
        hashMap.put("error_message", err_mesage);
        trackEvent(ConstantTrackEvent.ACTION_PAYMENT_VERIFY_FAIL, hashMap);
    }


    /**
     * Game bắt đầu giải nén resources trong bản build or OBB
     */
    public void trackStartExtractData(Activity activity) {
        HashMap<String, Object> hashMap = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("timestamp", String.valueOf(currentTime));
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME, currentTime);
        trackEvent(ConstantTrackEvent.ACTION_EXTRACT_STARTED, hashMap);

//        //TODO sao đoạn này em lại đặt là level_up ?
    }

    /**
     * Game kết thúc giải nén trong bản build or OBB
     */
    public void trackFinishExtractData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("delta_extract", String.valueOf(PrefManager.getLong(context, ConstantTrackEvent.START_TIME, currentTime)));
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME, 0);
        trackEvent(ConstantTrackEvent.ACTION_EXTRACT_FINISHED, hashMap);
    }

    /**
     * Game bắt đầu tải resources từ CDN
     * Chỉ track ở giai đoạn đầu của game (trước khi vào server), còn tải trong lúc đã chơi game thì ko track
     */
    public void trackDownloadResourceStarted() {
        HashMap<String, Object> map = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        map.put("delta", String.valueOf(delta));
        map.put("timestamp", String.valueOf(currentTime));
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME, currentTime);
        trackEvent(ConstantTrackEvent.ACTION_RESOURCE_STARTED, map);
    }

    /**
     * Game bắt đầu tải resources từ CDN
     * Chỉ track ở giai đoạn đầu của game (trước khi vào server), còn tải trong lúc đã chơi game thì ko track
     */
    public void trackDownloadResourceFinished() {
        HashMap<String, Object> map = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        long delta_resource = currentTime - PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_RESOURCE_STARTED, currentTime);
        map.put("timestamp", String.valueOf(currentTime));
        map.put("delta", String.valueOf(delta));
        map.put("delta_resource", String.valueOf(delta_resource));
        PrefManager.saveSetting(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_RESOURCE_FINISHED, currentTime);
        trackEvent(ConstantTrackEvent.ACTION_RESOURCE_FINISHED, map);
    }

    /**
     * Game kết giải nén resources tải từ CDN
     */
    public void trackExtractCDNFinished(Activity activity) {
        HashMap<String, Object> map = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        long delta_resource = currentTime - PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_RESOURCE_FINISHED, currentTime);
        map.put("timestamp", String.valueOf(currentTime));
        map.put("delta", String.valueOf(delta));
        map.put("delta_resource", String.valueOf(delta_resource));
        trackEvent(ConstantTrackEvent.ACTION_EXTRACT_CDN_FINISHED, map);
    }

    /**
     * Track session (time bắt đầu, time kết thúc, kéo dài bao nhiêu)
     */
    public void trackSession() {
        long endTime = System.currentTimeMillis();
        long deltaTime = endTime - beginTime;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("start_time", String.valueOf(beginTime));
        hashMap.put("end_time ", String.valueOf(endTime));
        hashMap.put("length_second", String.valueOf(deltaTime));
        trackEvent(ConstantTrackEvent.SESSION, hashMap);
    }

    public void trackLastSession(Context ct) {
        int minute = PrefManager.getInt(ct, Constants.SESSION_OUT_TIME, 10);
        long milisecond = minute * 60 * 1000;

        long begin = PrefManager.getLong(ct, ConstantTrackEvent.LAST_SESSION_START, 0);
        long endTime = PrefManager.getLong(ct, ConstantTrackEvent.LAST_SESSION_END, 0);
        long deltaTime = PrefManager.getLong(ct, ConstantTrackEvent.LAST_SESSION_DETAl, 0);
        // Validate last sesion data
        if (begin == 0 || endTime == 0)
            return;
        if (deltaTime < milisecond)
            return;
        if ((beginTime - endTime) < milisecond) {
            beginTime = begin;
            return;
        }
        LogUtils.d(TAG, "Begin: " + begin + " - End : " + endTime + " - Detal : " + deltaTime
        );
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("start_time", String.valueOf(begin));
        hashMap.put("end_time ", String.valueOf(endTime));
        hashMap.put("length_second", String.valueOf(deltaTime));
        trackEvent(ConstantTrackEvent.SESSION, hashMap);
        clearSession(ct);
    }

    public void saveSession(Context ct) {
        long endTime = System.currentTimeMillis();
        long deltaTime = endTime - beginTime;
        PrefManager.saveSetting(ct, ConstantTrackEvent.LAST_SESSION_START, beginTime);
        PrefManager.saveSetting(ct, ConstantTrackEvent.LAST_SESSION_END, endTime);
        PrefManager.saveSetting(ct, ConstantTrackEvent.LAST_SESSION_DETAl, deltaTime);
    }

    public void clearSession(Context ct) {
        PrefManager.saveSetting(ct, ConstantTrackEvent.LAST_SESSION_START, 0);
        PrefManager.saveSetting(ct, ConstantTrackEvent.LAST_SESSION_END, 0);
        PrefManager.saveSetting(ct, ConstantTrackEvent.LAST_SESSION_DETAl, 0);
    }

    /**
     * Login/register thành công trên SDK
     *
     * @param userId              user id
     * @param type                login success | register success
     * @param loginOrRegisterType email | playnow | facebook | google | apple ...
     */
    public void trackLoginSuccess(int userId, String type, String
            loginOrRegisterType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        long start_open_login = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, currentTime);
        long deta_login = currentTime - start_open_login;
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("delta_login", String.valueOf(deta_login));
        hashMap.put("user_id", String.valueOf(userId));
        hashMap.put("type", type);
        hashMap.put("login_or_register_type", loginOrRegisterType);

        trackEvent(AFInAppEventType.LOGIN, hashMap);
    }

    /**
     * trackClickBtnEnterGame (time bắt đầu, time kết thúc, kéo dài bao nhiêu)
     * User click button vào game (tùy từng game mà tên khác nhau) để chuyển sang màn hình game
     *
     * @param serverId Id server that user login
     */
    public void trackClickBtnEnterGame(String serverId) {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("server_id", String.valueOf(serverId));
        trackEvent(ConstantTrackEvent.ACTION_ENTER_GAME_BTN_CLICKED, hashMap);
    }

    /**
     * User tạo nhân vật or chọn nhân vật trong game thành công.
     */
    public void trackCharactorCreated() {
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        trackEvent(ConstantTrackEvent.ACTION_CHARACTER_CREATED, hashMap);

    }

    /**
     * SDK save area id & role id lên server thành công
     */
    public void trackCharactorCreatedSuccess() {
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        trackEvent(ConstantTrackEvent.ACTION_CHARACTER_CREATED_CALL_API_SUCCESS, hashMap);
    }

    /**
     * SDK save area id & role id lên server thất bại
     */
    public void trackCharactorCreatedFailed() {
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - beginTime;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("delta", String.valueOf(delta));
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        trackEvent(ConstantTrackEvent.ACTION_CHARACTER_CREATED_CALL_API_FAIL, hashMap);
    }

    /**
     * User hoàn thành hướng dẫn tân thủ.
     * Được quyết định khi user đạt mốc level X. Level X config động với mỗi game và được trả về từ server
     * @param user
     * @param role mã nhân vật
     * @param area Game save area id (mã server)
     */
    public void trackTutorialCompletion(int user, String role, String area) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);

        hashMap.put("af_success", true);
        hashMap.put("af_content_id", 123456);
        trackEvent(AFInAppEventType.TUTORIAL_COMPLETION, hashMap);
    }

    /**
     * Nhân vật trong game lên level
     *
     * @param user
     * @param role mã nhân vật
     * @param area Game save area id (mã server)
     */
    public void trackLevelAchieved(Activity activity, int user, String role, String area, String af_level) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        hashMap.put("af_level ", af_level);
        hashMap.put("af_score ", 1);
        trackEvent(AFInAppEventType.LEVEL_ACHIEVED, hashMap);

        if (af_level.equals(String.valueOf(GameConfigs.getInstance().getSdkConfig().getTutorialLevel()))) {
            CheShaoTracking.getInstance().trackTutorialCompletion(GameConfigs.getInstance().getUser().getId(), character_id, area_id);
        }
    }


    /**
     * Nhân vật trong game lên cấp VIP
     *
     * @param user
     * @param role mã nhân vật
     * @param area Game save area id (mã server)
     */
    public void trackVipAchieved(int user, String role, String area, String vip) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        hashMap.put("vip", vip);
        trackEvent(ConstantTrackEvent.ACTION_VIP_ACHIEVED, hashMap);
    }


    /**
     * Mở màn hình login của SDK
     */
    public void trackLoginScreenOpen() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_OPENED, hashMap);
    }


    /**
     * user tác động vào placeholder login email / phone number / username
     */
    public void trackClickLoginEmailButton() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_EMAIL_BTN_CLICKED, hashMap);
    }

    //---------------------------------------------------------------EMAIL----------------------------------------------------------------

    /**
     * user ko pass qua valid form login email của sdk
     *
     * @param errCode    mã lỗi code (1: username, 2: password)
     * @param errMessage nội dung lỗi
     */
    public void trackLoginEmailInputError(int errCode, String
            errMessage) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("error_code", String.valueOf(errCode));
        hashMap.put("error_message", errMessage);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_EMAIL_INPUT_ERROR, hashMap);
    }

    /**
     * sdk call api login email
     */
    public void trackLoginEmailCallApi() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_EMAIL_API_CALL, hashMap);
    }

    /**
     * sdk call api login email - thành công
     */
    public void trackLoginEmailCallApiSuccess() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
//        trackEvent(ConstantTrackEvent.TRACK_LS_LOGIN_EMAIL_CALL_API_SUCCESS, hashMap);
    }


    /**
     * sdk call api login email - Thất bại
     *
     * @param errCode mã lỗi code
     * @param mess    Nội dung lỗi
     */
    public void trackLoginEmailCallApiFail(int errCode, String mess) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
        hashMap.put("error_code", String.valueOf(errCode));
        hashMap.put("error_message", mess);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_EMAIL_API_CALL_FAIL, hashMap);
    }

    //---------------------------------------------------------------FACEBOOK----------------------------------------------------------------

    /**
     * user click nút login facebook
     */
    public void trackClickLoginFacebookButton() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_BTN_CLICKED, hashMap);
    }

    /**
     * user cancel login trên facebook sdk (app, webview của facebook)s
     */
    public void trackCancelLoginFacebook() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_USER_CANCELED, hashMap);
    }

    /**
     * user login trên facebook và có thông báo lỗi từ Facebook về SDK
     *
     * @param mess nội dung lỗi
     */
    public void trackLoginFacebookSdkErr(String mess) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("error_message", mess);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_HAVE_ERROR, hashMap);
    }


    /**
     * sdk call api login facebook
     */
    public void trackLoginFacebookCallApi() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_API_CALL, hashMap);
    }

    /**
     * sdk call api login facebook - thành công
     */
    public void trackLoginFacebookCallApiSuccess() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
//        trackEvent(ConstantTrackEvent.TRACK_LS_LOGIN_FACEBOOK_CALL_API_SUCCESS, hashMap);
    }


    /**
     * sdk call api login facebook - Thất bại
     *
     * @param errCode mã lỗi code
     * @param mess    Nội dung lỗi
     */
    public void trackLoginFacebookCallApiFail(int errCode, String mess) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
        hashMap.put("error_code", String.valueOf(errCode));
        hashMap.put("error_message", mess);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_API_CALL_FAIL, hashMap);
    }

    //---------------------------------------------------------------GOOGLE----------------------------------------------------------------

    /**
     * user click nút login Google
     */
    public void trackClickLoginGoogleButton() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_BTN_CLICKED, hashMap);
    }

    /**
     * user cancel login trên Google sdk (app, webview của facebook)s
     */
    public void trackCancelLoginGoogle() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_USER_CANCELED, hashMap);
    }

    /**
     * user login trên facebook và có thông báo lỗi từ Google về SDK
     *
     * @param mess    nội dung lỗi
     * @param errCode mã lỗi code
     */
    public void trackLoginGoogleSdkErr(int errCode, String mess) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("error_code", String.valueOf(errCode));
        hashMap.put("error_message", mess);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_HAVE_ERROR, hashMap);
    }


    /**
     * sdk call api login Google
     */
    public void trackLoginGoogleCallApi() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_API_CALL, hashMap);
    }

    /**
     * sdk call api login Google - thành công
     */
    public void trackLoginGoogleCallApiSuccess() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
//        trackEvent(ConstantTrackEvent.TRACK_LS_LOGIN_GOOGLE_CALL_API_SUCCESS, hashMap);
    }


    /**
     * sdk call api login Google - Thất bại
     *
     * @param errCode mã lỗi code
     * @param mess    Nội dung lỗi
     */
    public void trackLoginGoogleCallApiFail(int errCode, String mess) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
        hashMap.put("error_code", String.valueOf(errCode));
        hashMap.put("error_message", mess);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_API_CALL_FAIL, hashMap);
    }


    //---------------------------------------------------------------PLAYNOW----------------------------------------------------------------


    /**
     * sdk call api login PlayNow
     */
    public void trackLoginPlayNowCallApi() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_PLAYNOW_BTN_CLICKED, hashMap);
    }

    /**
     * sdk call api login PlayNow - thành công
     */
    public void trackLoginPlayNowCallApiSuccess() {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
//        trackEvent(ConstantTrackEvent.TRACK_LS_LOGIN_PLAYNOW_CALL_API_SUCCESS, hashMap);
    }


    /**
     * sdk call api login PlayNow - Thất bại
     *
     * @param errCode mã lỗi code
     * @param mess    Nội dung lỗi
     */
    public void trackLoginPlayNowCallApiFail(int errCode, String mess) {
        long current = System.currentTimeMillis();
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        long startTimeCallApi = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        hashMap.put("delta_call", String.valueOf(current - startTimeCallApi));
        hashMap.put("error_code", String.valueOf(errCode));
        hashMap.put("error_message", mess);
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_LOGIN_PLAYNOW_API_CALL_FAIL, hashMap);
    }

    /**
     * TRack get sdk-config success
     */
    public void trackGetSdkConfigSuccess() {
        long current = System.currentTimeMillis();
        long start_time = PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_LOAD_GAME_CONFIG_CALL_API_SUCCESS, System.currentTimeMillis());
        long deltaTime = current - start_time;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta_call", String.valueOf(deltaTime));
        trackEvent(ConstantTrackEvent.ACTION_LOAD_GAME_CONFIG_CALL_API_SUCCESS, hashMap);
    }

    /**
     * TRack get sdk-config success
     */
    public void trackGetSdkConfigFailed(int code) {
        long current = System.currentTimeMillis();
        long start_time = PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_LOAD_GAME_CONFIG_CALL_API_FAIL, System.currentTimeMillis());
        long deltaTime = current - start_time;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta_call", String.valueOf(code));
        trackEvent(ConstantTrackEvent.ACTION_LOAD_GAME_CONFIG_CALL_API_FAIL, hashMap);
    }


    //------------------------------------------------------------------------------------------------------

    /**
     * user chọn tab register trong màn hình login
     */
    public void trackClickBtnRegister() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_REGISTER_BTN_CLICKED, hashMap);
    }

    /**
     * màn hình register tải thành công
     */
    public void trackRegisterLoadSuccess() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_REGISTER_FORM_LOAD_SUCCESS, hashMap);
    }

    /**
     * user submit form register
     */
    public void trackRegisterSubmitForm() {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long startTime = PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(current - startTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_REGISTER_FORM_SUBMITED, hashMap);
    }

    /**
     * Login screen close
     */
    public void trackLoginScreenClose(View form, View text, View button) {
        long current = System.currentTimeMillis();
        PrefManager.saveLong(context, ConstantTrackEvent.START_TIME_CALL_API, current);
        long deltaTime = current - beginTime;
        long delta_login = current - PrefManager.getLong(context, ConstantTrackEvent.START_TIME_LOGIN, current);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        hashMap.put("delta", String.valueOf(deltaTime));
        hashMap.put("delta_login", String.valueOf(delta_login));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_CLOSE_BTN_CLICKED, hashMap);
    }


    /**
     * track when get authenconfig success
     */
    public void trackCallAuthenConfigSuccess() {
        long current = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        trackEvent(ConstantTrackEvent.ACTION_GET_AUTHEN_API_CALL_SUCCESS, hashMap);
    }

    /**
     * track when game maintain opened
     */
    public void trackMaintainScreenOpened() {
        long current = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        trackEvent(ConstantTrackEvent.ACTION_MAINTAIN_SCREEN_OPENED, hashMap);
    }

    /**
     * track when get authenconfig failed
     */
    public void trackCallAuthenConfigFailed() {
        long current = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        trackEvent(ConstantTrackEvent.ACTION_GET_AUTHEN_API_CALL_FAIL, hashMap);
    }


    /**
     * Bắt đầu bấm vào nút xin quyền , show dialog xin quyền
     */
    public void trackClickOverlayPermission() {
        trackEvent(ConstantTrackEvent.ACTION_OVERLAY_CLICK_PERMISSION, null);
    }

    /**
     * Bắt đầu bấm vào nút xin quyền , show dialog xin quyền
     */
    public void trackStartPermisison() {
        trackEvent(ConstantTrackEvent.ACTION_START_PERMISISON, null);
    }

    /**
     * Lắng nghe ở result activity sau khi xin quyền xong
     */
    public void trackEndPermisison() {
        trackEvent(ConstantTrackEvent.ACTION_END_PERMISSION, null);
    }

    /**
     * Click button register on form register
     */
    public void trackClickBtnRegisterInForm() {
        long current = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        long deltaTime = current - beginTime;
        hashMap.put("delta", String.valueOf(deltaTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_FORGOT_PASSWORD_BTN_CLICKED, hashMap);
    }

    /**
     * Load form register success
     */
    public void trackForgotPassword() {
        long current = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", String.valueOf(current));
        long deltaTime = current - beginTime;
        hashMap.put("delta", String.valueOf(deltaTime));
        trackEvent(ConstantTrackEvent.ACTION_LOGIN_SCREEN_FORGOT_PASSWORD_BTN_CLICKED, hashMap);
    }

    public String getAppsflyerDevkey() {
        return appsflyerDevkey;
    }

    public void trackEvent(String eventName, HashMap<String, Object> map) {
        LogUtils.d(TAG, eventName);
        appsFyer.trackEventWithCountFailed(eventName, map, new AppsFlyerTrackingRequestListener() {
            @Override
            public void onTrackingRequestSuccess() {
                LogUtils.d(TAG + ">SENT", eventName + " sent");
            }

            @Override
            public void onTrackingRequestFailure(String s) {
                LogUtils.e(TAG + ">SENT", eventName + " send failed");
            }
        });
        firebase.firebaseEventTrack(eventName, map);
    }

    private void trackEventAF(String eventName, HashMap<String, Object> map) {
        LogUtils.d(TAG, eventName);
        appsFyer.trackEventWithCountFailed(eventName, map, new AppsFlyerTrackingRequestListener() {
            @Override
            public void onTrackingRequestSuccess() {
                LogUtils.d(TAG + ">SENT", eventName + " sent");
            }

            @Override
            public void onTrackingRequestFailure(String s) {
                LogUtils.e(TAG + ">SENT", eventName + " send failed");
            }
        });
    }

    private void trackEventFireBase(String eventName, HashMap<String, Object> map) {
        LogUtils.d(TAG, eventName);
        firebase.firebaseEventTrack(eventName, map);
    }
    public void sendInstallToFirebase(Map<String, String> conversionData) {
        mfirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("install_time", conversionData.get("install_time") == null ? String.valueOf(new Date().getTime()) : conversionData.get("install_time"));
        bundle.putString("click_time", conversionData.get("click_time"));
        bundle.putString("media_source", conversionData.get("media_source") == null ? "organic" : conversionData.get("media_source"));
        bundle.putString("campaign", conversionData.get("campaign") == null ? "organic" : conversionData.get("campaign"));
        bundle.putString("install_type", conversionData.get("af_status"));
        mfirebaseAnalytics.logEvent("install", bundle);
    }

    AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {

        @Override
        public void onConversionDataSuccess(Map<String, Object> conversionData) {

            for (String attrName : conversionData.keySet()) {
                if (attrName.equals(ConstantAppsFyer.IS_FIRST_LAUCH) && ((boolean) conversionData.get(attrName))) {
                    LogUtils.d(TAG, "Start track event first launcher");
                    trackFirstLaunch();
                }
                Map<String, String> newMap = new HashMap<String, String>();
                for (Map.Entry<String, Object> entry : conversionData.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        newMap.put(entry.getKey(), (String) entry.getValue());
                    }
                }

                if (Objects.requireNonNull(conversionData.get(ConstantAppsFyer.IS_FIRST_LAUCH)).toString().trim().equals("true")) {
                    LogUtils.d(TAG, "Start Firebase Analytics Tracking");
                    sendInstallToFirebase(newMap);
                }
            }
        }

        @Override
        public void onConversionDataFail(String errorMessage) {
            LogUtils.d(TAG, "error getting conversion data: " + errorMessage);
        }

        @Override
        public void onAppOpenAttribution(Map<String, String> attributionData) {

            for (String attrName : attributionData.keySet()) {
                LogUtils.d(TAG, "attribute: " + attrName + " = " + attributionData.get(attrName));
            }

        }

        @Override
        public void onAttributionFailure(String errorMessage) {
            LogUtils.d(TAG, "error onAttributionFailure : " + errorMessage);
        }
    };

    public void trackLocalPaymentVerifyFail(VerifyPurchaseObj verifyPurchaseObj) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        String user_id = "";
        try {
            user_id += GameConfigs.getInstance().getUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        long startCall = PrefManager.getLong(CheShaoSdk.getInstance().getApplication(), ConstantTrackEvent.ACTION_PAYMENT_BEFORE_VERIFY, currentTime);
        long deta_call = currentTime - startCall;

        hashMap.put("user_id", user_id);
        hashMap.put("role_id", character_id);
        hashMap.put("area_id", area_id);
        hashMap.put("order_no", verifyPurchaseObj.getOrder_no());
        hashMap.put("type_error", "");
        hashMap.put("timestamp", String.valueOf(currentTime));
        hashMap.put("delta_call", String.valueOf(deta_call));
        hashMap.put("error_code", verifyPurchaseObj.getStatus());
        hashMap.put("error_message", verifyPurchaseObj.getDescription());
        trackEvent(ConstantTrackEvent.ACTION_PAYMENT_VERIFY_FAIL, hashMap);

    }
}