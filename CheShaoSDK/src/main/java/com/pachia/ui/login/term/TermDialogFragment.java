package com.pachia.ui.login.term;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pachia.comon.game.BaseDialogWebFragment;
import com.pachia.comon.js.JsBase;
import com.pachia.comon.js.JsTerm;
import com.pachia.comon.listener.IWebViewClientListener;

public class TermDialogFragment extends BaseDialogWebFragment {


    public static TermDialogFragment newInstance(String url) {
        TermDialogFragment frag = new TermDialogFragment();
        Bundle args = new Bundle();
        args.putString(URL_WEBVIEW, url);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleForm("");
    }

    @Override
    protected JsBase getJsHandler() {
        return new JsTerm(new JsTerm.Listener() {
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
        });
    }

    @Override
    protected IWebViewClientListener getWebListener() {
        return null;
    }
}
