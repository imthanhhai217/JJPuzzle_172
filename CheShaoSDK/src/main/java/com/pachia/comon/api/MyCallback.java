package com.pachia.comon.api;

import androidx.annotation.NonNull;

import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dungnv
 */

public abstract class MyCallback<T extends BaseObj> implements Callback<T> {
    public static final String TAG = MyCallback.class.getSimpleName();

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        if (!response.isSuccessful() && response.errorBody() != null && response.code() == 200) {
            String errorMessage = null;
            try {
                Gson gson = new Gson();
                errorMessage = response.errorBody().string();
                BaseObj apiErrorObj = gson.fromJson(errorMessage, BaseObj.class);
                apiErrorObj.setStatus(response.code());
                onError(call, apiErrorObj);
            } catch (IOException e) {
                BaseObj apiErrorObj = new BaseObj();
                apiErrorObj.setStatus(response.code());
                apiErrorObj.setMessage(e.getMessage());
                onError(call, apiErrorObj);
                LogUtils.printStackTrace(e);
            } catch (Exception e) {
                BaseObj apiErrorObj = new BaseObj();
                apiErrorObj.setStatus(response.code());
                apiErrorObj.setMessage(e.getMessage());
                BaseObj.createError("server error", apiErrorObj.getStatus() + "=> " + errorMessage);
                onError(call, apiErrorObj);
                LogUtils.printStackTrace(e);
            }
            return;
        }

        T t = response.body();
        if (t == null) {
            //NO DATA
            BaseObj apiErrorObj = new BaseObj();
            try {
                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                if (jObjError.has("message")) {
                    apiErrorObj.setMessage(jObjError.getString("message"));
                }
            } catch (Exception e) {
                apiErrorObj.setMessage(response.message());
            }
            apiErrorObj.setStatus(response.code());

            BaseObj.createError("null", "No data");
            LogUtils.e(TAG, "Error4: ======> " + apiErrorObj.getStatus());
            onError(call, apiErrorObj);
            return;
        }
        if (t instanceof BaseObj & response.code() == 200)
            onSuccess(call, response);
        else {
            BaseObj apiErrorObj = new BaseObj();
            apiErrorObj.setMessage(response.message());
            apiErrorObj.setStatus(response.code());
            onError(call, apiErrorObj);
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, Throwable t) {
        if (t.getMessage() != null){
            LogUtils.e("Error113", t.getMessage());
        }
        BaseObj apiErrorObj = null;
        if (!NetworkUtils.isConnect(CheShaoSdk.getInstance().getApplication())) {
            apiErrorObj = BaseObj.createError("", "No Connect");
            apiErrorObj.setStatus(ErrorCode.NO_INTERNET);
            onError(call, apiErrorObj);
        } else if (!call.isCanceled()) {
            apiErrorObj = BaseObj.createError("", t.getMessage());
            onError(call, apiErrorObj);
        }
        if (apiErrorObj != null)
            LogUtils.e(TAG, "Error4: ======> " + apiErrorObj.getStatus());
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onError(Call<T> call, Object object);

}
