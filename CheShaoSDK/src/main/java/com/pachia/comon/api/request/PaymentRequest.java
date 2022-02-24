package com.pachia.comon.api.request;

import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.object.request.InitalizePuchaseRequestObj;
import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.pachia.comon.object.response.InitialPurchaseResponseObj;
import com.pachia.comon.object.response.ItemPayResponseObj;
import com.pachia.comon.object.response.RetryPaymentResponseObj;
import com.pachia.comon.object.response.VerifyPurchaseResponseObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaymentRequest {

    @Headers({"Content-Type: application/json","Accept:application/json"})
    @POST(ConstantApi.URL_VERIFY_PURCHASE)
    Call<VerifyPurchaseResponseObj> verifyPurchase(@Path("order_no") String order_no,
                                                   @Body VerifyPurchaseRequestObj body);



    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept:application/json"})
    @GET(ConstantApi.URL_GET_LIST_ITEM)
    Call<ItemPayResponseObj> getListItem(
            @Query("method") String method,
            @Query("x_request") String x_request,
            @Query("app_version") String app_version,
            @Query("sdk_version") String sdk_version,
            @Query("account_id") String account_id,
            @Query("character_id") String role_id,
            @Query("server_id") String area_id
    );

    @Headers({"Content-Type: application/json", "Accept:application/json"})
    @POST(ConstantApi.URL_INITIAL_PURCHASE)
    Call<InitialPurchaseResponseObj> initialPurchase(@Body InitalizePuchaseRequestObj body);


    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST(ConstantApi.URL_RETRY_PURCHASE)
    Call<RetryPaymentResponseObj> retryPayment(@Field("app_key") String appKey,
                                               @Field("app_version") String app_version,
                                               @Field("user_id") String user_id,
                                               @Field("character_id") String character_id,
                                               @Field("area_id") String areaId,
                                               @Field("package_name") String packageName,
                                               @Field("purchase_token") String purchaseToken,
                                               @Field("product_id") String productId);
}
