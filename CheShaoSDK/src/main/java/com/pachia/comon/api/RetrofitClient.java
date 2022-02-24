package com.pachia.comon.api;

import android.content.Context;
import android.text.TextUtils;

import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dungnv
 */

public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static Retrofit retrofit = null;
    private static final int CONNECT_TIMEOUT = 10 * 1000; // MILISECONDS
    private static String baseUrl;

    private RetrofitClient() {
    }

    public static void clearInstant() {
        retrofit = null;
    }

    public static Retrofit getInstance() {

        if (retrofit == null) {
            Context context = CheShaoSdk.getInstance().getApplication();
            baseUrl = PrefManager.getString(context, ConstantApi.APP_UR, ConstantApi.BASE_URL);
            if (!baseUrl.endsWith("/"))
                baseUrl = ConstantApi.BASE_URL;
            if (!(baseUrl.startsWith("http:") || baseUrl.startsWith("https:"))) {
                baseUrl = ConstantApi.BASE_URL;
            }
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(1);

            Interceptor interceptorRequest = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();
                    // base header
                    builder.header("Accept", "application/json");

                    String token = AuthenConfigs.getInstance().getAccessToken();
                    String appKey = GameConfigs.getInstance().getAppKey();
//                    String adsKey = GameConfigs.getInstance().getAdsKey();
                    String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
                    String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());

                    if (token != null && !TextUtils.isEmpty(token))
                        builder.addHeader(ConstantApi.HEADER_ATHORIZATION, "Bearer " + token);
                    if (appKey != null && !TextUtils.isEmpty(appKey))
                        builder.addHeader(ConstantApi.HEADER_APP_KEY, appKey);
                    if (appVer != null && !TextUtils.isEmpty(appVer))
                        builder.addHeader(ConstantApi.HEADER_APP_VERSION, appVer);
                    if (sdkVer != null && !TextUtils.isEmpty(sdkVer))
                        builder.addHeader(ConstantApi.HEADER_SDK_VERSION, sdkVer);

                    // base parameter
                    HttpUrl originalHttpUrl = original.url();
                    HttpUrl.Builder httpBuilder = originalHttpUrl.newBuilder();

//                    if (companyId != -1) {
//                        httpBuilder.addQueryParameter("company_id", String.valueOf(companyId));
//                    }

                    //=================
                    HttpUrl url = httpBuilder.build();
                    Request request = builder.method(original.method(), original.body())
                            .url(url)
                            .build();
                    return chain.proceed(request);
                }
            };

            OkHttpClient okClient =
                    new OkHttpClient.Builder()
                            .addInterceptor(interceptorRequest)
                            .connectTimeout(CONNECT_TIMEOUT,
                                    TimeUnit.MILLISECONDS)
                            .readTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .writeTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .dispatcher(dispatcher)
                            .build();

            try {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                retrofit = new Retrofit.Builder()
                        .client(getUnsafeOkHttpClient().build())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(baseUrl).client(okClient).build();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                ToastUtils.showShortToast(CheShaoSdk.getInstance().getApplication(), "Url is Wrong");
            }
        }

        return retrofit;
    }


    public static Retrofit getInteractInstance(String baseUrl) {
        if (!(baseUrl.startsWith("http:") || baseUrl.startsWith("https:"))) {
            return null;
        }
        Retrofit retrofit = null;
        Context context = CheShaoSdk.getInstance().getApplication();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

        Interceptor interceptorRequest = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                // base header
                builder.header("Accept", "application/json");

                String fcmApiKey = GameConfigs.getInstance().getFcmApiKey();
                String appKey = GameConfigs.getInstance().getAppKey();
                String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
                String sdkVer = Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication());
                String sdl_locale = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.APP_LANG, "en");
                String deviceOs = DeviceUtils.getOSInfo();
                String deviceName = DeviceUtils.getDevice();

                // TODO: 8/10/2020 what x-autho
                builder.addHeader(ConstantApi.HEADER_X_AUTHIRIZATION, fcmApiKey == null ? "" : fcmApiKey);
                builder.addHeader(ConstantApi.HEADER_SDK_LOCATLE, DeviceUtils.getLanguage());
                builder.addHeader(ConstantApi.HEADER_APP_KEY, appKey == null ? "" : appKey);
                builder.addHeader(ConstantApi.HEADER_APP_VERSION, appVer == null ? "" : appVer);
                builder.addHeader(ConstantApi.HEADER_SDK_VERSION, sdkVer == null ? "" : sdkVer);
                builder.addHeader(ConstantApi.HEADER_DEVICE_OS, deviceOs == null ? "" : deviceOs);
                builder.addHeader(ConstantApi.HEADER_ADVERTISING_ID, DeviceUtils.getAdvertisingID(context));
                builder.addHeader(ConstantApi.HEADER_DEVICE_NAME, deviceName == null ? "" : deviceName);
                builder.addHeader(ConstantApi.HEADER_SDK_LOCATLE, sdl_locale);

                // base parameter
                HttpUrl originalHttpUrl = original.url();
                HttpUrl.Builder httpBuilder = originalHttpUrl.newBuilder();
                //=================
                HttpUrl url = httpBuilder.build();
                Request request = builder.method(original.method(), original.body())
                        .url(url)
                        .build();
                return chain.proceed(request);
            }
        };
        OkHttpClient okClient =
                new OkHttpClient.Builder()
                        .addInterceptor(interceptorRequest)
                        .connectTimeout(CONNECT_TIMEOUT,
                                TimeUnit.MILLISECONDS)
                        .readTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .writeTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .dispatcher(dispatcher)
                        .build();

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .client(getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(baseUrl).client(okClient).build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ToastUtils.showShortToast(CheShaoSdk.getInstance().getApplication(), "Url is Wrong");
        }

        return retrofit;
    }

    public static Retrofit getPaymentInstance(String baseUrl) {
        if (!(baseUrl.startsWith("http:") || baseUrl.startsWith("https:"))) {
            return null;
        }
        Retrofit retrofit = null;
        Context context = CheShaoSdk.getInstance().getApplication();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

        Interceptor interceptorRequest = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                // base header
                builder.header("Accept", "application/json");
                String accessToken = AuthenConfigs.getInstance().getAccessToken();

                // TODO: 8/10/2020 what x-autho
                builder.addHeader(ConstantApi.HEADER_ACCESS_TOKEN, accessToken == null ? "" : accessToken);
                builder.addHeader(ConstantApi.HEADER_ATHORIZATION, accessToken == null ? "" : accessToken);


                // base parameter
                HttpUrl originalHttpUrl = original.url();
                HttpUrl.Builder httpBuilder = originalHttpUrl.newBuilder();
                //=================
                HttpUrl url = httpBuilder.build();
                Request request = builder.method(original.method(), original.body())
                        .url(url)
                        .build();
                return chain.proceed(request);
            }
        };
        OkHttpClient okClient =
                new OkHttpClient.Builder()
                        .addInterceptor(interceptorRequest)
                        .connectTimeout(CONNECT_TIMEOUT,
                                TimeUnit.MILLISECONDS)
                        .readTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .writeTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .dispatcher(dispatcher)
                        .build();

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .client(getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(baseUrl).client(okClient).build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ToastUtils.showShortToast(CheShaoSdk.getInstance().getApplication(), "Url is Wrong");
        }

        return retrofit;
    }





    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
