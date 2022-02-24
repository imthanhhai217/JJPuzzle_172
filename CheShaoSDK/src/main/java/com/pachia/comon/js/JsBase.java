package com.pachia.comon.js;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsBase {
    JsBaseListener baseListener;

    public interface switchCommandJS {
        String closeWindow = "chels_close_window";
        String openWindow = "chels_open_window";
        String backtoWindow = "chels_back_window";
    }

    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        Log.d("AppSDKexecute,","command: "+command+"\n params: "+params);
        switch (command) {
            case switchCommandJS.closeWindow:
                if (baseListener != null) {
                    baseListener.onCloseWindow();
                }
                break;
            case switchCommandJS.openWindow:
                if (baseListener != null) {
                    baseListener.onOpenWindow();
                }
                break;
            case switchCommandJS.backtoWindow:
                if (baseListener != null) {
                    baseListener.onBackToWindow();
                }
                break;
            default:
                break;
        }
    }

    public JsBaseListener getBaseListener() {
        return baseListener;
    }

    public void setBaseListener(JsBaseListener baseListener) {
        this.baseListener = baseListener;
    }
}
