package com.pachia.comon.api.request;


import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.object.response.LoginEmailResponseObj;
import com.pachia.comon.object.response.LoginFacebookResponseObj;
import com.pachia.comon.object.response.LoginGoogleResponseObj;
import com.pachia.comon.object.response.LoginPlayNowResponseObj;
import com.pachia.comon.object.response.UserResponseObj;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dungnv
 */

public interface LoginRequest {


//    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
//    @POST(ConstantApi.URL_LOGIN)
//    Call<LoginEmailResponseObj> loginEmail(@Body LoginEmailRequestObj body);

    @Headers({"Accept:application/json"})
    @GET(ConstantApi.URL_GET_USER)
    Call<UserResponseObj> getUser(@Path("app_key") String appKey,
                                  @Path("app_version") String app_version,
                                  @Query("device_os") String device_os,
                                  @Query("device_name") String device_name,
                                  @Query("device_resolution") String device_resolution,
                                  @Query("device_network") String device_network,
                                  @Query("sdk_version") String sdk_version

    );

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST(ConstantApi.URL_LOGIN_PLAYNOW)
    Call<LoginPlayNowResponseObj> loginPlayNow(@Path("app_key") String appKey,
                                               @Path("app_version") String app_version,
                                               @Field("device_id") String device_id,
                                               @Field("device_os") String device_os,
                                               @Field("device_name") String device_name,
                                               @Field("device_resolution") String device_resolution,
                                               @Field("device_network") String device_network,
                                               @Field("sdk_version") String sdk_version,
                                               @Field("appsflyer_id") String appsflyer_id,
                                               @Field("advertising_id") String advertising_id);

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
    @POST(ConstantApi.URL_LOGIN_EMAIL)
    Call<LoginEmailResponseObj> loginEmail(@Path("app_key") String appKey,
                                           @Path("app_version") String app_version,
                                           @Field("email") String email,
                                           @Field("password") String password,
                                           @Field("device_os") String device_os,
                                           @Field("device_name") String device_name,
                                           @Field("device_resolution") String device_resolution,
                                           @Field("device_network") String device_network,
                                           @Field("sdk_version") String sdk_version,
                                           @Field("appsflyer_id") String appsflyer_id,
                                           @Field("advertising_id") String advertising_id
    );

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
    @POST(ConstantApi.URL_LOGIN_FACEBOOK)
    Call<LoginFacebookResponseObj> loginFacebook(@Path("app_key") String appKey,
                                                 @Path("app_version") String app_version,
                                                 @Field("fb_token") String fb_token,
                                                 @Field("device_os") String device_os,
                                                 @Field("device_name") String device_name,
                                                 @Field("device_resolution") String device_resolution,
                                                 @Field("device_network") String device_network,
                                                 @Field("sdk_version") String sdk_version,
                                                 @Field("appsflyer_id") String appsflyer_id,
                                                 @Field("advertising_id") String advertising_id
    );

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
    @POST(ConstantApi.URL_LOGIN_GOOGLE)
    Call<LoginGoogleResponseObj> loginGoogle(@Path("app_key") String appKey,
                                             @Path("app_version") String app_version,
                                             @Field("token") String gg_token,
                                             @Field("device_os") String device_os,
                                             @Field("device_name") String device_name,
                                             @Field("device_resolution") String device_resolution,
                                             @Field("device_network") String device_network,
                                             @Field("sdk_version") String sdk_version,
                                             @Field("appsflyer_id") String appsflyer_id,
                                             @Field("advertising_id") String advertising_id
    );


}
