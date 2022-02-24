package com.pachia.comon.constants;

/**
 * Created by Chuyennt
 */

public interface ConstantApi {


    String APP_UR = "APP_UR";
    String BASE_URL = "https://core.chelshaoc.com/";
    String URL_WEBVIEW_DASHBOARD = "aoc/v2/{app_key}/{app_version}/navigation/menu";
    String URL_LOGIN_PLAYNOW = "api/aoc/v2/{app_key}/{app_version}/auth/startNow";
    String URL_LOGIN_EMAIL = "api/aoc/v2/{app_key}/{app_version}/auth/signinemail";
    String URL_LOGIN_GOOGLE = "api/aoc/v2/{app_key}/{app_version}/auth/signingg";
    String URL_LOGIN_FACEBOOK = "api/aoc/v2/{app_key}/{app_version}/auth/signinfb";

    String URL_GET_AUTH_CONFIG = "api/aoc/v2/{app_key}/{app_version}/setup/authenticate";
    String URL_GET_SDK_CONFIG = "api/aoc/v2/{app_key}/{app_version}/setup/central";
    String URL_GET_USER = "api/aoc/v2/{app_key}/{app_version}/user/information";
    String URL_GET_MESS_IN_GAME = "api/aoc/v2/{app_key}/{app_version}/notify";
    String URL_CONNECT_FACEBOOK = "api/aoc/v2/{app_key}/{app_version}/user/setfb";
    String URL_SAVE_CHARACTOR = "api/aoc/v2/{app_key}/{app_version}/user/figure/save";

    /**
     * INTERACT
     */
     String URL_BASE_INTERACT = ""; // product
    String URL_SAVE_FCM = "sdk/device-tokens/fcm";

    /**
     * PAYTECH
     */
  String URL_BASE_PAYTECH = "https://stg-pay.chelshaoc.com/"; //Staging
//    String URL_BASE_PAYTECH = "https://pay.chelshaoc.com/"; // product
    String URL_VERIFY_PURCHASE = "payments/sdk/excute/{order_no}";
    String URL_PURCHASE_PRODUCTS = "wv/sdk/items";

    String URL_GET_LIST_ITEM="products/items/sdk";
    String URL_INITIAL_PURCHASE="payments/sdk";

    String URL_RETRY_PURCHASE = "payments/sdk/retry";

    /**
     * ---------------------------------------------------------------------------------------------
     */

    String URL_WEBVIEW_REGISTER = "aoc/v2/{app_key}/{app_version}/registration/email";
    String URL_WEBVIEW_TERM = "aoc/v2/{app_key}/{app_version}/policies";
    String HEADER_X_AUTHIRIZATION = "X-Authorization";
    String HEADER_ATHORIZATION = "Authorization";
    String HEADER_APP_KEY = "X-Request";
    String HEADER_APP_VERSION = "app-Version";
    String HEADER_SDK_VERSION = "sdk-Version";
    String HEADER_SDK_LOCATLE = "sdk-locale";
    String HEADER_ACCESS_TOKEN = "access_token";
    String HEADER_DEVICE_OS = "device-Os";
    String HEADER_DEVICE_NAME = "device-Name";
    String HEADER_ADVERTISING_ID = "advertising-id";

    String URL_BLOCK_IP="aoc/v2/{app_key}/{app_version}/showConfigure";

}
