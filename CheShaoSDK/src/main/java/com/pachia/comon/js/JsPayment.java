package com.pachia.comon.js;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsPayment extends JsBase {
    Listener listener;

    private interface CommandJS extends switchCommandJS {
        String startPayment = "chels_payment_start";
        String localPurchase = "chels_local_purchase";

    }

    public JsPayment(Listener listener) {
        this.listener = listener;
        setBaseListener(listener);
    }

    @JavascriptInterface
    public void executeFunctionP(String command, String params) {
        String message="command payment: " + command + " params: " + params;
        Log.d(JsPayment.class.getSimpleName(), message);
        switch (command) {
            case CommandJS.startPayment:
                if (listener != null) {
                    listener.onPaymentStartIAPV3(params);
                }
                break;

            case CommandJS.localPurchase:
                if (listener != null) {
                    listener.onLocalPayment(params);
                }
                break;
            default:
                super.mobAppSDKexecute(command, params);
                break;
        }
    }

    public interface Listener extends JsBaseListener {
        void onPaymentStartIAPV3(String param);

        void onLocalPayment(String param);
    }


}
