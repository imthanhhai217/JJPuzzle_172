package com.pachia.comon.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.KeyboardHeightObserver;
import com.pachia.comon.utils.KeyboardHeightProvider;
import com.pachia.comon.utils.Utils;
import com.pachia.comon.utils.WvUtil;
import com.supho.ChelShaoHelper;
import com.supho.gui.SuphoWebView2;
import com.quby.BuildConfig;
import com.quby.R;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.js.JsBase;
import com.pachia.comon.listener.IWebViewClientListener;
import com.pachia.comon.sharePref.PrefManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseDialogWebPaymentFragment extends DialogFragment implements KeyboardHeightObserver {

    public Activity mActivity;

    public SuphoWebView2 webView;
    ImageButton btnBack, btnClose;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvTitleWebView;
    public static String URL_WEBVIEW = "URL_WEBVIEW";
    private IWebViewClientListener clientListener;
    LinearLayout layoutKeyboardSpace;
    private KeyboardHeightProvider keyboardProvider;
    public static boolean isShowLogo = false;
    RelativeLayout toolbar;
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

    protected void hideToolbar(boolean hide) {

        if (hide) {
            toolbar.setVisibility(View.GONE);
        } else toolbar.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_web, container, false);
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
        btnBack = (ImageButton) v.findViewById(R.id.btnBack);
        webView = (SuphoWebView2) v.findViewById(R.id.webview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swRegister);
        tvTitleWebView = (TextView) v.findViewById(R.id.tvTitleWebview);
        toolbar= (RelativeLayout) v.findViewById(R.id.toolbar);
        layout_container= (LinearLayout)v.findViewById(R.id.layout_container);
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack())
                    webView.goBack();
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

    protected void setTitleForm(String title) {
        tvTitleWebView.setText(title);
    }

    public void setVisitableFragment(int visibility){
        layout_container.setVisibility(visibility);
    }

    public void initWebView() {
        try {
            webView.clearCache(true);
            webView.setInitialScale((int) DeviceUtils.getDensity(Objects.requireNonNull(getActivity())));
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

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
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);

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
                    view.addJavascriptInterface(BaseDialogWebPaymentFragment.this, "android");
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

//                    GoogleAnalyticsManager.getInstance().trackScreen(view.getTitle());
                    if (webView.canGoBack())
                        btnBack.setVisibility(View.VISIBLE);
                    else
                        btnBack.setVisibility(View.GONE);

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


    public void loadUrlWithLibraryHeaders(String url) {
        try {
            if (Utils.isPlfUrl(url)) {
                Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                Uri uri = Uri.parse(url);
                WvUtil.setCookieToURLWebview(webView.getContext(), url);
                Map<String, String> extraHeaders = new HashMap<String, String>();

                String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
                String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");

                String ads= DeviceUtils.getAdvertisingID(context);
                if(ads.isEmpty()){
                    ads="AdvertisingId not found";
                }

                extraHeaders.put(ConstantApi.HEADER_ATHORIZATION, AuthenConfigs.getInstance().getAccessToken());
                extraHeaders.put(Constants.X_REQUEST, GameConfigs.getInstance().getAppKey());
                extraHeaders.put(Constants.APP_KEY, GameConfigs.getInstance().getAppKey());
                extraHeaders.put(Constants.DEVICE, Utils.stringNormalize(DeviceUtils.getDevice()));
                extraHeaders.put(Constants.OS, Utils.stringNormalize(DeviceUtils.getOSInfo()));
                extraHeaders.put(Constants.RESOLUTION, Utils.stringNormalize(DeviceUtils.getResolution(getActivity())));
                extraHeaders.put(Constants.SDK_VER, Utils.stringNormalize(Utils.getSDKVersion(context)));
                extraHeaders.put(Constants.APP_VER, Utils.stringNormalize(Utils.getGameVersion(context)));
                extraHeaders.put(Constants.APP_VER_CODE, Utils.stringNormalize(Utils.getGameVersionCode(context)));
                extraHeaders.put(Constants.NETWORK, Utils.stringNormalize(Utils.getNetwork(context)));
                extraHeaders.put(Constants.ACCESS_TOKEN, Utils.stringNormalize(AuthenConfigs.getInstance().getAccessToken()));
                extraHeaders.put(Constants.ROLE, Utils.stringNormalize(character_id));
                extraHeaders.put(Constants.AREA, Utils.stringNormalize(area_id));
                extraHeaders.put(Constants.ROLE_NAME, character_id);
                extraHeaders.put(Constants.AREA_NAME, area_id);
                extraHeaders.put(Constants.ADVERTISING_ID, ads);
                extraHeaders.put(Constants.DISTRIBUTOR, Utils.stringNormalize(Utils.getReferrer(context)));
                extraHeaders.put(Constants.APPSFLYER_ID, DeviceUtils.getAppsflyerUID(mActivity));

                webView.stopLoading();
                webView.loadUrl(uri.toString(), extraHeaders);
            } else {
                loadUrl(url);
            }
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

    protected abstract JsBase getJsHandler();

    protected abstract IWebViewClientListener getWebListener();
}
