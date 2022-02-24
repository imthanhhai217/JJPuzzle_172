package com.pachia.comon.js;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsBlock extends JsBase {
    Listener listener;

    private interface loginCommandJS extends switchCommandJS {
        String cmd = "chels_check_show_block";
    }

    public JsBlock(Listener listener) {
        this.listener = listener;
        setBaseListener(listener);
    }

    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        Log.d("AppSDKexecute,","command: "+command+"\n params: "+params);

        switch (command) {
            case loginCommandJS.cmd:
                listener.hideBlock();
                break;
            default:
                super.mobAppSDKexecute(command, params);
                break;
        }

    }

    public interface Listener extends JsBaseListener {
        void hideBlock();
    }
}
