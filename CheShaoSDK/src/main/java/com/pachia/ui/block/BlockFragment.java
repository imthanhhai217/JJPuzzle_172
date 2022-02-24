package com.pachia.ui.block;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.pachia.comon.js.JsBlock;
import com.pachia.comon.listener.IBlockIpListener;
import com.pachia.comon.listener.IWebViewClientListener;

public class BlockFragment extends BaseDialogWebFragment implements IWebViewClientListener {
    IBlockIpListener iBlockIpListener;
    @SuppressLint("StaticFieldLeak")
    private static BlockFragment frag;

    public static BlockFragment newInstance(IBlockIpListener iBlockIpListener) {
        if(frag==null){
            frag = new BlockFragment();
            Bundle args = new Bundle();
            String url = ApiUtils.getUrlBlockIP(CheShaoSdk.getInstance().getApplication());
            Log.d(BlockFragment.class.getSimpleName(), url);
            args.putString(URL_WEBVIEW, url);
            frag.setArguments(args);
            frag.setBlockIpListener(iBlockIpListener);
        }
        return frag;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed(){
                // dismiss();
                // activityReference.finish();
                Log.d(BlockFragment.class.getSimpleName(), "back press");
                System.gc();
                System.exit(0);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideToolbar(true);
    }

    public void setBlockIpListener(IBlockIpListener iBlockIpListener) {
        this.iBlockIpListener = iBlockIpListener;
    }

    @Override
    protected JsBase getJsHandler() {
        return new JsBlock(new JsBlock.Listener() {
            @Override
            public void hideBlock() {
                if(iBlockIpListener!=null){
                    iBlockIpListener.hideBlock();
                }
            }

            @Override
            public void onCloseWindow() {

            }

            @Override
            public void onOpenWindow() {

            }

            @Override
            public void onBackToWindow() {

            }
        });
    }

    @Override
    protected IWebViewClientListener getWebListener() {
        return this;
    }

    @Override
    public void shouldOverrideUrlLoading(WebView view, String url) {

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
    }

}
