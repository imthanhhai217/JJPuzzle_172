package com.pachia.comon.constants;

public interface ConstantTrackEvent {
    String START_TIME = "BEGIN_ACTION_TIME";
    String START_TIME_LOGIN = "BEGIN_ACTION_LOGIN_TIME";
    String START_TIME_CALL_API = "START_TIME_CALL_API";

    String ACTION_APP_LAUNCHED = "action_app_launched";
    String ACTION_FIRST_LAUNCHED = "action_first_launched";
    String ACTION_EXTRACT_STARTED = "action_extract_started";
    String ACTION_EXTRACT_FINISHED = "action_extract_finished";
    String ACTION_RESOURCE_STARTED = "action_resource_started";
    String ACTION_RESOURCE_FINISHED = "action_resource_finished";
    String ACTION_EXTRACT_CDN_FINISHED = "action_extract_cdn_finished";

    String ACTION_LOGOUT_SUCCESS = "action_logout_success";

    String SESSION = "session";

    String ACTION_START_PERMISISON = "action_start_permisison";
    String ACTION_END_PERMISSION = "action_end_permission";
    String ACTION_OVERLAY_CLICK_PERMISSION = "action_overlay_click_permission";

    String ACTION_LOAD_GAME_CONFIG_CALL_API_SUCCESS = "action_load_game_config_call_api_success";
    String ACTION_LOAD_GAME_CONFIG_CALL_API_FAIL = "action_load_game_config_call_api_fail";



    String ACTION_ENTER_GAME_BTN_CLICKED = "action_enter_game_btn_clicked";
    String ACTION_CHARACTER_CREATED = "action_character_created";
    String ACTION_CHARACTER_CREATED_CALL_API_SUCCESS = "action_character_created_call_api_success";
    String ACTION_CHARACTER_CREATED_CALL_API_FAIL = "action_character_created_call_api_fail";

    String ACTION_VIP_ACHIEVED = "action_vip_achieved";

    //Login
    String ACTION_LOGIN_SCREEN_OPENED = "action_login_screen_opened";
    String ACTION_LOGIN_SCREEN_LOGIN_EMAIL_BTN_CLICKED = "action_login_screen_login_email_btn_clicked";
    String ACTION_LOGIN_SCREEN_LOGIN_EMAIL_INPUT_ERROR = "action_login_screen_login_email_input_error";
    String ACTION_LOGIN_SCREEN_LOGIN_EMAIL_API_CALL = "action_login_screen_login_email_api_call";
    String ACTION_LOGIN_SCREEN_LOGIN_EMAIL_API_CALL_FAIL = "action_login_screen_login_email_api_call_fail";
    String ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_BTN_CLICKED = "action_login_screen_login_facebook_btn_clicked";
    String ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_USER_CANCELED = "action_login_screen_login_facebook_user_canceled";
    String ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_HAVE_ERROR = "action_login_screen_login_facebook_have_error";
    String ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_API_CALL = "action_login_screen_login_facebook_api_call";
    String ACTION_LOGIN_SCREEN_LOGIN_FACEBOOK_API_CALL_FAIL = "action_login_screen_login_facebook_api_call_fail";
    String ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_BTN_CLICKED = "action_login_screen_login_google_btn_clicked";
    String ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_USER_CANCELED = "action_login_screen_login_google_user_canceled";
    String ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_HAVE_ERROR = "action_login_screen_login_google_have_error";
    String ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_API_CALL = "action_login_screen_login_google_api_call";
    String ACTION_LOGIN_SCREEN_LOGIN_GOOGLE_API_CALL_FAIL = "action_login_screen_login_google_api_call_fail";

    String ACTION_LOGIN_SCREEN_LOGIN_PLAYNOW_BTN_CLICKED = "action_login_screen_login_playnow_btn_clicked";
    String ACTION_LOGIN_SCREEN_LOGIN_PLAYNOW_API_CALL_FAIL = "action_login_screen_login_playnow_api_call_fail";

    String ACTION_LOGIN_SCREEN_REGISTER_BTN_CLICKED = "action_login_screen_register_btn_clicked";
    String ACTION_LOGIN_SCREEN_REGISTER_FORM_LOAD_SUCCESS = "action_login_screen_register_form_load_success";
    String ACTION_LOGIN_SCREEN_REGISTER_FORM_SUBMITED = "action_login_screen_register_form_submited";
    String ACTION_LOGIN_SCREEN_CLOSE_BTN_CLICKED = "action_login_screen_close_btn_clicked";//

    String ACTION_MAINTAIN_SCREEN_OPENED = "action_maintain_screen_opened";
    String ACTION_GET_AUTHEN_API_CALL_FAIL = "action_get_authen_api_call_fail";
    String ACTION_GET_AUTHEN_API_CALL_SUCCESS = "action_get_authen_api_call_success";
    String ACTION_LOGIN_SCREEN_FORGOT_PASSWORD_BTN_CLICKED = "action_login_screen_forgot_password_btn_clicked";

    String LAST_SESSION_START = "LAST_SESSION_START";
    String LAST_SESSION_END = "LAST_SESSION_END";
    String LAST_SESSION_DETAl = "LAST_SESSION_DETAl";


    /**
     * PAYMENT------------------------------------------------------------------
     */

    String ACTION_PAYMENT_SCREEN_OPENED = "action_payment_screen_opened";
    String ACTION_PAYMENT_SCREEN_LOAD_SUCCESS = "action_payment_screen_load_success";
    String ACTION_PAYMENT_SCREEN_LOAD_FAIL = "action_payment_screen_load_fail";
    String ACTION_PAYMENT_BEFORE_VERIFY = "action_payment_before_verify";
    String ACTION_PAYMENT_VERIFY_FAIL = "action_payment_verify_fail";

    String TRACK_RETRY_PURCHASE_FAIL = "action_payment_retry_purchase_fail";
}
