package com.pachia.comon.api.request;

import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.response.AuthenConfigResponseObj;
import com.pachia.comon.object.response.MessInGameResponseObj;
import com.pachia.comon.object.response.SdkConfigResponseObj;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by dungnv
 */
public interface GameRequest {

    @Headers({"Accept:application/json"})
    @GET(ConstantApi.URL_GET_AUTH_CONFIG)
    Call<AuthenConfigResponseObj> getAuthenConfig(@Path("app_key") String appKey,
                                                  @Path("app_version") String appVersion);

    @Headers({"Accept:application/json"})
    @GET(ConstantApi.URL_GET_SDK_CONFIG)
    Call<SdkConfigResponseObj> getSdkConfig(@Path("app_key") String appKey,
                                            @Path("app_version") String appVersion);

    @Headers({"Accept:application/json"})
    @GET(ConstantApi.URL_GET_MESS_IN_GAME)
    Call<MessInGameResponseObj> getMessagesInGame(@Path("app_key") String appKey, @Path("app_version") String appVersion);

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
    @POST(ConstantApi.URL_SAVE_CHARACTOR)
    Call<BaseObj> saveCharactor(@Path("app_key") String appKey,
                                @Path("app_version") String app_version,
                                @Field("role_id") String role_id,
                                @Field("area_id") String area_id,
                                @Field("device_os") String device_os,
                                @Field("device_network") String device_network,
                                @Field("device_name") String device_name
    );


    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
    @POST(ConstantApi.URL_CONNECT_FACEBOOK)
    Call<BaseObj> connectFacebook(@Path("app_key") String appKey,
                                  @Path("app_version") String app_version,
                                  @Field("fb_token") String device_os);



}
