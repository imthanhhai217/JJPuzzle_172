package com.pachia.ui.login;

import android.app.Activity;

import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.object.response.AuthenConfigResponseObj;
import com.pachia.comon.object.response.LoginEmailResponseObj;
import com.pachia.comon.object.response.LoginFacebookResponseObj;
import com.pachia.comon.object.response.LoginGoogleResponseObj;
import com.pachia.comon.object.response.LoginPlayNowResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.presenter.InteractorCallback;

public class LoginPresenterImpl implements ILoginPresenter {

    private ILoginInteractor mInteractor;
    private BaseView mBaseView;
    private static Activity activity;

    public LoginPresenterImpl(BaseView mBaseView) {
        this.mBaseView = mBaseView;
        mInteractor = new LoginInteractorImpl(mLoginCallback);
    }

    private final InteractorCallback<Object> mLoginCallback = new InteractorCallback<Object>() {
        @Override
        public void success(Object x) {
            if (mBaseView != null) {
                if ((x instanceof LoginFacebookResponseObj) || (x instanceof LoginGoogleResponseObj) || (x instanceof LoginEmailResponseObj) || (x instanceof LoginPlayNowResponseObj)) {
                } else
                    mBaseView.hideProgress();
                mBaseView.success(x);

            }
        }

        @Override
        public void error(Object o) {
            if (mBaseView != null) {
                mBaseView.hideProgress();
                mBaseView.error(o);
            }


        }
    };

    @Override
    public void cancelRequest(String... tags) {
        mInteractor.cancelRequest();
    }

    @Override
    public void loginEmail(String user, String pass) {
        mInteractor.cancelRequest();
        mInteractor.loginEmail(user, pass);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }


    @Override
    public void loginFtFacebook(String token) {
        mInteractor.cancelRequest();
        mInteractor.loginFtFacebook(token);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void loginFtGoogle(String token) {
        mInteractor.cancelRequest();
        mInteractor.loginFtGoogle(token);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void loginPlayNow() {
        mInteractor.cancelRequest();
        mInteractor.loginPlayNow();
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void getAuthenConfig() {
        if (AuthenConfigs.getInstance().getAuthenConfigObj() != null) {
            AuthenConfigResponseObj obj = new AuthenConfigResponseObj();
            obj.setData(AuthenConfigs.getInstance().getAuthenConfigObj());
            mBaseView.success(obj);
            return;
        }
        mInteractor.cancelRequest();
        mInteractor.getAuthenConfig();
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void getUser() {
        mInteractor.cancelRequest();
        mInteractor.getUser();
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void getSdkConfig() {
//        PrefManager.saveLong(Lovely.getInstance().getApplication(), ConstantTrackEvent.START_CALL_GET_SDK_CONFIG, System.currentTimeMillis());
        mInteractor.cancelRequest();
        mInteractor.getSdkConfig();
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }


}
