package com.pachia.ui.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;


import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.response.SaveFcmResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.ui.other.GamePresenterImpl;
import com.pachia.ui.other.IGamePresenter;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.supho.CheShaoSDK;
import com.quby.R;
import com.supho.model.NtfModel;
import com.supho.model.TimerData;
import com.supho.model.TimerObject;
import com.supho.utils.Constants;
import com.supho.utils.FloatButtonTimerHelper;
import com.supho.utils.NotificationUtils;
import com.supho.utils.Preference;
import com.supho.utils.Res;
import com.supho.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import static com.pachia.comon.constants.Constants.SAVE_FCM_NON_TOKEN;


public class PachiaFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PachiaFirebaseMessagingService.class
            .getSimpleName();

    public PachiaFirebaseMessagingService() {
    }

    public int getDrawableSmall(Context c) {
        return Res.drawableResource(c, R.drawable.transparent);
    }

    private static PachiaFirebaseMessagingService INSTANCE;

    public static PachiaFirebaseMessagingService getInstance() {
        if (INSTANCE == null) INSTANCE = new PachiaFirebaseMessagingService();
        return INSTANCE;
    }


    public void sendRegistrationToServer(Activity activity, String user, String refreshedToken) {
        try {
            Log.d(TAG, "sendToken/RegId to server: " + refreshedToken);
            AppsFlyerLib.getInstance().updateServerUninstallToken(CheShaoSDK.getApplicationContext(), refreshedToken);
            Preference.save(CheShaoSDK.getApplicationContext(), Constants.SAVE_FCM_KEY, refreshedToken);
            //TODO tại sao lại mất phần send lên server
            if (!TextUtils.isEmpty(refreshedToken)) {

                IGamePresenter gamePresenter = new GamePresenterImpl(new BaseView() {
                    @Override
                    public void showProgress(String mess) {

                    }

                    @Override
                    public void hideProgress() {
                        
                    }

                    @Override
                    public void success(Object x) {
                        if (x instanceof SaveFcmResponseObj) {
                            LogUtils.d(TAG, "Save FCM success : " + user);
                            if (TextUtils.isEmpty(user))
                                PrefManager.saveBoolean(activity, SAVE_FCM_NON_TOKEN, true);
                            else
                                PrefManager.saveBoolean(activity, SAVE_FCM_NON_TOKEN + AuthenConfigs.getInstance().getAccessToken(), true);
                        }
                    }

                    @Override
                    public void error(Object o) {
                        BaseObj baseObj = (BaseObj) o;
                        ToastUtils.showShortToast(activity, baseObj.getMessage());
                    }
                });
                gamePresenter.saveFCM(user, refreshedToken);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, e.getMessage());
        }
    }

    @SuppressLint({"MissingPermission", "CheckResult"})
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Context context = getApplicationContext();
            LogUtils.i(TAG, "onMessage: " + remoteMessage);

            // check forcelogout if true then logout
            Boolean forcelogoutNotifi = Boolean.valueOf(remoteMessage.getData().get("forcelogout"));
            LogUtils.i(TAG, "forcelogoutNotifi: " + forcelogoutNotifi);
            if (forcelogoutNotifi) {
                Intent intent = new Intent(Constants.INTENT_FILTER);
                intent.putExtra("category", "forcelogout");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            if (remoteMessage.getData().size() <= 0) return; // Vibrate for 30 ms
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title");
            String message = map.get("body");
//            String message = map.get("message");
            String id;
            if (Integer.parseInt(map.get("id")) < 10) {
                id = map.get("id");
            } else {
                id = null;
            }
            String url = map.get("url");
            String timer = map.get("timer");
            String deepLink = map.get("deep_link");

            LogUtils.i(TAG, "id: " + id);
            LogUtils.i(TAG, "title: " + title);
            LogUtils.i(TAG, "message: " + message);
            LogUtils.i(TAG, "url: " + url);
            LogUtils.i(TAG, "timer: " + timer);
            LogUtils.i(TAG, "deepLink: " + deepLink);

            //hiendv: load url image to cache first
            if (url != null) {
                Glide.with(context.getApplicationContext())
                        .load(url)
                        .downloadOnly(512, 256);
            }

            // Send broadcast
            Intent intent = new Intent(Constants.INTENT_FILTER);
            intent.putExtra("category", "gcm");
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("mesage", message);
            if (!TextUtils.isEmpty(deepLink)) {
                intent.putExtra("deep_link", deepLink);
                Preference.save(context, "deep_link", deepLink);
            }
            if (timer != null || timer == "") {
                Log.d(TAG, "VAO DAY");
                parseTimerData(context, timer, intent);
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            if (!TextUtils.isEmpty(id)) {
                NtfModel ntfModel = new NtfModel();
                ntfModel.setId(Integer.parseInt(id));
                ntfModel.setTitle(title);
                ntfModel.setNoti(Boolean.parseBoolean(message));
                NotificationUtils.addNtf(context, ntfModel);
            }
            if (!TextUtils.isEmpty(title)
                    || !TextUtils.isEmpty(message)) {
                NotificationUtils notificationUtils = NotificationUtils.getInstance(context);
                if (!TextUtils.isEmpty(deepLink)) notificationUtils.setDeeplink(deepLink);
                notificationUtils
                        .setMainClass(null)
                        .setMessage(message)
                        .setTitle(title)
                        .setSmallIcon(getDrawableSmall(context));
                if (url != null) {
                    notificationUtils.setImageUrl(url).showNotificationWithImage();
                } else {
                    notificationUtils.showNotification();
                }
                Vibrator v = (Vibrator) context
                        .getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null)
                    v.vibrate(30);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseTimerData(Context context, String timer, Intent intent) {
        if (!TextUtils.isEmpty(timer)) {
            try {
                Log.d(TAG, "Timer : " + timer);
                JSONArray timerArray = new JSONArray(timer);
                for (int i = 0; i < timerArray.length(); i++) {
                    JSONObject timerJsonObj = timerArray.getJSONObject(i);
                    long startTime = TimeUtils.getMilisecondByTimestamp(timerJsonObj.optLong("start"));
                    long endTime = TimeUtils.getMilisecondByTimestamp(timerJsonObj.optLong("end"));
                    int id = timerJsonObj.getInt("id");
                    final long now = Calendar.getInstance().getTimeInMillis();
                    TimerData timerData = FloatButtonTimerHelper
                            .getFloatButtonTimerData(context);//chua object arrayList timer
                    if (timerData == null) {
                        Log.d(TAG, "vao null");
                        ArrayList<TimerObject> listTimerObject = new ArrayList<>();
                        TimerObject timerObject = new TimerObject();
                        timerObject.setId(id);
                        timerObject.setStartTime(startTime);
                        timerObject.setEndTime(endTime);
                        listTimerObject.add(timerObject);
                        timerData = new TimerData();
                        timerData.setListTimerObject(listTimerObject);
                        timerData.saveData(context, timerData);
                        if (startTime <= now && now < endTime) {
                            FloatButtonTimerHelper.setcanRunTimer(true);
                            FloatButtonTimerHelper.setisRuningTimer(true);
                        }
                    } else {
                        Log.d(TAG, "Vao khong null");
                        ArrayList<TimerObject> listTimer = new ArrayList<>();
                        listTimer = timerData.getListTimerObject();
                        TimerObject timerObj = new TimerObject();
                        timerObj.setId(id);
                        timerObj.setStartTime(startTime);
                        timerObj.setEndTime(endTime);
                        listTimer.add(timerObj);
                        timerData.setListTimerObject(listTimer);
                        timerData.saveData(context, timerData);
                        if (FloatButtonTimerHelper.isRuningTimer() == true) {
                            FloatButtonTimerHelper.setcanRunTimer(false);
                        } else {
                            if (startTime <= now && now < endTime) {
                                FloatButtonTimerHelper.setcanRunTimer(true);
                                FloatButtonTimerHelper.setisRuningTimer(true);
                            } else {
                                FloatButtonTimerHelper.setcanRunTimer(false);
                            }
                        }
                    }
                }
                intent.putExtra("show-timer", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
