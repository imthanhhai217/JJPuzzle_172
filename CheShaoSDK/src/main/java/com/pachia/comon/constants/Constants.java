package com.pachia.comon.constants;

/**
 * Created by Dungnv
 */

public interface Constants {

    String LAST_PURCHASE = "IAP_LAST_PURCHASE";

    interface PrefKey {
        String PREF_USER_JSON = "PREF_USER_JSON";
        String PREF_TOKEN = "PREF_TOKEN";
        String APP_KEY = "APP_KEY";
        String ADS_KEY = "ADS_KEY";
    }

    String SESSION_OUT_TIME = "SESSION_OUT_TIME";

    interface LOGIN_TYPE {
        String EMAIL = "Email";
        String FACEBOOK = "Facebook";
        String PLAYNOW = "PlayNow";
        String GOOGLE = "Google";
    }


    interface USER_ERR_CODE {
        int INVALID_TOKEN = 401;
        int INVALID_INPUT = 422;
    }

    enum RTF_STATUS {
        AUTHENING,
        DONE
    }

    /**
     * ----------------------------------
     */
    int HIDE = 0;
    int VISIBLE = 1;

    /**
     * -----------------------------------\
     */
    String SAVE_FCM_NON_TOKEN = "SAVE_FCM_NON_TOKEN";
    String SCREEN_ORIENTATION_CURRENT = "SCREEN_ORIENTATION_CURRENT";
    String APP_LANG = "APP_LANG";

    String ROLE_ID = "CHESHAO_ROLE_ID";
    String AREA_ID = "CHESHAO_AREA_ID";
    String PURCHASE = "CHESHAO_PURCHASE";


    //----------------------
    /**
     * REQUEST CODE
     **/
     int REQUEST_CODE_LOGIN_PLAY_SERVICES = 0;
     int REQUEST_CODE_GOOGLE_IN_APP_BILLING = 10001;
     int REQUEST_CODE_FACEBOOK_LOGIN = 64206;
     int REQUEST_CODE_FACEBOOK_SHARE = 64207;
     int REQUEST_CODE_FACEBOOK_INVITE = 64213;
     int REQUEST_CODE_FACEBOOK_INVITE_GAME = 64210;
     int REQUEST_CODE_PICKER = 20002;

     int REQUEST_CODE_PAYPAL_PAYMENT = 1;
     int REQUEST_CODE_PAYPAL_FUTURE_PAYMENT = 2;
     int REQUEST_CODE_PAYPAL_PROFILE_SHARING = 3;
     int REQUEST_OVERLAY_PERMISSION = 999;
     int REQUEST_CALENDAR_PERMISSION = 992;

    public static String SDK_VERSION = "5.0.0_172";


    /** HTTP HEADERS Payment **/
    String X_REQUEST = "X-Request";
    String APP_KEY = "chel_eImTn";
    String ACCESS_TOKEN = "chel_uTyEH";
    String OS = "chel_gWIWV";
    String RESOLUTION = "chel_EmDlI";
    String APP_VER = "chel_BpjJE";
    String APP_VER_CODE = "chel_RTpDr";
    String NETWORK = "chel_FrGyf";
    String DEVICE = "chel_rGnpp";
    String SDK_VER = "chel_xVqLk";
    String ROLE = "chel_rOrWP";
    String AREA = "chel_AsrcV";
    String ROLE_NAME = "chel_AqVPB";
    String AREA_NAME = "chel_wnlsH";
    String ADVERTISING_ID = "chel_lCEpL";
    String DISTRIBUTOR = "chel_vWKXD";
    String APPSFLYER_ID = "chel_RjMAg";


}
