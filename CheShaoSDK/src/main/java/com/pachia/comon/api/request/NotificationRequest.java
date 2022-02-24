package com.pachia.comon.api.request;

import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.object.request.SaveFcmRequestObj;
import com.pachia.comon.object.response.SaveFcmResponseObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by dungnv
 */
public interface NotificationRequest {
    @Headers({"Accept:application/json"})
    @POST(ConstantApi.URL_SAVE_FCM)
    Call<SaveFcmResponseObj> saveFCM(@Body SaveFcmRequestObj body);

}
