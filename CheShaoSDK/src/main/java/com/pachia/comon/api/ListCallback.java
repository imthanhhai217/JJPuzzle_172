package com.pachia.comon.api;

import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dungnv
 */

public abstract class ListCallback<T extends BaseObj> implements Callback<T> {
    public static final String TAG = ListCallback.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful() && response.errorBody() != null) {
            String errorMessage = null;
            try {
                Gson gson = new Gson();
                errorMessage = response.errorBody().string();
                BaseObj apiErrorObj = gson.fromJson(errorMessage, BaseObj.class);
                apiErrorObj.setStatus(response.code() );
                onError(call, apiErrorObj);
            } catch (IOException e) {
                BaseObj apiErrorObj = new BaseObj();
                apiErrorObj.setStatus(response.code() );
                onError(call, apiErrorObj);
                e.printStackTrace();
            } catch (Exception e) {
                BaseObj apiErrorObj = new BaseObj();
                apiErrorObj.setStatus(response.code() );
                if (response.code() >= 500) {
                    BaseObj.createError("server error", "unexpected error from server");
                    onError(call, apiErrorObj);
                } else {
                    BaseObj.createError("server error", apiErrorObj.getStatus() + "=> " + errorMessage);
                    onError(call, apiErrorObj);
                }
                e.printStackTrace();
            }
            return;
        }

        T t = response.body();
        if (t == null) {
            //NO DATA
            BaseObj apiErrorObj = new BaseObj();
            apiErrorObj.setStatus(response.code() );
            BaseObj.createError("null", "No data");

            onError(call, apiErrorObj);
            return;
        }
        LogUtils.d(TAG, "response : " + response.body());
        onSuccess(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        BaseObj apiErrorObj;
        if (!NetworkUtils.isConnect(CheShaoSdk.getInstance().getApplication())) {
            apiErrorObj = BaseObj.createError("", "No Connect");
            apiErrorObj.setStatus(ErrorCode.NO_INTERNET);
            onError(call, apiErrorObj);
        } else {
            apiErrorObj = BaseObj.createError("", t.getMessage());
            onError(call, apiErrorObj);
        }
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onError(Call<T> call, Object object);
}
