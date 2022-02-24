package com.supho.gui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.supho.model.MUrl;
import com.supho.CheShaoSDK;
import com.supho.utils.Constants;
import com.supho.utils.Preference;

import com.quby.R;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.js.JsHandler;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;

import com.supho.utils.Res;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressLint("NewApi")
public class SuphoDialogInGameFragment extends DialogFragment {

    private static String TAG = SuphoDialogInGameFragment.class.getSimpleName();
    private static final String KEY_URL = "url";
    private static final String KEY_POST_PARAMS = "post_params";
    private Activity activity = CheShaoSDK.activity;
    private View layoutRoot;
    private ProgressBar progessBar;
    private FrameLayout layoutContent;
    //	private ProgressDialog progressBar;
    private SuphoWebView webView;
    private Bundle args;
    private String url;
    private MUrl mUrl;
    private static HashMap<String, MUrl> cachedUrls = new HashMap<>();
    private DialogInterface.OnDismissListener mOnDismissListener;
    private JsHandler jsHandler;

    {
        args = new Bundle();
    }

    public SuphoDialogInGameFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public SuphoDialogInGameFragment(String url) {
        this(new MUrl(url));
    }

    @SuppressLint("ValidFragment")
    public SuphoDialogInGameFragment(MUrl url) {
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
        layoutRoot = inflater.inflate(R.layout.fragment_noti_ingame, container, false);
        layoutContent = layoutRoot.findViewById(R.id.layout_ingame_noti);
        try {
            progessBar = (ProgressBar) layoutRoot.findViewById(R.id.progress_bar);
            webView = (SuphoWebView) layoutRoot.findViewById(R.id.webview);
            initWebView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layoutRoot;
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
//			webView.setBackgroundColor(Color.TRANSPARENT);

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
                }
            });
            webView.loadUrl(url);

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (getActivity() != null) {
//					TODO PersistentConfig.setCookieToURLWebview(webView.getContext(), url);
                    }
                    if (Utils.isPlfUrl(url)
                            && !url.contains("appkey=" + GameConfigs.getInstance().getAppKey())) {
                        try {
                            loadUrlWithLibraryHeaders(url);
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
                        Log.d("check url", "url:" + url + " ; item url:");
                        if (url.contains("appkey")) {
                            url = url + "?appkey="
                                    + GameConfigs.getInstance().getAppKey();
                        }
                    } catch (Exception e) {

                    }
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    Log.d(TAG, "onReceivedError: failingUrl: " + failingUrl);
                    if (!failingUrl.contains("insights.hotjar.com") && !failingUrl.contains("smartlook.com")) {
                        loadErrorPage(failingUrl);
                    }
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

                }

            });

            mUrl = args.getParcelable(KEY_URL);

            HashMap<String, String> postParams = (HashMap<String, String>) args.getSerializable(KEY_POST_PARAMS);
            if (mUrl != null) {
                if (postParams != null) {
                    postUrlWithLibraryHeaders(mUrl, postParams);
                } else {
                    try {
                        loadUrlWithLibraryHeaders(mUrl);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postUrlWithLibraryHeaders(MUrl url, HashMap<String, String> postParams) {
        postUrlWithLibraryHeaders(url, postParams, "");
    }

    public void loadUrlWithLibraryHeaders(String url)
            throws InterruptedException, ExecutionException {
        MUrl mUrl = cachedUrls.get(url);
        if (mUrl == null) {
            mUrl = new MUrl(url);
        }
        loadUrlWithLibraryHeaders(mUrl);
    }

    public void loadUrlWithLibraryHeaders(final MUrl url)
            throws InterruptedException, ExecutionException {

        if (!isAdded()) {
            args.putParcelable(KEY_URL, url);
            return;
        }
        if (Utils.isPlfUrl(url)) {
            Uri uri = url.toUri();
            setCookieToURLWebview(webView.getContext(), url.getPath());
            Map<String, String> extraHeaders = new HashMap<String, String>();
            webView.stopLoading();
            webView.loadUrl(uri.toString(), extraHeaders);
        } else {
            loadUrl(url);
        }
    }

    public void postUrlWithLibraryHeaders(final MUrl url, HashMap<String, String> postParams, String a) {

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
                try {
                    postData += "&" + key + "=" + URLEncoder.encode(postParams.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            setCookieToURLWebview(webView.getContext(), postUrl);
            webView.postUrl(postUrl, postData.getBytes());

        } else {
            postUrl(url);
        }
    }

    private void postUrl(MUrl url) {
        try {
            webView.postUrl(url.getPath(), null);
        } catch (Exception e) {
            e.printStackTrace();
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


    private void loadUrl(MUrl url) {
        try {
            if (isAdded()) {
                args.clear();
                webView.stopLoading();
                webView.loadUrl(url.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadErrorPage() {
        loadErrorPage(null);
    }

    private void loadErrorPage(String failingUrl) {
        try {
            String html = "" +
                    "<!DOCTYPE html>												" +
                    "<html>														" +
                    "	<head>														" +
                    "		<meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/> 															" +
                    "		<meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,user-scalable=no\" /> 										" +
                    "		<style type=\"text/css\">								" +
                    "		html, body {   											" +
                    "			height: 100%;										" +
                    "			padding: 0px;										" +
                    "		}														" +
                    "		body {													" +
                    "			font-family: Tahoma, Geneva, sans-serif;			" +
                    "			font-size: 14px;									" +
                    "			text-align: center;									" +
                    "			line-height: 20px;									" +
                    "			background-color: #fff;								" +
                    "		}														" +
                    "		html,body,a,div,p {										" +
                    "			user-select:none;									" +
                    "			-webkit-user-select:none;							" +
                    "			-moz-user-select:none;								" +
                    "			-webkit-touch-callout: none;						" +
                    "		}														" +
                    "		a {text-decoration:none}								" +
                    "		.btnTryAgain {											" +
                    "			display: block;										" +
                    "			color: #607d8b;										" +
                    "			border: 1px #607d8b solid;							" +
                    "			border-radius: 10px;								" +
                    "			-webkit-border-radius: 10px;						" +
                    "			-moz-border-radius: 10px;							" +
                    "			font-size: 18px;									" +
                    "			font-weight: bold;									" +
                    "			line-height: 32px;									" +
                    "			text-align: center;									" +
                    "			display: block;										" +
                    "			margin: 20px auto;									" +
                    "			width: 128px;										" +
                    "			padding: 5px 20px;									" +
                    "		}														" +
                    "		.alert {												" +
                    "			padding: 0 20px;									" +
                    "			color: #607d8b;										" +
                    "			font-size: 19px;									" +
                    "			font-weight: bold;									" +
                    "		}														" +
                    "		/* Smartphones (landscape) ----------- */				" +
                    "		@media only screen										" +
                    "		and (min-width : 321px) {								" +
                    "			/* Styles */										" +
                    "			.img-alert {										" +
                    "				width: 64px;									" +
                    "			}													" +
                    "		}														" +
                    "		/* Smartphones (portrait) ----------- */				" +
                    "		@media only screen										" +
                    "		and (max-width : 320px) {								" +
                    "			/* Styles */										" +
                    "			.img-alert {										" +
                    "				width: 64px;									" +
                    "			}													" +
                    "		}														" +
                    "		</style>												" +
                    "	</head>														" +
                    "	<body>														" +
                    "		<table height='100%' style='width:100%;margin:0px;'>	" +
                    "			<tr><td valign='center'>							" +
                    "				<img class=\"img-alert\" src=\"data: image/png;base64,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\">	" +
                    "				<p class=\"alert\">" + Res.string(getActivity(), R.string.connection_lost) + "</p>	" +
                    "				<a href=\"[RELOADLINK]\" class=\"btnTryAgain\">" + Res.string(getActivity(), R.string.reload) + "</a>	" +
                    "			</td></tr>											" +
                    "		</table>												" +
                    "	</body>														" +
                    "</html>														";
            webView.loadDataWithBaseURL(null, html.replace("[RELOADLINK]", failingUrl), "text/html", "utf-8", null);
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
                        }
                    });
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}