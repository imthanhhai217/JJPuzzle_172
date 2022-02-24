package com.pachia.comon.game;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.appsflyer.AppsFlyerLib;
import com.pachia.comon.api.ErrorCode;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.object.UserObj;
import com.pachia.comon.object.err.SdkConfigErrObj;
import com.pachia.comon.object.err.UserErrObj;
import com.pachia.comon.object.response.SdkConfigResponseObj;
import com.pachia.comon.object.response.UserResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.ui.login.ILoginPresenter;
import com.pachia.ui.login.LoginPresenterImpl;
import com.quby.R;
import com.supho.CheShaoSDK;


public class LifeCycleActivity implements BaseView {
    public static LifeCycleActivity lovelyActivity;
    Activity activity;
    boolean isFirstLauch;
    ILoginPresenter loginPresenter;

    public static LifeCycleActivity getInstance(Activity activity) {
        if (lovelyActivity == null)
            lovelyActivity = new LifeCycleActivity(activity);
        return lovelyActivity;
    }

    public LifeCycleActivity(Activity activity) {
        isFirstLauch = true;
        this.activity = activity;
        loginPresenter = new LoginPresenterImpl(this);
    }


    public void onCreate() {
        if (GameConfigs.getInstance().getUser() != null && GameConfigs.getInstance().isLogin()) {
            loginPresenter.getUser();
        } else {
            isFirstLauch = false;
        }
    }

    public void onStart() {
    }


    public void onResume() {
        if (isFirstLauch) {
            return;
        }
        if (!TextUtils.isEmpty(PrefManager.getAccessToken(activity))) {
            loginPresenter = new LoginPresenterImpl(this);
            loginPresenter.getUser();
        }
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroy() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onLowMemory() {
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    }


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
        if (x instanceof UserResponseObj) {
            UserObj obj = ((UserResponseObj) x).getData();
            GameConfigs.getInstance().setUser(obj);
            if (isFirstLauch)
                loginPresenter.getSdkConfig();
            LogUtils.d("LifeCycleActivity", "User valid");
        } else if (x instanceof SdkConfigResponseObj) {
            try {
                isFirstLauch = false;
                SdkConfigResponseObj obj = (SdkConfigResponseObj) x;
                GameConfigs.getInstance().setSdkConfig(obj.getData());
                if (obj != null && obj.getData() != null && obj.getData().getMaintenance() != null && !TextUtils.isEmpty(obj.getData().getMaintenance().getUrl())) {
                    CheShaoTracking.getInstance().trackMaintainScreenOpened();
                    CheShaoSDK.getInstance().getQueuePopups().add(CheShaoSDK.POPUP_MAINTAIN);
                    CheShaoSDK.getInstance().showPopup();
                    return;
                }
                AppsFlyerLib.getInstance().setCustomerUserId(GameConfigs.getInstance().getUser().getId() + "");
                CheShaoSdk.getInstance().onLoginSuccess(activity, "login");
                LogUtils.d("LifeCycleActivity", "get game info success");
            } catch (Exception e) {
                LogUtils.d("LifeCycleActivity", e.getMessage());
            }
        }
    }

    @Override
    public void error(Object o) {
        if (o instanceof UserErrObj) {
            UserErrObj obj = (UserErrObj) o;
            if (obj.getStatus() == Constants.USER_ERR_CODE.INVALID_TOKEN) {
                DialogUtils.showExpireDialog(activity);
            } else {
                if (obj.getStatus() == ErrorCode.NO_INTERNET)
                    ToastUtils.showShortToast(activity, activity.getString(R.string.error_network));
                DialogUtils.showRetryDialog(activity, obj.getMessage(), new DialogUtils.Listener() {
                    @Override
                    public void onRetry() {
                        onCreate();
                    }
                });
            }
        } else if (o instanceof SdkConfigErrObj) {
            SdkConfigErrObj obj = (SdkConfigErrObj) o;
            if (obj.getStatus() == Constants.USER_ERR_CODE.INVALID_TOKEN) {
                DialogUtils.showExpireDialog(activity);
            } else {
                if (obj.getStatus() == ErrorCode.NO_INTERNET) {
                    DialogUtils.showRetryDialog(activity, activity.getString(R.string.error_network), new DialogUtils.Listener() {
                        @Override
                        public void onRetry() {
                            ILoginPresenter loginPresenter = new LoginPresenterImpl(LifeCycleActivity.this);
                            loginPresenter.getSdkConfig();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialogWithLogOut(activity, obj.getMessage(), new DialogUtils.Listener() {
                        @Override
                        public void onRetry() {
                            ILoginPresenter loginPresenter = new LoginPresenterImpl(LifeCycleActivity.this);
                            loginPresenter.getSdkConfig();
                        }
                    });
                }
            }
        }
    }
}
