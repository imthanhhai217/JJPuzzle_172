package com.pachia.ui.login;


import com.pachia.comon.api.ApiUtils;
import com.pachia.comon.api.MyCallback;
import com.pachia.comon.api.request.GameRequest;
import com.pachia.comon.api.request.LoginRequest;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.err.AuthenConfigErrObj;
import com.pachia.comon.object.err.LoginEmailErrObj;
import com.pachia.comon.object.err.LoginFacebookErrObj;
import com.pachia.comon.object.err.LoginGGErrObj;
import com.pachia.comon.object.err.LoginPlayNowErrObj;
import com.pachia.comon.object.err.SdkConfigErrObj;
import com.pachia.comon.object.err.UserErrObj;
import com.pachia.comon.object.response.AuthenConfigResponseObj;
import com.pachia.comon.object.response.LoginEmailResponseObj;
import com.pachia.comon.object.response.LoginFacebookResponseObj;
import com.pachia.comon.object.response.LoginGoogleResponseObj;
import com.pachia.comon.object.response.LoginPlayNowResponseObj;
import com.pachia.comon.object.response.SdkConfigResponseObj;
import com.pachia.comon.object.response.UserResponseObj;
import com.pachia.comon.presenter.InteractorCallback;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class LoginInteractorImpl implements ILoginInteractor {
    private static final String TAG = LoginInteractorImpl.class.getName();

    private InteractorCallback mCallback;

    private Call<LoginEmailResponseObj> mLoginRequest;
    private Call<UserResponseObj> mUserRequest;
    private Call<AuthenConfigResponseObj> mAuthenRequest;
    private Call<LoginPlayNowResponseObj> mLoginPlayNowRequest;
    private Call<SdkConfigResponseObj> mSdkConfigRequest;
    private Call<LoginFacebookResponseObj> mLoginFacebookRequest;
    private Call<LoginGoogleResponseObj> mLoginGGRequest;

    public LoginInteractorImpl(InteractorCallback mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public void loginEmail(String user, String pass) {
        LoginRequest request = ApiUtils.getLoginRequest();
        String deviceOs = DeviceUtils.getOSInfo();
        String deviceName = DeviceUtils.getDevice();
        String deviceResolution = Objects.requireNonNull(DeviceUtils.getResolution(CheShaoSdk.getInstance().getApplication()));
        String deviceNetwork = Utils.getNetwork(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appfyerKey = DeviceUtils.getAppsflyerUID(CheShaoSdk.getInstance().getApplication());
        String advertising_id = DeviceUtils.getAdvertisingID(CheShaoSdk.getInstance().getApplication());
        mLoginRequest = request.loginEmail(appKey, appVer, user, pass, deviceOs, deviceName, deviceResolution, deviceNetwork, sdkVer, appfyerKey, advertising_id);
        mLoginRequest.enqueue(callbackLoginEmail);
    }

    @Override
    public void getAuthenConfig() {
        GameRequest request = ApiUtils.getGameRequest();
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        mAuthenRequest = request.getAuthenConfig(appKey, appVer);
        mAuthenRequest.enqueue(callbackAuthen);
    }

    @Override
    public void getUser() {
        LoginRequest request = ApiUtils.getLoginRequest();
        String deviceOs = DeviceUtils.getOSInfo();
        String deviceName = DeviceUtils.getDevice();
        String deviceResolution = Objects.requireNonNull(DeviceUtils.getResolution(CheShaoSdk.getInstance().getApplication()));
        String deviceNetwork = Utils.getNetwork(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        mUserRequest = request.getUser(appKey, appVer, deviceOs, deviceName, deviceResolution, deviceNetwork, sdkVer);
        mUserRequest.enqueue(callbackUser);
    }

    @Override
    public void loginPlayNow() {
        String deviceId = DeviceUtils.getUniqueDeviceID(CheShaoSdk.getInstance().getApplication());
        String deviceOs = DeviceUtils.getOSInfo();
        String deviceName = DeviceUtils.getDevice();
        String deviceResolution = Objects.requireNonNull(DeviceUtils.getResolution(CheShaoSdk.getInstance().getApplication()));
        String deviceNetwork = Utils.getNetwork(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());
        LoginRequest request = ApiUtils.getLoginRequest();
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appfyerKey = DeviceUtils.getAppsflyerUID(CheShaoSdk.getInstance().getApplication());
        String advertising_id = DeviceUtils.getAdvertisingID(CheShaoSdk.getInstance().getApplication());
        mLoginPlayNowRequest = request.loginPlayNow(appKey, appVer, deviceId, deviceOs, deviceName, deviceResolution, deviceNetwork, sdkVer, appfyerKey, advertising_id);
        mLoginPlayNowRequest.enqueue(callbackLoginPlayNow);
    }

    @Override
    public void loginFtFacebook(String token) {
        LoginRequest request = ApiUtils.getLoginRequest();
        String deviceOs = DeviceUtils.getOSInfo();
        String deviceName = DeviceUtils.getDevice();
        String deviceResolution = Objects.requireNonNull(DeviceUtils.getResolution(CheShaoSdk.getInstance().getApplication()));
        String deviceNetwork = Utils.getNetwork(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appfyerKey = DeviceUtils.getAppsflyerUID(CheShaoSdk.getInstance().getApplication());
        String advertising_id = DeviceUtils.getAdvertisingID(CheShaoSdk.getInstance().getApplication());
        mLoginFacebookRequest = request.loginFacebook(appKey, appVer, token, deviceOs, deviceName, deviceResolution, deviceNetwork, sdkVer, appfyerKey, advertising_id);
        mLoginFacebookRequest.enqueue(callbackLoginFacebook);
    }

    @Override
    public void loginFtGoogle(String token) {
        LoginRequest request = ApiUtils.getLoginRequest();
        String deviceOs = DeviceUtils.getOSInfo();
        String deviceName = DeviceUtils.getDevice();
        String deviceResolution = Objects.requireNonNull(DeviceUtils.getResolution(CheShaoSdk.getInstance().getApplication()));
        String deviceNetwork = Utils.getNetwork(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appfyerKey = DeviceUtils.getAppsflyerUID(CheShaoSdk.getInstance().getApplication());
        String advertising_id = DeviceUtils.getAdvertisingID(CheShaoSdk.getInstance().getApplication());
        mLoginGGRequest = request.loginGoogle(appKey, appVer, token, deviceOs, deviceName, deviceResolution, deviceNetwork, sdkVer, appfyerKey, advertising_id);
        mLoginGGRequest.enqueue(callbackLoginGG);
    }

    @Override
    public void getSdkConfig() {
        GameRequest request = ApiUtils.getGameRequest();
        String gameVersion = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        mSdkConfigRequest = request.getSdkConfig(appKey, gameVersion);
        mSdkConfigRequest.enqueue(callbackGetSdk);
    }

    @Override
    public void cancelRequest(String... tags) {
        if (mLoginRequest != null)
            mLoginRequest.cancel();
        if (mAuthenRequest != null)
            mAuthenRequest.cancel();
        if (mUserRequest != null)
            mUserRequest.cancel();
        if (mLoginPlayNowRequest != null)
            mLoginPlayNowRequest.cancel();
        if (mLoginFacebookRequest != null)
            mLoginFacebookRequest.cancel();
        if (mSdkConfigRequest != null)
            mSdkConfigRequest.cancel();
        if (mLoginGGRequest != null)
            mLoginGGRequest.cancel();
    }


    private MyCallback<LoginEmailResponseObj> callbackLoginEmail = new MyCallback<LoginEmailResponseObj>() {
        @Override
        public void onSuccess(Call<LoginEmailResponseObj> call, Response<LoginEmailResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<LoginEmailResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginEmailErrObj err = new LoginEmailErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<AuthenConfigResponseObj> callbackAuthen = new MyCallback<AuthenConfigResponseObj>() {
        @Override
        public void onSuccess(Call<AuthenConfigResponseObj> call, Response<AuthenConfigResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<AuthenConfigResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            AuthenConfigErrObj err = new AuthenConfigErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);

        }
    };

    private MyCallback<UserResponseObj> callbackUser = new MyCallback<UserResponseObj>() {
        @Override
        public void onSuccess(Call<UserResponseObj> call, Response<UserResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<UserResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            UserErrObj err = new UserErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<LoginPlayNowResponseObj> callbackLoginPlayNow = new MyCallback<LoginPlayNowResponseObj>() {
        @Override
        public void onSuccess(Call<LoginPlayNowResponseObj> call, Response<LoginPlayNowResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<LoginPlayNowResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginPlayNowErrObj err = new LoginPlayNowErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<SdkConfigResponseObj> callbackGetSdk = new MyCallback<SdkConfigResponseObj>() {
        @Override
        public void onSuccess(Call<SdkConfigResponseObj> call, Response<SdkConfigResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<SdkConfigResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            SdkConfigErrObj err = new SdkConfigErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<LoginFacebookResponseObj> callbackLoginFacebook = new MyCallback<LoginFacebookResponseObj>() {
        @Override
        public void onSuccess(Call<LoginFacebookResponseObj> call, Response<LoginFacebookResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<LoginFacebookResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginFacebookErrObj err = new LoginFacebookErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };
    private MyCallback<LoginGoogleResponseObj> callbackLoginGG = new MyCallback<LoginGoogleResponseObj>() {
        @Override
        public void onSuccess(Call<LoginGoogleResponseObj> call, Response<LoginGoogleResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<LoginGoogleResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginGGErrObj err = new LoginGGErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };
}
