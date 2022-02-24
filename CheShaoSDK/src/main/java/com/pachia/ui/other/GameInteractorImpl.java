package com.pachia.ui.other;


import com.pachia.comon.api.ApiUtils;
import com.pachia.comon.api.MyCallback;
import com.pachia.comon.api.request.GameRequest;
import com.pachia.comon.api.request.NotificationRequest;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.err.LoginEmailErrObj;
import com.pachia.comon.object.request.SaveFcmRequestObj;
import com.pachia.comon.object.response.MessInGameResponseObj;
import com.pachia.comon.object.response.SaveFcmResponseObj;
import com.pachia.comon.presenter.InteractorCallback;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;

import retrofit2.Call;
import retrofit2.Response;

public class GameInteractorImpl implements IGameInteractor {
    private static final String TAG = GameInteractorImpl.class.getName();

    private InteractorCallback mCallback;

    private Call<MessInGameResponseObj> mMessInGameRequest;
    private Call<BaseObj> saveCharactorRequest;
    private Call<SaveFcmResponseObj> saveFCM;
    private Call<BaseObj> connectFacebook;


    public GameInteractorImpl(InteractorCallback mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public void cancelRequest(String... tags) {
        if (mMessInGameRequest != null)
            mMessInGameRequest.cancel();
        if (saveCharactorRequest != null)
            saveCharactorRequest.cancel();
    }


    @Override
    public void getMessagesInGame() {
        GameRequest gameRequest = ApiUtils.getGameRequest();
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        mMessInGameRequest = gameRequest.getMessagesInGame(appKey, appVer);
        mMessInGameRequest.enqueue(callbackMessInGame);
    }

    @Override
    public void saveCharactor(String roleId, String areaId) {
        GameRequest gameRequest = ApiUtils.getGameRequest();
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String deviceOs = DeviceUtils.getOSInfo();
        String deviceName = DeviceUtils.getDevice();
        String deviceNetwork = Utils.getNetwork(CheShaoSdk.getInstance().getApplication());
        saveCharactorRequest = gameRequest.saveCharactor(appKey, appVer, roleId, areaId, deviceOs, deviceNetwork, deviceName);
        saveCharactorRequest.enqueue(callbackSaveCharactor);
    }

    @Override
    public void saveFCM(String userId, String token)  {
        NotificationRequest request = ApiUtils.getNotificationRequest();
        SaveFcmRequestObj obj = new SaveFcmRequestObj();
        obj.setRegid(token);
        obj.setUser_id(userId);
        saveFCM = request.saveFCM(obj);
        saveFCM.enqueue(callbackFCM);
    }

    @Override
    public void connectFaceBook(String token) {
        GameRequest gameRequest = ApiUtils.getGameRequest();
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        connectFacebook = gameRequest.connectFacebook(appKey, appVer, token);
        connectFacebook.enqueue(callbackConnectFB);
    }

    private MyCallback<BaseObj> callbackSaveCharactor = new MyCallback<BaseObj>() {
        @Override
        public void onSuccess(Call<BaseObj> call, Response<BaseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<BaseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginEmailErrObj err = new LoginEmailErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<SaveFcmResponseObj> callbackFCM = new MyCallback<SaveFcmResponseObj>() {
        @Override
        public void onSuccess(Call<SaveFcmResponseObj> call, Response<SaveFcmResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<SaveFcmResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginEmailErrObj err = new LoginEmailErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<MessInGameResponseObj> callbackMessInGame = new MyCallback<MessInGameResponseObj>() {
        @Override
        public void onSuccess(Call<MessInGameResponseObj> call, Response<MessInGameResponseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<MessInGameResponseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            LoginEmailErrObj err = new LoginEmailErrObj();
            err.setMessage(obj.getMessage());
            err.setStatus(obj.getStatus());
            mCallback.error(err);
        }
    };

    private MyCallback<BaseObj> callbackConnectFB = new MyCallback<BaseObj>() {
        @Override
        public void onSuccess(Call<BaseObj> call, Response<BaseObj> response) {
            if (response.isSuccessful()) {
                mCallback.success(response.body());
            }
        }

        @Override
        public void onError(Call<BaseObj> call, Object object) {
            BaseObj obj = (BaseObj) object;
            mCallback.error(obj);
        }
    };
}
