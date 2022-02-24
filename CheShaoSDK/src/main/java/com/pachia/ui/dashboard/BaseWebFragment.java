package com.pachia.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.listener.IWebViewClientListener;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.KeyboardHeightObserver;
import com.pachia.comon.utils.KeyboardHeightProvider;
import com.pachia.comon.utils.Utils;
import com.pachia.comon.utils.WvUtil;
import com.quby.R;
import com.supho.ChelShaoHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseWebFragment extends DialogFragment implements KeyboardHeightObserver {

    private static final String TAG = "BaseWebFragment";
    public Activity mActivity;

    public CustomWebView webView;
    ImageButton btnClose;
    SwipeRefreshLayout swipeRefreshLayout;
    public static String URL_WEBVIEW = "URL_WEBVIEW";
    private IWebViewClientListener clientListener;
    LinearLayout layoutKeyboardSpace;
    private KeyboardHeightProvider keyboardProvider;
    public static boolean isShowLogo = false;
    LinearLayout layout_container;

    /**
     * Layout of activity
     *
     * @return id of resource layout
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_web_no_title_bar, container, false);
        //Setting dialog background and animation
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0.8f);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (GameConfigs.getInstance().getSdkConfig() != null) ChelShaoHelper.hideNotiFloatButton();

        int orientation = mActivity.getResources().getConfiguration().orientation;
        PrefManager.saveInt(mActivity, getTag() + Constants.SCREEN_ORIENTATION_CURRENT, orientation);
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public static boolean isShow() {
        return isShowLogo;
    }

    @Override
    public void onResume() {
        isShowLogo = true;
        super.onResume();
        keyboardProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardProvider.setKeyboardHeightObserver(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        keyboardProvider.close();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showProgressDialog(boolean show, String mess) {
        Utils.showLoading(mActivity, show);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isShowLogo = false;
        if (GameConfigs.getInstance().getSdkConfig() != null) {
            ChelShaoHelper.showNotiFloatButton(GameConfigs.getInstance().getSdkConfig().getEx());
        }
        super.onDismiss(dialog);
    }

    protected void initView(View v) {
        clientListener = getWebListener();
        btnClose = (ImageButton) v.findViewById(R.id.btnClose);
        webView = (CustomWebView) v.findViewById(R.id.webview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swRegister);
        layout_container = (LinearLayout) v.findViewById(R.id.layout_container);
        initWebView();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameConfigs.getInstance().getSdkConfig() != null) {
                    isShowLogo = false;
                    ChelShaoHelper.showNotiFloatButton(GameConfigs.getInstance().getSdkConfig().getEx());
                }
                dismiss();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (webView != null)
                    webView.reload();
            }
        });
        layoutKeyboardSpace = (LinearLayout) v.findViewById(R.id.layout_keyboard_space);
        keyboardProvider = new KeyboardHeightProvider(mActivity);
        keyboardProvider.start();

    }

    public void setVisitableFragment(int visibility) {
        layout_container.setVisibility(visibility);
    }

    public void initWebView() {
        try {
            webView.clearCache(true);
            webView.setInitialScale((int) DeviceUtils.getDensity(Objects.requireNonNull(getActivity())));
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webView.setBackgroundColor(Color.parseColor("#00000000"));
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadsImagesAutomatically(true);

            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int progress) {
//                    progressbarPredict.setVisibility(View.VISIBLE);
//                    progressbarPredict.setProgress(progress);
                }
            });

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    showProgressDialog(true, "");
                    if (Utils.isPlfUrl(url)) {
                        loadUrlWithLibraryHeaders(url);
                        return true;
                    }
                    if (clientListener != null)
                        clientListener.shouldOverrideUrlLoading(view, url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @SuppressLint("JavascriptInterface")
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    showProgressDialog(true,"");
                    view.addJavascriptInterface(BaseWebFragment.this, "android");
                    if (clientListener != null)
                        clientListener.onPageStarted(view, url, favicon);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    showProgressDialog(false, "");
                    WvUtil.showErrorPage(mActivity, view, failingUrl);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    showProgressDialog(false, "");
                    if (swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);


                    if (clientListener != null)
                        clientListener.onPageFinished(view, url);
                }


            });

            webView.addJavascriptInterface(getJsHandler(), "JsHandler");
            String url = getArguments().getString(URL_WEBVIEW, "");
            if (Utils.isPlfUrl(url)) {
                loadUrlWithLibraryHeaders(url);
            }
            webView.setTag("RegisterFragment");
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public void invokeJavascript(String script) {
        webView.loadUrl("javascript:" + script);
    }

    public void loadUrlWithLibraryHeaders(String url) {
        try {
            if (Utils.isPlfUrl(url)) {
                Log.d(TAG, "loadUrlWithLibraryHeaders: " + url);
                Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                Uri uri = Uri.parse(url);
                WvUtil.setCookieToURLWebview(webView.getContext(), url);
                Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("access_token", AuthenConfigs.getInstance().getAccessToken());
                extraHeaders.put(ConstantApi.HEADER_ATHORIZATION, "Bearer " + AuthenConfigs.getInstance().getAccessToken());
                extraHeaders.put("device_name", Utils.stringNormalize(DeviceUtils.getDevice()));
                extraHeaders.put("device_os", Utils.stringNormalize(DeviceUtils.getOSInfo()));
                extraHeaders.put("device_resolution", Utils.stringNormalize(DeviceUtils.getResolution(getActivity())));
                extraHeaders.put("sdk_version", Utils.stringNormalize(Utils.getSDKVersion(context)));
                extraHeaders.put("device_network", Utils.stringNormalize(Utils.getNetwork(context)));
                extraHeaders.put("appsflyer_id", DeviceUtils.getAppsflyerUID(context));
                extraHeaders.put("advertising_id", DeviceUtils.getAdvertisingID(context));
                webView.stopLoading();
                webView.loadUrl(uri.toString(), extraHeaders);
            } else {
                loadUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadWithUrl(String url) {

        try {
            Log.d(TAG, "reloadWithUrl: " + url);
            Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
            Uri uri = Uri.parse(url);
            WvUtil.setCookieToURLWebview(webView.getContext(), url);
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("access_token", AuthenConfigs.getInstance().getAccessToken());
            extraHeaders.put(ConstantApi.HEADER_ATHORIZATION, "Bearer " + AuthenConfigs.getInstance().getAccessToken());
            extraHeaders.put("device_name", Utils.stringNormalize(DeviceUtils.getDevice()));
            extraHeaders.put("device_os", Utils.stringNormalize(DeviceUtils.getOSInfo()));
            extraHeaders.put("device_resolution", Utils.stringNormalize(DeviceUtils.getResolution(getActivity())));
            extraHeaders.put("sdk_version", Utils.stringNormalize(Utils.getSDKVersion(context)));
            extraHeaders.put("device_network", Utils.stringNormalize(Utils.getNetwork(context)));
            extraHeaders.put("appsflyer_id", DeviceUtils.getAppsflyerUID(context));
            extraHeaders.put("advertising_id", DeviceUtils.getAdvertisingID(context));
            if (webView != null)
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.stopLoading();
                        webView.loadUrl(uri.toString(), extraHeaders);
                    }
                });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadUrl(String url) {
        if (isAdded()) {
            webView.stopLoading();
            webView.loadUrl(url);
        }
    }

    public void goBack() {
        if (webView != null)
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if (webView.canGoBack())
                        webView.goBack();
                }
            });
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        changeKeyboardHeight(height);
    }

    private void changeKeyboardHeight(int height) {
        try {
            if (height > 100) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                layoutKeyboardSpace.setLayoutParams(params);
                layoutKeyboardSpace.setVisibility(View.VISIBLE);
                return;
            }

            layoutKeyboardSpace.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivityResult().onActivityRessult(requestCode, resultCode, data);
    }

    protected abstract JsDashboard getJsHandler();

    protected abstract IWebViewClientListener getWebListener();

    protected abstract IFragmentListener getActivityResult();
}
