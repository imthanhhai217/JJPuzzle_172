package com.pachia.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pachia.comon.api.ApiUtils;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.js.command.CmdDashboard;
import com.pachia.comon.listener.IWebViewClientListener;
import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.utils.ToastUtils;

import com.quby.R;

import org.json.JSONObject;

public class DashboardFragment extends BaseWebFragment implements IWebViewClientListener {

    private static final String TAG = "DashboardFragment";

    public static DashboardFragment newInstance() {
        DashboardFragment frag = new DashboardFragment();
        Bundle args = new Bundle();
        String url = ApiUtils.getUrlDashboard(CheShaoSdk.getInstance().getApplication());
        Log.d(DashboardFragment.class.getSimpleName(), "url: " + url);
        args.putString(URL_WEBVIEW, url);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected JsDashboard getJsHandler() {
        return new JsDashboard(new JsDashboard.Listener() {
            @Override
            public void openFanPage(String param) {
                DashBoardUtils.getInstance().openFanpage(mActivity, param);
            }

            @Override
            public void openGroup(String param) {
                DashBoardUtils.getInstance().mobOpenFBGroup(mActivity, param);
            }

            @Override
            public void openBrowser(String param) {
                DashBoardUtils.getInstance().openBrowser(mActivity, param);
            }

            @Override
            public void getImageFromDevice(String param) {
                DashBoardUtils.getInstance().selectImage(mActivity, DashboardFragment.this, param);
            }

            @Override
            public void deleteImage(String param) {
                DashBoardUtils.getInstance().deleteImageData(mActivity, param);
            }

            @Override
            public void deleteAllImage(String param) {
                DashBoardUtils.getInstance().clearImage();
            }

            @Override
            public void loadUrl(String param) {
                try {
                    JSONObject obj = new JSONObject(param);
                    String url = obj.getString("url");
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        reloadWithUrl(url);
                    } else {
                        url = "http://" + url;
                        reloadWithUrl(url);
                    }

                } catch (Exception e) {
                    ToastUtils.showLongToast(mActivity,
                            mActivity.getResources().getString(R.string.err_server_unresponse));
                }
            }


            @Override
            public void onCloseWindow() {
                dismiss();
            }


            @Override
            public void onOpenWindow(String param) {

            }

            @Override
            public void onOpenLink(String param) {
                 CmdDashboard.getInstance().openDialogWebView(mActivity, param);
            }

            @Override
            public void onBackToWindow() {
                goBack();
            }

            @Override
            public void onConnectFacebook(String param) {
                Log.d(TAG, "onConnectFacebook: "+param);
                DashBoardUtils.getInstance().upgradeFacebook(mActivity, DashboardFragment.this);
            }
        });
    }

    @Override
    protected IWebViewClientListener getWebListener() {
        return this;
    }

    @Override
    protected IFragmentListener getActivityResult() {
        return new IFragmentListener() {
            @Override
            public void onActivityRessult(int requestCode, int resultCode, Intent data) {
                switch (requestCode) {
                    case Constants.REQUEST_CODE_FACEBOOK_LOGIN:
                        FacebookManager.getInstance(mActivity).onAuthResult(requestCode, resultCode, data);
                        break;
                    default:
                        DashBoardUtils.getInstance().handleResult(mActivity, requestCode, resultCode, data);
                        break;
                }

            }
        };
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
