package com.pachia.ui.register;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pachia.comon.api.ApiUtils;
import com.pachia.comon.game.BaseDialogWebFragment;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.js.JsBase;
import com.pachia.comon.js.JsRegister;
import com.pachia.comon.listener.ILoginListener;
import com.pachia.comon.listener.IWebViewClientListener;
import com.quby.R;
import com.pachia.comon.tracking.CheShaoTracking;

public class RegistryFragment extends BaseDialogWebFragment implements IWebViewClientListener {

    ILoginListener registerListener;

    public static RegistryFragment newInstance(ILoginListener registerListener) {
        RegistryFragment frag = new RegistryFragment();
        Bundle args = new Bundle();
        String url = ApiUtils.getUrlWvRegister(CheShaoSdk.getInstance().getApplication());
        Log.d(RegistryFragment.class.getSimpleName(), "url: "+ url);
        args.putString(URL_WEBVIEW, url);
        frag.setArguments(args);
        frag.setRegisterListener(registerListener);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleForm(getResources().getString(R.string.header_registry));
    }

    @Override
    protected JsBase getJsHandler() {
        return new JsRegister(new JsRegister.Listener() {
            @Override
            public void onCloseWindow() {
                dismiss();
            }

            @Override
            public void onOpenWindow() {

            }

            @Override
            public void onBackToWindow() {
                goBack();
            }

            @Override
            public void onRegisterSuccess(String param) {
                if (registerListener != null)
                    registerListener.onRegisterSuccess(param);
            }
        });
    }

    @Override
    protected IWebViewClientListener getWebListener() {
        return this;
    }

    public void setRegisterListener(ILoginListener registerListener) {
        this.registerListener = registerListener;
    }

    @Override
    public void shouldOverrideUrlLoading(WebView view, String url) {

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (url.contains("checkScreen=register")) {
            CheShaoTracking.getInstance().trackRegisterSubmitForm();
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.equals(ApiUtils.getUrlWvRegister(mActivity))) {
            CheShaoTracking.getInstance().trackRegisterLoadSuccess();
        }
    }
}
