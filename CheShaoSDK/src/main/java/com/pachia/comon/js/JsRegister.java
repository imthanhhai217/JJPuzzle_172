package com.pachia.comon.js;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.pachia.comon.tracking.CheShaoTracking;

public class JsRegister extends JsBase {
    Listener listener;

    private interface loginCommandJS extends switchCommandJS {
        String loginSuccess = "chels_login_done";
        String clickRegister = "chels_click_regis";
        String loadFormRegisterSuccess = "chels_load_frmregis";
    }

    public JsRegister(Listener listener) {
        this.listener = listener;
        setBaseListener(listener);
    }

    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        Log.d("AppSDKexecute,", "command: " + command + "\n params: " + params);

        switch (command) {
            case loginCommandJS.loginSuccess:
                if (listener != null) {
                    listener.onRegisterSuccess(params);
                }
                break;
            case loginCommandJS.clickRegister:
                CheShaoTracking.getInstance().trackRegisterSubmitForm();
                break;
            case loginCommandJS.loadFormRegisterSuccess:
                CheShaoTracking.getInstance().trackRegisterLoadSuccess();
                break;

            default:
                super.mobAppSDKexecute(command, params);
                break;
        }

    }

    public interface Listener extends JsBaseListener {
        void onRegisterSuccess(String param);
    }
}
