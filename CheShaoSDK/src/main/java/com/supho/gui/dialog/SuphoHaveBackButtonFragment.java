package com.supho.gui.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.supho.model.MUrl;
import com.supho.CheShaoSDK;
import com.supho.gui.SuphoWebView;
import com.supho.utils.Constants;
import com.supho.utils.Preference;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.comon.utils.WvUtil;
import com.quby.R;
import com.pachia.comon.js.JsHandler;
import com.supho.utils.EncryptionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

/**
 * Created by Khai Tran on 5/25/2017.
 */

public class SuphoHaveBackButtonFragment extends DialogFragment {
    //	private static String TAG = GameFragment.class.getSimpleName();
    private static final String KEY_URL = "url";
    private static final String KEY_POST_PARAMS = "post_params";

    private Activity activity = CheShaoSDK.activity;
    private View layoutRoot;
    private ProgressBar progessBar;
    //	private ProgressDialog progressBar;
    private SuphoWebView webView;
    private ImageButton btnClose, btnBack;
    private Bundle args;
    private JsHandler jsHandler;
    private MUrl mUrl;
    private Stack<MUrl> historyBackstack;
    private static HashMap<String, MUrl> cachedUrls = new HashMap<>();
    private DialogInterface.OnDismissListener mOnDismissListener;

    {
        args = new Bundle();
        historyBackstack = new Stack<MUrl>();
    }

    public SuphoHaveBackButtonFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SuphoHaveBackButtonFragment(String url) {
        this(new MUrl(url));
    }

    @SuppressLint("ValidFragment")
    public SuphoHaveBackButtonFragment(MUrl url) {
        super();
        args.putParcelable(KEY_URL, url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Preference.save(activity, Constants.SHARED_PREF_SHOW_DASHBOARD, true);
        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (event != null
                        && keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });

        layoutRoot = inflater.inflate(R.layout.fragment_backbutton_dialog, container, false);
        btnBack = (ImageButton) layoutRoot.findViewById(R.id.btn_back);
        btnClose = (ImageButton) layoutRoot.findViewById(R.id.btn_close);
        progessBar = (ProgressBar) layoutRoot.findViewById(R.id.progress_bar);
        webView = (SuphoWebView) layoutRoot.findViewById(R.id.webview);

        initHeaderBar();
        initWebView();

        return layoutRoot;
    }

    private void initHeaderBar() {
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
            }
        });
    }

    private void updateHeaderBar() {
        try {
            if (isAdded()) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (canGoBack()) {
                            btnBack.setVisibility(View.VISIBLE);
                        } else {
                            btnBack.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        try {
            webView.clearCache(true);
            webView.setInitialScale((int) DeviceUtils.getDensity(getActivity()));
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webView.setBackgroundColor(Color.TRANSPARENT);

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
                    progress = Math.max(1, progress);
                    progessBar.setProgress(progress);
                    if (progress >= 75) {
                        hideProgressBar();
                    } else {
                        showProgressBar();
                    }
                    updateHeaderBar();
                }
            });

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (getActivity() != null) {
//					TODO PersistentConfig.setCookieToURLWebview(webView.getContext(), url);
                    }
                    if (Utils.isPlfUrl(url)
                            && !url.contains("appkey=" + GameConfigs.getInstance().getAppKey())) {
                        try {
                            loadUrlWithMobHeaders(url);
                            return true;
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    try {
                        Uri uri = Uri.parse(url);
                        String strClearHistoryBackstack = uri
                                .getQueryParameter("clearHistoryBackstack");
                        if (!TextUtils.isEmpty(strClearHistoryBackstack)) {
                            boolean shoulClearHistoryBackstack = Integer
                                    .parseInt(Objects.requireNonNull(strClearHistoryBackstack)) != 0;
                            if (shoulClearHistoryBackstack) {
//							SharePreferenceUtils.save(activity,
//									Constants.ITEM_NEW, url);
                                historyBackstack.clear();
                            }
                        }

                        Log.d("check url", "url:" + url + " ; item url:");
                    } catch (Exception e) {

                    }
                    addToBackstack(url);
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    WvUtil.showErrorPage(getActivity(), webView, failingUrl);
                }

                @TargetApi(android.os.Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                    // Redirect to deprecated method, so you can use it in all SDK versions
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    hideProgressBar();
                    updateHeaderBar();
                }

            });

            jsHandler = new JsHandler(CheShaoSDK.activity, this);
            webView.addJavascriptInterface(jsHandler, "JsHandler");

            mUrl = args.getParcelable(KEY_URL);

            HashMap<String, String> postParams = (HashMap<String, String>) args.getSerializable(KEY_POST_PARAMS);
            if (mUrl != null) {
                if (postParams != null) {
                    try {
                        postUrlWithMobHeaders(mUrl, postParams);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        loadUrlWithMobHeaders(mUrl);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl(MUrl url) {
        if (isAdded()) {
            try {
                args.clear();
                webView.stopLoading();
                webView.loadUrl(url.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadUrlWithMobHeaders(String url)
            throws InterruptedException, ExecutionException {
        MUrl mUrl = cachedUrls.get(url);
        if (mUrl == null) {
            mUrl = new MUrl(url);
        }
        loadUrlWithMobHeaders(mUrl);
    }

    public void loadUrlWithMobHeaders(final MUrl url)
            throws InterruptedException, ExecutionException {

        if (!isAdded()) {
            args.putParcelable(KEY_URL, url);
            return;
        }
        if (Utils.isPlfUrl(url)) {
            Context context = getActivity().getApplicationContext();
            Uri uri = url.toUri();
            if (uri.getQueryParameter("appkey") == null) {
                uri = uri
                        .buildUpon()
                        .appendQueryParameter("appkey", GameConfigs.getInstance().getAppKey()).build();
            }
            if (url.isEcrypted() && uri.getQueryParameter("sign") == null) {
                uri = uri
                        .buildUpon()
                        .appendQueryParameter("sign", EncryptionUtils.getSignedString(context)).build();
            }

            setCookieToURLWebview(webView.getContext(), url.getPath());
            Map<String, String> extraHeaders = new HashMap<String, String>();
            String token = AuthenConfigs.getInstance().getAccessToken();
            if (token != null && !TextUtils.isEmpty(token))
                extraHeaders.put(ConstantApi.HEADER_ATHORIZATION, "Bearer " + token);

            webView.stopLoading();
            webView.loadUrl(uri.toString(), extraHeaders);
        } else {
            loadUrl(url);
        }
    }

    private void postUrl(MUrl url) {
        try {
            webView.postUrl(url.getPath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void postUrlWithMobHeaders(MUrl url, HashMap<String, String> postParams)
            throws InterruptedException, ExecutionException, UnsupportedEncodingException {
        postUrlWithMobHeaders(url, postParams, "");
    }

    public void postUrlWithMobHeaders(final MUrl url, HashMap<String, String> postParams, String a)
            throws InterruptedException, ExecutionException, UnsupportedEncodingException {

        if (!isAdded()) {
            args.putParcelable(KEY_URL, url);
            args.putSerializable(KEY_POST_PARAMS, postParams);
            return;
        }
        if (Utils.isPlfUrl(url)) {
            Uri uri = url.toUri();
            final String postUrl = uri.toString();
            String postData = "hl=en";
            for (String key : postParams.keySet()) {
                postData += "&" + key + "=" + URLEncoder.encode(postParams.get(key), "UTF-8");
            }
            setCookieToURLWebview(webView.getContext(), postUrl);
            webView.postUrl(postUrl, postData.getBytes());

        } else {
            postUrl(url);
        }
    }


    public static void setCookieToURLWebview(Context context, String url) {
        try {
            HashSet<String> cookies = (HashSet<String>) Preference.getStringSet(context, Constants.SHARED_PREF_COOKIES, new HashSet<String>());
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            for (String cookie : cookies) {
                cookieManager.setCookie(url, cookie);
            }
            cookieSyncManager.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {
        if (canGoBack()) {
            goBack();
        } else {
            dismiss();
            Preference.remove(activity, Constants.SHARED_PREF_SHOW_DASHBOARD);
        }
    }

    private boolean canGoBack() {
        return historyBackstack.size() > 1;
    }

    private void goBack() {
        try {
//			if(isClearHistoryBackstack(historyBackstack.peek().getPath())){
//				historyBackstack.pop();
//			}
            historyBackstack.pop();
            loadUrlWithMobHeaders(historyBackstack.peek());
        } catch (Exception e) {
            // if history stack is empty
        }
    }


    private void showProgressBar() {
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progessBar.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressBar() {
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            progessBar.setVisibility(View.GONE);
//						progressBar.dismiss();
                        }
                    });
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isClearHistoryBackstack(String url) {
        try {
            Uri uri = Uri.parse(url);
            String strIsClearHistoryBackstack = uri.getQueryParameter("clearHistoryBackstack");
            if (!TextUtils.isEmpty(strIsClearHistoryBackstack)) {
                return Integer.parseInt(strIsClearHistoryBackstack) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean isRedirectUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            String strIsRedirect = uri.getQueryParameter("isRedirect");
            if (!TextUtils.isEmpty(strIsRedirect)) {
                return Integer.parseInt(strIsRedirect) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addToBackstack(String url) {
        try {
            MUrl mUrl = cachedUrls.get(url);
            if (mUrl == null) {
                mUrl = new MUrl(url);
            }
            addToBackstack(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToBackstack(MUrl url) {
        if (url.getPath().equalsIgnoreCase("about:blank"))
            return;
        try {
            if (historyBackstack.isEmpty()) {
                if (url != null) {
                    historyBackstack.push(url);
                    if (isClearHistoryBackstack(url.getPath())) {
                        historyBackstack.pop();
                    }
                }
            } else {
                Uri uri = url.toUri();
                Uri lastUri = historyBackstack.peek().toUri();
                if (lastUri.getPath() == null) {
                    historyBackstack.pop();
                    addToBackstack(url);
                } else if (!lastUri.getPath().equalsIgnoreCase(uri.getPath())) {
                    if (url != null) {
                        if (isRedirectUrl(url.getPath())) {
                            historyBackstack.pop();
                        }
                        historyBackstack.push(url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
