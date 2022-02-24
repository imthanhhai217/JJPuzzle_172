package com.pachia.comon.js;

import android.webkit.JavascriptInterface;

public class JsTerm extends JsBase {
    JsBaseListener listener;

    public JsTerm(Listener listener) {
        this.listener = listener;
        setBaseListener(listener);
    }

    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        super.mobAppSDKexecute(command, params);
    }

    public interface Listener extends JsBaseListener {

    }


}
