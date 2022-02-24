package com.pachia.comon.game;

import static com.pachia.comon.constants.Constants.AREA_ID;
import static com.pachia.comon.constants.Constants.ROLE_ID;
import static com.pachia.comon.constants.Constants.SAVE_FCM_NON_TOKEN;
import static com.facebook.login.widget.ProfilePictureView.TAG;

import android.app.Activity;
import android.app.Application;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerLib;
import com.pachia.comon.api.RetrofitClient;
import com.pachia.comon.cmd.CmdPaymentV3;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.listener.IBlockIpListener;
import com.pachia.comon.listener.ILoginListener;
import com.pachia.comon.listener.ILogoutListener;
import com.pachia.comon.listener.IMesssageListener;
import com.pachia.comon.listener.IPaymentListener;
import com.pachia.comon.listener.ISaveCharactorListener;
import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.login.GoogleManager;
import com.pachia.comon.object.AuthenConfigObj;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.MessInGameObj;
import com.pachia.comon.object.PurchaseHistoryObj;
import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.object.VerifyPurchaseObj;
import com.pachia.comon.object.response.AuthenConfigResponseObj;
import com.pachia.comon.object.response.MessInGameResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.ui.block.BlockFragment;
import com.pachia.ui.dashboard.DashboardFragment;
import com.pachia.ui.login.ILoginPresenter;
import com.pachia.ui.login.LoginFragment;
import com.pachia.ui.login.LoginPresenterImpl;
import com.pachia.ui.notification.PachiaFirebaseMessagingService;
import com.pachia.ui.other.GamePresenterImpl;
import com.pachia.ui.other.IGamePresenter;
import com.pachia.ui.payment.PaymentNativeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onesignal.OneSignal;
import com.quby.R;
import com.supho.ChelShaoHelper;
import com.supho.CheShaoSDK;

import java.util.ArrayList;
import java.util.Timer;

import cz.msebera.android.httpclient.Header;


public class CheShaoSdk implements ICheshaoSdk {
    private static Application sApp;
    private static CheShaoSdk cheShaoSdk;
    private static CheShaoSDK cheShaoSDK;
    Handler mHandler;
    Runnable mRunable;
    private Timer timerAsync;
    private BlockFragment dialogFragment;

    public Application getApplication() {
        return sApp;
    }

    public static CheShaoSdk getInstance() {
        if (cheShaoSdk == null) {
            cheShaoSdk = new CheShaoSdk();
            cheShaoSDK = CheShaoSDK.getInstance();
        }
        return cheShaoSdk;
    }

    public void initSdk(Activity activity) {
        cheShaoSDK.init(activity);
        try {
            PackageInfo pInfo = null;

            pInfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);


            String appVersionName = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
            String appVersionCode = Utils.getGameVersionCode(CheShaoSdk.getInstance().getApplication());
            String advertising_id = DeviceUtils.getAdvertisingID(CheShaoSdk.getInstance().getApplication());
            checkIpRequest(activity);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void initSdkOneSignal(Activity activity, String oneSignalAppKey) {
        cheShaoSDK.init(activity);
        try {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            OneSignal.initWithContext(activity);
            OneSignal.setAppId(oneSignalAppKey);


            PackageInfo pInfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);

            String appVersionName = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
            String appVersionCode = Utils.getGameVersionCode(CheShaoSdk.getInstance().getApplication());
            String advertising_id = DeviceUtils.getAdvertisingID(CheShaoSdk.getInstance().getApplication());
            checkIpRequest(activity);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init(@NonNull Application application, String appkey, String facebookId) {
        sApp = application;

        //New SDK init
        CheShaoTracking.getInstance().init(sApp);
        CheShaoTracking.getInstance().trackAppLaunch();
        CheShaoTracking.getInstance().trackLastSession(sApp);

        //init GameConfig
        GameConfigs.getInstance().setAppKey(appkey);
        GameConfigs.getInstance().initFacebook(facebookId);
        mHandler = new Handler();

        timerAsync = new Timer();
    }

    public void login(Activity activity, ILoginListener listener) {
        //check authen config , if not exit , will not show login dialog
        if (GameConfigs.getInstance().isLogin()) {
            String mess = activity.getResources().getString(R.string.lbl_already_login);
            ToastUtils.showShortToast(activity, mess);
            return;
        }
        ILoginPresenter pr = new LoginPresenterImpl(new BaseView() {
            @Override
            public void showProgress(String message) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showLoading(activity, true);
                    }
                });

            }

            @Override
            public void hideProgress() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showLoading(activity, false);
                    }
                });

            }

            @Override
            public void success(Object x) {
                if (x instanceof AuthenConfigResponseObj) {
                    AuthenConfigObj obj = ((AuthenConfigResponseObj) x).getData();
                    AuthenConfigs.getInstance().setAuthenConfigObj(obj);
                }
                showFormLogin(activity, new ILoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        AppsFlyerLib.getInstance().setCustomerUserId(GameConfigs.getInstance().getUser().getId() + "");
//                        saveFCM(activity);
                        if (listener != null)
                            listener.onLoginSuccess();

                        Log.d(TAG, "userId : " + GameConfigs.getInstance().getUser().getId());
                    }

                    @Override
                    public void onRegisterSuccess(String param) {

                    }
                });
            }

            @Override
            public void error(Object o) {
                showFormLogin(activity, listener);
            }
        });
        pr.getAuthenConfig();
    }

    public void showFormLogin(Activity activity, ILoginListener listener) {
        if (mHandler != null && mRunable != null) {
            mHandler.removeCallbacks(mRunable);
        }
        mRunable = new Runnable() {
            @Override
            public void run() {
                DialogFragment newFragment = (DialogFragment) activity.getFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
                if (newFragment == null)
                    newFragment = LoginFragment.newInstance(listener);
                newFragment.setCancelable(false);
                if (newFragment.getDialog() != null && newFragment.getDialog().isShowing())
                    return;
                newFragment.show(activity.getFragmentManager(), LoginFragment.class.getSimpleName());
            }
        };
        mHandler.postDelayed(mRunable, 200);

    }

    public void logOut(Activity activity) {
        cheShaoSDK.logout();
        if (GameConfigs.getInstance().getUser() != null || GameConfigs.getInstance().isLogin()) {
            if (GameConfigs.getInstance().getUser() != null)
                CheShaoTracking.getInstance().trackLogoutSuccess(GameConfigs.getInstance().getUser().getId());
            RetrofitClient.clearInstant();
            GameConfigs.clearInstance();
            GoogleManager.getInstance(sApp).logout();
            FacebookManager.getInstance(sApp).logout();
            PrefManager.saveBoolean(sApp, SAVE_FCM_NON_TOKEN + AuthenConfigs.getInstance().getAccessToken(), false);
            PrefManager.saveSetting(sApp, ROLE_ID, "");
            PrefManager.saveSetting(sApp, AREA_ID, "");
            PrefManager.saveAccessToken(sApp, "");
            AppsFlyerLib.getInstance().setCustomerUserId("");
            ChelShaoHelper.hideNotiFloatButton();
            CheShaoSDK.getInstance().logout();
            String mess = activity.getResources().getString(R.string.lbl_log_out_success);
            ToastUtils.showShortToast(activity, mess);
        } else {
            String mess = activity.getResources().getString(R.string.lbl_not_login);
            ToastUtils.showShortToast(activity, mess);
        }
    }

    public void logOut(Activity activity, ILogoutListener logoutListener) {
        logOut(activity);
        if (logoutListener != null)
            logoutListener.onLogoutSuccess();
    }

    public void onLoginSuccess(Activity activity, String tab) {
        if (GameConfigs.getInstance().getUser() != null && GameConfigs.getInstance().getUser().getName() != null) {

            String name = GameConfigs.getInstance().getUser().getName();
            name = TextUtils.isEmpty(name) ? "" : (" " + name + "!");
            String mess = activity.getResources().getString(R.string.welcome) + name;
            ToastUtils.showShortToast(activity, mess);
            OneSignal.sendTag("uid", String.valueOf(GameConfigs.getInstance().getUser().getId()));
        }
        if (GameConfigs.getInstance().getSdkConfig() != null) {
            ChelShaoHelper.showNotiFloatButton(GameConfigs.getInstance().getSdkConfig().getEx());
        }


    }

    public void getMessagesInGame(Activity activity, IMesssageListener messsageListener) {
        if (!GameConfigs.getInstance().isLogin()) {
            String mess = activity.getResources().getString(R.string.lbl_not_login);
            ToastUtils.showShortToast(activity, mess);
            return;
        }
        IGamePresenter gamePresenter = new GamePresenterImpl(new BaseView() {
            @Override
            public void showProgress(String mess) {
                Utils.showLoading(activity, true);
            }

            @Override
            public void hideProgress() {
                Utils.showLoading(activity, false);
            }

            @Override
            public void success(Object x) {
                if (x instanceof MessInGameResponseObj && messsageListener != null) {
                    ArrayList<MessInGameObj> obj = ((MessInGameResponseObj) x).getData();
                    if (obj != null && obj.size() > 0)
                        messsageListener.onSuccess(obj);
                }

            }

            @Override
            public void error(Object o) {
                BaseObj baseObj = (BaseObj) o;
                ToastUtils.showShortToast(activity, baseObj.getMessage());
            }
        });
        gamePresenter.getMessagesInGame();
    }


    @Override
    public void saveCharacter(Activity activity, String roleId, String areaId, ISaveCharactorListener listener) {
        if (!GameConfigs.getInstance().isLogin()) {
            String mess = activity.getResources().getString(R.string.lbl_not_login);
            ToastUtils.showShortToast(activity, mess);
            return;
        }
        PrefManager.saveSetting(activity, ROLE_ID, roleId);
        PrefManager.saveSetting(activity, AREA_ID, areaId);

        LogUtils.d("saveCharacter", "ROLE_ID :" + roleId + " - AREA_ID :" + areaId);

        IGamePresenter gamePresenter = new GamePresenterImpl(new BaseView() {
            @Override
            public void showProgress(String mess) {
                Utils.showLoading(activity, true);
            }

            @Override
            public void hideProgress() {
                Utils.showLoading(activity, false);
            }

            @Override
            public void success(Object x) {
                if (x instanceof BaseObj && listener != null) {
                    CheShaoTracking.getInstance().trackCharactorCreatedSuccess();
                    SdkConfigObj.Pop pop = GameConfigs.getInstance().getPopup();
                    if (pop != null && !TextUtils.isEmpty(pop.getUrl())) {
                        CheShaoSDK.getInstance().getQueuePopups().add(CheShaoSDK.POPUP_LINK);
                        CheShaoSDK.getInstance().showPopup();
                    }
                    listener.onSuccess();
                }
            }

            @Override
            public void error(Object o) {
                BaseObj baseObj = (BaseObj) o;
                ToastUtils.showShortToast(activity, baseObj.getMessage());
                CheShaoTracking.getInstance().trackCharactorCreatedFailed();
            }
        });
        CheShaoTracking.getInstance().trackCharactorCreated();

        try {
            gamePresenter.saveCharactor(roleId, areaId);
        } catch (Exception e) {
            LogUtils.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (cheShaoSDK != null)
            cheShaoSDK.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (cheShaoSDK != null) {
            timerAsync.cancel();
            cheShaoSDK.onDestroySDK();
        }
    }

    @Override
    public void shareImageToFacebook(Activity activity, Bitmap image) {
        if (cheShaoSDK != null)
            cheShaoSDK.shareImageToFacebook(activity, image);
    }

    @Override
    public void showTextScroll(ArrayList<MessInGameObj> listNoti) {
        if (cheShaoSDK != null)
            cheShaoSDK.showTextScroll(listNoti);
    }

    @Override
    public void onRequestPermissionsResult(Activity gameActivity, int requestCode, String[] permissions, int[] grantResults) {
        if (cheShaoSDK != null)
            cheShaoSDK.onRequestPermissionsResult(gameActivity, requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (cheShaoSDK != null)
            cheShaoSDK.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        cheShaoSDK.onBackPressed();
    }

    DialogFragment dashboard;
    @Override
    public void showDashBoard(Activity activity) {
        if (!GameConfigs.getInstance().isLogin()) {
            String mess = activity.getResources().getString(R.string.lbl_not_login);
            ToastUtils.showShortToast(activity, mess);
        } else {
            dashboard = (DialogFragment) activity.getFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
            if (dashboard == null)
                dashboard = DashboardFragment.newInstance();
            dashboard.setCancelable(true);
            if (dashboard.getDialog() != null && dashboard.getDialog().isShowing())
                return;
            dashboard.show(activity.getFragmentManager(), DashboardFragment.class.getSimpleName());
        }
    }

    DialogFragment paymentNativeFragment;


    @Override
    public void payment(Activity activity, String state, IPaymentListener listener) {

        if (mHandler != null && mRunable != null) {
            mHandler.removeCallbacks(mRunable);
        }
        mRunable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "payment:" + state);

                    if (!GameConfigs.getInstance().isLogin()) {
                        String mess = activity.getResources().getString(R.string.lbl_not_login);
                        ToastUtils.showShortToast(activity, mess);
                    } else {
                        String role_id = PrefManager.getString(activity, ROLE_ID, "");
                        String area_id = PrefManager.getString(activity, AREA_ID, "");
                        if (TextUtils.isEmpty(role_id) || TextUtils.isEmpty(area_id)) {
                            ToastUtils.showShortToast(activity, activity.getString(R.string.err_empty_charactor));
                            return;
                        }

                        paymentNativeFragment = (DialogFragment) activity.getFragmentManager().findFragmentByTag(PaymentNativeFragment.class.getSimpleName());

                        if (paymentNativeFragment == null) {
                            paymentNativeFragment = PaymentNativeFragment.newInstance(state, new PaymentNativeFragment.PaymentSuccess() {
                                @Override
                                public void onIapPaymentSuccess(PurchaseHistoryObj obj) {
                                    CmdPaymentV3.getInstance().verifyPayment(activity, obj, listener);
                                }

                                @Override
                                public void onLocalPaymentSuccess(VerifyPurchaseObj verifyPurchaseObj) {
                                    listener.onPaymentSuccess(verifyPurchaseObj);
                                }
                            });
                        }
                        if (paymentNativeFragment.getDialog() != null && paymentNativeFragment.getDialog().isShowing())
                            return;
                        paymentNativeFragment.show(activity.getFragmentManager(), PaymentNativeFragment.class.getSimpleName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mHandler.postDelayed(mRunable, 200);
    }

    public void checkIpRequest(Activity activity) {
        dialogFragment = (BlockFragment) activity.getFragmentManager().findFragmentByTag(BlockFragment.class.getSimpleName());
        if (dialogFragment == null) {
            dialogFragment = BlockFragment.newInstance(new IBlockIpListener() {
                @Override
                public void hideBlock() {
                    if (dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing()) {
                        dialogFragment.dismiss();
                    }

                }
            });
        }
        dialogFragment.setCancelable(true);
        dialogFragment.show(activity.getFragmentManager(), BlockFragment.class.getSimpleName());

    }

    public void saveFCM(Activity activity) {
        if (GameConfigs.getInstance().isLogin() && !PrefManager.getBoolean(sApp, SAVE_FCM_NON_TOKEN + AuthenConfigs.getInstance().getAccessToken(), false)) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String firebaseToken = instanceIdResult.getToken();
                   LogUtils.d(TAG, "Firebase Token/regId: " + firebaseToken);

                    if (GameConfigs.getInstance().getAppKey() != null && GameConfigs.getInstance().getUser() != null) {
                        int userId = GameConfigs.getInstance().getUser().getId();
                        PachiaFirebaseMessagingService.getInstance().sendRegistrationToServer(activity, String.valueOf(userId), firebaseToken);
                    }
                }
            });
        } else if (!PrefManager.getBoolean(sApp, SAVE_FCM_NON_TOKEN, false)) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String firebaseToken = instanceIdResult.getToken();
                   LogUtils.d(TAG, "Firebase Token/regId: " + firebaseToken);
                    if (GameConfigs.getInstance().getAppKey() != null) {
                        PachiaFirebaseMessagingService.getInstance().sendRegistrationToServer(activity, "", firebaseToken);
                    }
                }
            });

        }
    }

    public void trackCdnStart(Activity activity, String url, Header[] header) {
        
        LogUtils.d("CDN_TRACKING", "Start url : " + url);
        LogUtils.d("CDN_TRACKING", "Time : " + System.currentTimeMillis());
        for (Header h : header) {
           LogUtils.d("CDN_TRACKING", "header name : " + h.getName() + " - header value : " + h.getValue());
        }
    }

    public void trackCdnFinish(Activity activity, int Code) {
        LogUtils.d("CDN_TRACKING", Code == 1 ? "Fail" : "Success");
        LogUtils.d("CDN_TRACKING", "Finish Time : " + System.currentTimeMillis());
    }
}
