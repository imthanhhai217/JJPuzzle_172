package com.supho;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.game.LifeCycleActivity;
import com.pachia.comon.js.command.CmdDashboard;
import com.pachia.comon.listener.IMesssageListener;
import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.object.MessInGameObj;
import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.ui.dashboard.DashBoardUtils;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.quby.R;
import com.supho.gui.SuphoDialogInGameFragment;
import com.supho.gui.dialog.SuphoDialogStartWebFragment;
import com.supho.gui.dialog.SuphoHaveBackButtonFragment;
import com.supho.model.MUrl;
import com.supho.model.NtfModel;
import com.supho.model.giftimage.GifData;
import com.supho.utils.Constants;
import com.supho.utils.GifHelper;
import com.supho.utils.Preference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("deprecation")
public class CheShaoSDK {

    private static final String TAG = CheShaoSDK.class.getSimpleName();
    private static CheShaoSDK INSTANCE;

    public static Activity activity;
    public static Activity currentActivity;
    public static Context applicationContext;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean accelerometerPresent;
    private boolean canDraw;
    private AppOpsManager.OnOpChangedListener onOpChangedListener = null;

    private static boolean shouldShowFloatButton;


    private ArrayList<String> queuePopups = new ArrayList<String>();
    public static final String POPUP_HELLO = "popup_hello";
    public static final String POPUP_LINK = "pop_link";
    public static final String POPUP_MAINTAIN = "pop_maintain";
    private static final String POPUP_FLOAT_BUTTON = "popup_float_button";
    private static final String POPUP_ADS = "popup_ads";
    // hours
    private boolean isShowPopupLink = false;
    private boolean isPauseForHideFloatButton = false;

    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccel;
    private int scrollAnimIndex = 0;
    private int scrollAnimRepeatCount = 0;
    private ArrayList<TextView> textViews;
    private boolean isScrollingText = false;
    private static View viewNotice;
    private ObjectAnimator scrollTextAnimation;
    private FrameLayout scrollTextLayout;
    private long startPointOfOneScrollingTextNow = 0;
    private long durationOfOneScrollingTextNow = 0;
    private SdkConfigObj.Ex ex;


    //    private boolean isShaking = false;
    private SensorEventListener accelerometerListener = new SensorEventListener() {


        @Override
        public void onAccuracyChanged(Sensor event, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            try {

                // Shake detection
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 3f
                        && !Preference.getBoolean(activity, Constants.SHARED_PREF_SHOW_CONFIRM_DIALOG_HIDE_FLOAT, false)
                        && !Preference.getBoolean(activity, Constants.SHARED_PREF_SHOW_DASHBOARD, false)) {
                    Log.d(TAG, "VAO SENSOR");
                    Preference.remove(activity, Constants.SHARED_PREF_FLOAT_BUTTON_DISMISS_TIME);
                    Preference.remove(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON);
                    Preference.remove(activity, Constants.SHARED_PREF_SHOW_CONFIRM_DIALOG_HIDE_FLOAT);
                    Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);

                    Intent intent = new Intent(Constants.INTENT_FILTER);
                    intent.putExtra("category", "show_float_button");
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private CheShaoSDK() {
    }

    public static CheShaoSDK getInstance() {
        if (INSTANCE == null) {
            synchronized (CheShaoSDK.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CheShaoSDK();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressLint("MissingPermission")
    public void init(Activity activity) {
        Log.i(TAG, "QubySDK");
        CheShaoSDK.activity = activity;
        applicationContext = activity.getApplicationContext();
        Preference.save(activity, Constants.SHARED_PREF_MAIN_ACTIVITY,
                activity.getClass().getName());
        ChelShaoHelper.doHideFloatButton = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canDraw = Settings.canDrawOverlays(activity);
        }

        makeInstallReferrerClient(activity);
        try {

            activity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            activity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

            CookieSyncManager.createInstance(activity.getApplicationContext());

            sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

            if (sensorList.size() > 0) {
                accelerometerPresent = true;
                accelerometerSensor = sensorList.get(0);
            } else {
                accelerometerPresent = false;
            }
//            Lovely.getInstance().saveFCM(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
        checkTimeText();

        LifeCycleActivity.getInstance(activity).onCreate();//TODO new code
    }

    public void onDestroySDK() {

    }

    InstallReferrerClient referrerClient;

    private void makeInstallReferrerClient(Activity activity) {
        try {
            referrerClient = InstallReferrerClient.newBuilder(activity).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Connection established.
                            try {
                                ReferrerDetails response = referrerClient.getInstallReferrer();
                                String referrer = response.getInstallReferrer();
                                Log.d(TAG, "referrer : " + referrer);
                                Utils.setReferrer(activity, referrer);
                                referrerClient.endConnection();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app.
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CountDownTimer countDownTimer;
    boolean checkTimeInited = false;

    private void checkTimeText() {
        try {
            checkTimeInited = true;
            countDownTimer = new CountDownTimer(60 * 10 * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    SdkConfigObj obj = GameConfigs.getInstance().getSdkConfig();
                    if (obj != null && obj.isMessageInGame()) {
                        CheShaoSdk.getInstance().getMessagesInGame(activity, new IMesssageListener() {
                            @Override
                            public void onSuccess(ArrayList<MessInGameObj> data) {
                                if (data != null && data.size() > 0) {
                                    showTextScroll(data);
                                }
                            }
                        });
                    }
                    checkTimeText();
                }
            }.start();

            SdkConfigObj obj = GameConfigs.getInstance().getSdkConfig();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Context getApplicationContext() {
        if (activity == null && applicationContext != null) {
            return applicationContext;
        }
        if (activity == null && currentActivity != null) {
            return currentActivity.getApplicationContext();
        }
        return activity.getApplicationContext();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult:requestCode=" + requestCode
                + ";resultCode=" + resultCode);
        try {
            switch (requestCode) {
                case Constants.REQUEST_OVERLAY_PERMISSION:
                    requestOverlayPermission(activity);
                    break;
                case Constants.REQUEST_CODE_PICKER:
                    DashBoardUtils.getInstance().handleResult(activity, requestCode,
                            resultCode, data);
                    break;
                case Constants.REQUEST_CODE_FACEBOOK_LOGIN:
                    FacebookManager.getInstance(activity).onAuthResult(requestCode, resultCode, data);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @TargetApi(19)
    public void onWindowFocusChanged(boolean hasFocus) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                if (DeviceUtils.hasHardwareButtons()) {
                    if (hasFocus) {
                        activity.getWindow()
                                .getDecorView()
                                .setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    }
                } else {
                    if (hasFocus) {
                        activity.getWindow()
                                .getDecorView()
                                .setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logout() {
        try {
            Preference.remove(activity, Constants.SHARED_PREF_COOKIES);
            Preference.remove(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON);
            Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
            Preference.remove(activity, Constants.SHARED_PREF_NOTIFICATONS);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();

            queuePopups.clear();
            Preference.remove(activity, Constants.SHARED_PREF_FLOAT_BUTTON_DISMISS_TIME);
            Preference.remove(currentActivity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is used to exit the game or immediately exit the app, but do not call logout so do not exit the SDK.
     */
    public static void onBackPressed() {
        try {
            Log.d(TAG, "onDestroy");
            Preference.remove(activity, Constants.START_SESSION);
            Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
            System.gc();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public void customUrl(String url) {
        try {
            if (!isLoggedIn()) {
                String mess = activity.getResources().getString(R.string.lbl_not_login);
                ToastUtils.showShortToast(activity, mess);
            } else {
                QubyDialogStartWebFragment dialogStartWebFragment = new QubyDialogStartWebFragment(activity, "custom_url", url);
                dialogStartWebFragment.show(activity.getFragmentManager(), "dialog webview");
                isShowPopupLink = true;
                dialogStartWebFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShowPopupLink = false;
                        Intent intent = new Intent(Constants.INTENT_FILTER);
                        intent.putExtra("category", "float_button");
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }
                });
                hideNotiFloatButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    boolean isShowSocialDialog = false;

    private void showFragmentHaveBackButton(String url, boolean isEncrypted) {
        try {
            if (url != null && !url.equals("")) {
                FragmentManager fm = activity.getFragmentManager();
                SuphoHaveBackButtonFragment fr = (SuphoHaveBackButtonFragment) fm
                        .findFragmentByTag(Constants.TAG_FRAGMENT2);

                isShowSocialDialog = true;
                if (fr == null) {
                    fr = new SuphoHaveBackButtonFragment(new MUrl(url, isEncrypted));
                    fr.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (isShowSocialDialog
                                    && Preference
                                    .getBoolean(
                                            activity,
                                            Constants.SHARED_PREF_HIDE_FLOAT_BUTTON,
                                            true)
                                    && Utils.isDashboardEnabled(activity)) {
                                Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
                            }
                            isShowSocialDialog = false;
                        }
                    });
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(fr, Constants.TAG_FRAGMENT);
                    // fragmentTransaction.commit();
                    fragmentTransaction.commitAllowingStateLoss();
                } else {
                    try {
                        fr.loadUrlWithMobHeaders(new MUrl(url, isEncrypted));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d(TAG, "url null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        close(true);
    }

    private void close(boolean check) {
        try {
            Intent i = new Intent(Constants.INTENT_FILTER);
            i.putExtra("category", "dashboard_close");
            LocalBroadcastManager.getInstance(activity).sendBroadcast(i);
            if (!GameConfigs.getInstance().getShow_qc())
                queuePopups.add(POPUP_FLOAT_BUTTON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(AuthenConfigs.getInstance()
                .getAccessToken());
    }


    public void showPopup() {
        if (queuePopups == null || queuePopups.isEmpty()) {
            return;
        }
        try {
            String popup = queuePopups.get(0);
            Log.d(TAG, "Show popup: " + popup);
            queuePopups.remove(0);
            switch (popup) {
                case POPUP_LINK:
                    //show Popup link
                    shouldShowFloatButton = true;
                    SuphoDialogStartWebFragment dialogStartWebFragment = new SuphoDialogStartWebFragment(activity, true, true);
                    dialogStartWebFragment.show(activity.getFragmentManager(), "dialog webview");
                    Preference.save(activity, "save_ads", true);
                    break;
                case POPUP_MAINTAIN:
                    //show Popup link
                    shouldShowFloatButton = true;
                    SuphoDialogStartWebFragment dialogStartWebFragment1 = new SuphoDialogStartWebFragment(activity, true, true);
                    dialogStartWebFragment1.show(activity.getFragmentManager(), "dialog webview");
                    Preference.save(activity, "save_ads", true);
                    break;
                case POPUP_FLOAT_BUTTON:
                    shouldShowFloatButton = true;
                    Intent intent = new Intent(Constants.INTENT_FILTER);
                    intent.putExtra("category", "float_button");
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
//                    showPopup();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onRequestPermissionsResult(Activity gameActivity, int requestCode, String[] permissions, int[] grantResults) {
        CmdDashboard.getInstance().onRequestPermissionsResult(gameActivity, requestCode, permissions, grantResults);
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        private final String TAG = ActivityLifecycleCallbacks.class
                .getSimpleName();

        int count = 0;

        @Override
        public void onActivityCreated(Activity a, Bundle b) {
        }

        @Override
        public void onActivityStarted(Activity a) {
        }

        @Override
        public void onActivityResumed(Activity a) {
           LifeCycleActivity.getInstance(a).onResume();


            if (!isGrantedPermission(activity)) {

            } else {
                if(GameConfigs.getInstance().getSdkConfig()!=null && GameConfigs.getInstance().getSdkConfig().getEx()!=null)
                ChelShaoHelper.showNotiFloatButton(GameConfigs.getInstance().getSdkConfig().getEx());

//                Intent intent = new Intent(Constants.INTENT_FILTER);
//                intent.putExtra("category", "float_button");
//                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
            }


            if (!checkTimeInited && !isPhoneIsLockedOrNot(activity) &&
                    (!a.getClass().getSimpleName().equals("LovelyImageGaleryActivity")
                            || !a.getClass().getSimpleName().equals("FacebookActivity"))) {
                checkTimeText();
            }
            try {
                currentActivity = a;
                isPauseForHideFloatButton = false;
                if (isScrollingText && scrollTextAnimation != null) {
                    long resumedPoint = System.currentTimeMillis() - startPointOfOneScrollingTextNow;
                    scrollTextAnimation.start();
                    if (resumedPoint < durationOfOneScrollingTextNow) {
                        scrollTextAnimation.setCurrentPlayTime(resumedPoint);
                    }
                }

                changeInForegroundStatus(true);
                if (a.equals(activity)) {

                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .registerReceiver(mMessageReceiver, new IntentFilter(Constants.INTENT_FILTER));

                    CookieSyncManager.getInstance().startSync();
                    if (!Preference.getBoolean(activity, Constants.SHARED_PREF_SHOW_DASHBOARD, false) && !isShowPopupLink) {
                        Intent intent = new Intent(Constants.INTENT_FILTER);
                        intent.putExtra("category", "float_button");
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }
                    if (!Preference.getBoolean(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON, false)) {
                        CheShaoSDK.getInstance().registerSensor();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onActivityPaused(Activity a) {
            try {
                Log.i(TAG, "onActivityPaused:" + a.getClass().getSimpleName());
                count++;
                currentActivity = a;

                if (countDownTimer != null) countDownTimer.cancel();
                countDownTimer = null;

                isPauseForHideFloatButton = true;

                changeInForegroundStatus(false);
                hideNotiFloatButton();
                if (isPhoneIsLockedOrNot(activity)) {
                    removeViewNotice();
                    checkTimeInited = false;
                }

                if (a.equals(activity)) {
                    CookieSyncManager.getInstance().stopSync();
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .unregisterReceiver(mMessageReceiver);
                    if (!Preference.getBoolean(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON, false)) {
                        CheShaoSDK.getInstance().unRegisterSensor();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onActivityStopped(Activity a) {

        }

        @Override
        public void onActivityDestroyed(Activity a) {
            try {
                if (a.equals(activity)) {
                    CheShaoTracking.getInstance().saveSession(a);
//                    LovelyTracking.getInstance().trackAppClose();
                    Log.i(TAG, "onActivityDestroyed:" + a.getClass().getSimpleName());
                    if (a.equals(activity)) {
                        if (!a.getClass().getSimpleName()
                                .equals("FacebookActivity") && !a.getClass().getSimpleName()
                                .equals("QubyImageGaleryActivity")) {
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity a, Bundle b) {
        }

    };

    private boolean isPhoneIsLockedOrNot(Context context) {
        boolean isLocked = false;
        // First we check the locked state
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean inKeyguardRestrictedInputMode = keyguardManager.isKeyguardLocked();

        if (inKeyguardRestrictedInputMode) {
            isLocked = true;

        } else {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                isLocked = !powerManager.isInteractive();
            } else {
                isLocked = !powerManager.isScreenOn();
            }
        }
        Log.d(TAG, "isLocked : " + isLocked);
        return isLocked;
    }

    public void registerSensor() {
        try {
            if (accelerometerPresent) {
                if (accelerometerPresent) {
                    sensorManager.registerListener(accelerometerListener,
                            accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterSensor() {
        try {
            if (accelerometerPresent) {
                sensorManager.unregisterListener(accelerometerListener);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "BroadcastReceiver::onReceive");
            try {
                String category = intent.getStringExtra("category");
                LogUtils.i(TAG, "category:" + category);
                if (category != null) {
                    switch (category) {
                        case "forcelogout":
                            DialogUtils.showExpireDialog(activity);
                            break;
                        case "gcm":
                            try {
                                Log.d(TAG, "khaitran " + !Preference.getBoolean(activity, Constants.SHARED_PREF_SHOW_DASHBOARD, false)
                                        + " , " + Preference.getBoolean(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON, true));
                                if (AuthenConfigs.getInstance().getAuthenConfigObj() != null
                                        && isLoggedIn()
                                        && Utils.isDashboardEnabled(activity)
                                        && !Preference.getBoolean(activity, Constants.SHARED_PREF_SHOW_DASHBOARD, false)
                                        && Preference.getBoolean(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON, true)) {
                                    Log.d(TAG, "id gcm: " + intent.getStringExtra("id"));
                                    int id = Integer.parseInt(intent.getStringExtra("id"));
                                    String textTitle = intent.getStringExtra("title");
                                    boolean noti = Boolean.parseBoolean(intent.getStringExtra("noti"));

                                    ArrayList<NtfModel> listId = new ArrayList<>();
                                    NtfModel ntfModel = new NtfModel();
                                    ntfModel.setId(id);
                                    ntfModel.setTitle(textTitle);
                                    ntfModel.setNoti(noti);
                                    //check neu ko phai id = 2,3,5
                                    if (ntfModel.getTitle() != null)
                                        listId.add(ntfModel);
                                }

                                if (intent.getBooleanExtra("play_gif", false)) {
                                    GifHelper.playGif(activity);
                                }

                                if (intent.hasExtra("gif_data")) {
                                    String gifDataString = intent.getStringExtra("gif_data");
                                    GifHelper.fetchFramesImage(activity, GifData.parse(gifDataString));
                                    GifHelper.playGif(activity);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "mobOpenFanPage":
                            try {
                                String params = intent.getStringExtra("data");
                                CmdDashboard.getInstance().openFBFanpage(activity, params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "mobOpenGroup":
                            try {
                                String params = intent.getStringExtra("data");
                                CmdDashboard.getInstance().openFBGroup(activity, params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "mobOpenBrowser":
                            try {
                                String params = intent.getStringExtra("data");
                                CmdDashboard.getInstance().openBrowser(activity, params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "float_button":
                            if (GameConfigs.getInstance().getSdkConfig() != null) {
                                SdkConfigObj.Ex ex = GameConfigs.getInstance().getSdkConfig().getEx();
//                                requestOverlayPermission(activity);
                            }
                            break;
                        case "hide_float_button":
                            ChelShaoHelper.doHideFloatButton = true;
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void showTextScroll(ArrayList<MessInGameObj> listNoti) {

        if (this.listNoti.size() == 0) {
            this.listNoti = listNoti;
        } else {
            this.listNoti.addAll(listNoti);
        }
        showTextScroll(activity, listNoti.size() - 1, listNoti);
    }

    private ArrayList<MessInGameObj> listNoti = new ArrayList<>();

    public void showTextScroll(final Activity activity, int repeatCount, ArrayList<MessInGameObj> listNoti) {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    if (viewNotice == null)
                        viewNotice = inflater.inflate(R.layout.layout_notice, null);
                    if (scrollTextLayout == null) {
                        scrollTextLayout = (FrameLayout) viewNotice.findViewById(R.id.layoutNotiIngame);
                        /*CHỈNH LAYOUT = 4/5 CHIỀU NGẮN CỦA MÀN HÌNH*/
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollTextLayout.getLayoutParams();
                        layoutParams.width = rootView.getWidth() / 5 * 4;
                        Log.i("Layout Width", layoutParams.width + " | ScreenWidth: " + rootView.getWidth());
                    }
                    TextView scrollText = (TextView) viewNotice.findViewById(R.id.txt_notice);
                    Rect bounds = new Rect();
                    Paint textPaint = scrollText.getPaint();
                    long layoutWidth = rootView.getWidth() / 5 * 4;
                    if (isScrollingText) {
                        initScrollView(listNoti, textPaint, bounds, scrollTextLayout);
                    } else {
                        /* FIRST TIME SET UP ANIMATION */
                        textViews = new ArrayList<>();
                        scrollAnimIndex = 0;
                        scrollAnimRepeatCount = 0;
                        initScrollView(listNoti, textPaint, bounds, scrollTextLayout);
                        rootView.addView(viewNotice);
                        int startPoint = (rootView.getWidth() / 5 * 4) + rootView.getWidth() / 10;
                        startScrollingText(listNoti, textViews.get(scrollAnimIndex), layoutWidth, startPoint, repeatCount);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeViewNotice() {
        try {
            ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (viewNotice != null)
                rootView.removeView(viewNotice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideTextScroll(final Activity activity, boolean isAnimated) {
        try {
            LogUtils.i("HERE", " Hiding");
            ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (isScrollingText) {
                if (isAnimated) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Animation animation = AnimationUtils.loadAnimation(
                                    activity, R.anim.top_out);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }
                            });
                            if (viewNotice != null) {
                                viewNotice.startAnimation(animation);
                                rootView.removeView(viewNotice);
                            }
                        }
                    });
                } else {
                    try {
                        if (viewNotice != null)
                            rootView.removeView(viewNotice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isScrollingText = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initScrollView(ArrayList<MessInGameObj> listNoti, Paint textPaint, Rect bounds, FrameLayout layout) {
        try {
            for (int i = 0; i < listNoti.size(); i++) {
                Spanned text = Html.fromHtml(listNoti.get(i).getText());
                TextView textView = new TextView(activity);
                textPaint.getTextBounds(String.valueOf(text), 0, text.length(), bounds);
                float textLength = bounds.width();
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                if (DeviceUtils.isTablet(activity)) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                } else {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                }
//                textView.setTextColor(Color.WHITE);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                textView.setLayoutParams(lp);
                textView.setText(text);
                final MessInGameObj itemNoti = listNoti.get(i);
                textView.setTextColor(Color.WHITE);
                switch (listNoti.get(i).getType()) {
                    case 1:
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setMessage(R.string.open_with_browser)
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int index) {
                                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(itemNoti.getLink())));
                                                }

                                            })
                                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    builder.create().dismiss();
                                                }

                                            });

                                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                        }
                                    });
                                    builder.create().show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case 2:
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    String url = itemNoti.getLink();
                                    if (url != null && !url.equals("")) {
                                        FragmentManager fm = activity.getFragmentManager();
                                        SuphoDialogInGameFragment fr = (SuphoDialogInGameFragment) fm
                                                .findFragmentByTag(Constants.TAG_FRAGMENT);

//                                    isShowSocialDialog = true;
                                        if (fr == null) {
                                            fr = new SuphoDialogInGameFragment(new MUrl(url, false));
                                            fr.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    if (/*isShowSocialDialog && */Preference.getBoolean(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON, true)
                                                            && Utils.isDashboardEnabled(activity)) {
                                                        Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
                                                    }
//                                                isShowSocialDialog = false;
                                                }
                                            });
                                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                            fragmentTransaction.add(fr, Constants.TAG_FRAGMENT);
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }
                                    } else {
                                        Log.d(TAG, "url null");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case 3:
                        break;
                }
                textViews.add(textView);
                layout.addView(textView);
                textView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startScrollingText(ArrayList<MessInGameObj> listNoti, TextView textView, long layoutWidth, float startPoint, int repeatCount) {
        try {
            scrollAnimRepeatCount++;
            if (!isScrollingText)
                isScrollingText = true;
            textView.setVisibility(View.VISIBLE);
//            textView.setText(listNoti.get(scrollAnimRepeatCount).getText().toString());
            textView.setX(startPoint);
            float scale = 1;
            try {
                scale = 3 / (activity.getResources().getDisplayMetrics().density);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long s = layoutWidth + textView.getLayoutParams().width;
            long textDuration = (long) (s * Constants.DEFAULT_SPEED * scale);
            durationOfOneScrollingTextNow = textDuration;

            scrollTextAnimation = ObjectAnimator.ofFloat(textView, "x", -textView.getLayoutParams().width);
            scrollTextAnimation.setDuration(textDuration);
            scrollTextAnimation.setInterpolator(new LinearInterpolator());
            scrollTextAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    try {
                        textView.setVisibility(View.GONE);
                        if (scrollAnimRepeatCount <= repeatCount) {
                            startScrollingText(listNoti, textViews.get(scrollAnimRepeatCount), layoutWidth, startPoint, repeatCount);
                            LogUtils.i("Scroll", "1");
                        } else {
                            hideTextScroll(activity, true);
                            listNoti.removeAll(listNoti);
                            textViews.removeAll(textViews);
                            scrollAnimRepeatCount = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            startPointOfOneScrollingTextNow = System.currentTimeMillis();
            scrollTextAnimation.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestOverlayPermission(final Activity activity) {
        try {
            if (activity != null) {
                if (!isGrantedPermission(activity)) {
                    if (!ChelShaoHelper.didShowPermissionDialog) {
                        showDialogRequestOverlayPermission();
                    }
                } else {
                    if (isInForeground() && !shouldShowFloatButton) {

                        if (GameConfigs.getInstance().getSdkConfig().getEx() != null && GameConfigs.getInstance().getSdkConfig().getEx().isShowLogo()) {
                            showNotiFloatButton(GameConfigs.getInstance().getSdkConfig().getEx());

//                            showNotiFloatButton(ex);
                        }
                    }
                    Intent intent = new Intent(Constants.INTENT_FILTER);
                    intent.putExtra("category", "float_button");
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogRequestOverlayPermission() {
        try {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(activity);
            }

            String appName = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
            builder.setTitle(activity.getString(R.string.attention_overlay))
                    .setMessage(String.format(activity.getString(R.string.overlay_message), appName))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = null;
                            canDraw = Settings.canDrawOverlays(activity);
                            CheShaoTracking.getInstance().trackClickOverlayPermission();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                AppOpsManager opsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
                                onOpChangedListener = new AppOpsManager.OnOpChangedListener() {
                                    @Override
                                    public void onOpChanged(String op, String packageName) {
                                        PackageManager packageManager = activity.getPackageManager();
                                        String myPackageName = activity.getPackageName();
                                        if (myPackageName.equals(packageName) && AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW.equals(op)) {
                                            canDraw = true;
                                        }
                                    }
                                };
                                opsManager.startWatchingMode(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, null, onOpChangedListener);
                            }

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + activity.getPackageName()));
                            }
                            dialog.dismiss();
                            ChelShaoHelper.didShowPermissionDialog = false;
                            (activity).startActivityForResult(intent, Constants.REQUEST_OVERLAY_PERMISSION);
                        }
                    })
                    .setCancelable(false)
                    .setIcon(activity.getApplicationInfo().icon)
                    .show();
            ChelShaoHelper.didShowPermissionDialog = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotiFloatButton(SdkConfigObj.Ex ex) {
        if (ChelShaoHelper.isShowingNotiFloatButton()) {
            return;
        }
        boolean isShowLogo = ex.isShowLogo();
        if (isShowLogo) {

        }
    }

    public void hideNotiFloatButton() {
        ChelShaoHelper.hideNotiFloatButton();
    }

    public boolean isGrantedPermission(Context context) {
        boolean isGrandted = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            isGrandted = true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isGrandted = canDraw;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            isGrandted = Settings.canDrawOverlays(context);
        }
        return isGrandted;
    }

    private void changeInForegroundStatus(boolean isInForeground) {
        Preference.save(activity, "isInForeground", isInForeground);
    }

    private boolean isInForeground() {
        try {
            return Preference.getBoolean(activity, "isInForeground", true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressLint("NewApi")
    public void shareImageToFacebook(Activity activity, Bitmap image) {

        try {
            try {
                Intent openFB = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                activity.startActivity(openFB);
                Log.i(TAG, "shareImageToFacebook: " + openFB);
            } catch (Exception e) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(R.string.notifi);
                    builder.setMessage(R.string.dialog_share_image_facebook)
                            .setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int index) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.katana"));
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    builder.create().dismiss();
                                }
                            });

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
                    });
                    builder.create().show();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            ShareDialog shareDialog = new ShareDialog(activity);
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getQueuePopups() {
        return queuePopups;
    }


}