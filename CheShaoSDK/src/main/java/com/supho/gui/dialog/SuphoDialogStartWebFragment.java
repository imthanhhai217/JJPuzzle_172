package com.supho.gui.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.supho.model.MUrl;
import com.supho.ChelShaoHelper;
import com.supho.CheShaoSDK;
import com.supho.gui.SuphoWebViewDetail;
import com.supho.utils.Constants;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.comon.utils.WvUtil;
import com.quby.R;
import com.pachia.comon.js.JsHandler;
import com.supho.utils.Preference;
import com.supho.utils.Res;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SuphoDialogStartWebFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = SuphoDialogWebviewFragment.class.getName();

    // TODO: Rename and change types of parameters
    private Activity activity;
    private SuphoWebViewDetail webView;
    private ProgressBar mProgressView;
    private ImageButton dialogCloseButton;
    public static final String DIALOG_WEBVIEW_TYPE_WEBVIEW_DETAIL = "webview_detail";
    public static final String DIALOG_WEBVIEW_TYPE_ADS = "webview_ads";
    private String type;
    private int orientation;
    private String webview_detail_url;
    private String webview_detail_title;
    private JsHandler jsHandler;
    private boolean saveReaded;
    private View dialogView;
    private int selectButton = 0;
    private DialogInterface.OnDismissListener mOnDismissListener;

    private SuphoDialogWebviewFragment.EventListener eventListeners;

    public interface EventListener {
        void onProgressChanged(WebView view, int progress);

        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);

        //		public boolean shouldOverrideUrlLoading(WebView view, String url);
        void onScrollChanged(WebView view, int l, int t, int oldl, int oldt);

        void onSizeChanged(WebView view, int w, int h, int oldw, int oldh);
    }

    private static HashMap<String, MUrl> cachedUrls;

    {
        cachedUrls = new HashMap<String, MUrl>();
    }

    public SuphoDialogStartWebFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public SuphoDialogStartWebFragment(Activity activity, boolean isShowAds, boolean saveRead) {
        // Constructor cho Ads

        this.activity = activity;
        this.saveReaded = saveRead;
        this.type = DIALOG_WEBVIEW_TYPE_ADS;
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Translucent_FullScreen);
    }

    @SuppressLint("ValidFragment")
    public SuphoDialogStartWebFragment(Activity activity, String title, String url) {
        //COnstructor cho webview detail
        Log.d(TAG, "Reload 1 : " + url);
        this.activity = activity;
        this.webview_detail_url = url;
        this.webview_detail_title = title;
        this.type = DIALOG_WEBVIEW_TYPE_WEBVIEW_DETAIL;
        setType(type);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Translucent_FullScreen);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            orientation = DeviceUtils.getScreenOrientation(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {// Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        if(activity != null && activity.getApplicationContext() != null) {
            LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(
                    mMessageReceiver, new IntentFilter("UploadBitmap"));
        }
        super.onResume();
        handlerBackButton();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            dialogCloseButton = (ImageButton) dialogView.findViewById(R.id.btn_dialog_close);
            mProgressView = (ProgressBar) dialogView.findViewById(R.id.loading_progress);
            webView = (SuphoWebViewDetail) dialogView.findViewById(R.id.dialog_webview);
            //set state back button and forward button

            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            initWebview();
            Log.d(TAG, "onCreateDialog: activity name: " + activity.getClass().getName());

            dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeDialog();

                }
            });
            if (type.equals(DIALOG_WEBVIEW_TYPE_ADS)) {
                showMobAds();
            } else if (type.equals(DIALOG_WEBVIEW_TYPE_WEBVIEW_DETAIL)) {
                dialogCloseButton.setVisibility(View.VISIBLE);
                showWebViewDetail(webview_detail_title, webview_detail_url);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Translucent);
            builder.setView(dialogView);
            CheShaoSDK.getInstance().unRegisterSensor();

            setKeyboardListener();

            SdkConfigObj game = GameConfigs.getInstance().getSdkConfig();
            if (game != null && game.getEx() != null && game.getEx().isShowLogo()) {
                ChelShaoHelper.hideNotiFloatButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialogView = inflater.inflate(R.layout.dialog_after_login, null);
        return dialogView;
    }


    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onDetach() {
        super.onDetach();
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

    private void closeDialog() {
        try {
            if (Preference.getBoolean(activity, Constants.SHARED_PREF_HIDE_FLOAT_BUTTON, true)
                    && type.equals(DIALOG_WEBVIEW_TYPE_ADS)) {
                SdkConfigObj game = GameConfigs.getInstance().getSdkConfig();
                if (game != null && game.getEx() != null && game.getEx().isShowLogo()) {
                    ChelShaoHelper.showNotiFloatButton(game.getEx());
                }
            }
            if (isCheck) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.evaluateJavascript("checkFormData()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d(TAG, "onReceiveValue: " + s);
                        if (s != null) {
                            if (s.contains("1")) {
                                final DialogFragment dialog = new SuphoConfirmDialog(activity,
                                        Res.string(activity, R.string.do_you_want_close),
                                        activity.getString(R.string.ok),
                                        Res.string(activity, R.string.cancel)
                                        , new SuphoConfirmDialog.EventListener() {

                                    @Override
                                    public void onConfirmClick(View v) {
                                        SuphoDialogStartWebFragment.this.dismiss();
                                    }

                                    @Override
                                    public void onCancelClick(View v) {

                                    }

                                });
                                dialog.show(activity.getFragmentManager(), "tag_fragment_confirm");
                            } else {
                                SuphoDialogStartWebFragment.this.dismiss();
                            }
                        }
                    }
                });
            } else {
                SuphoDialogStartWebFragment.this.dismiss();
            }

            if (Preference.getBoolean(activity, "save_ads", false)) {
                Preference.save(activity, "save_ads", false);
                CheShaoSDK.getInstance().showPopup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked"})
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "ClickableViewAccessibility"})
    private void initWebview() {
        try {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.TRANSPARENT);

            webView.setInitialScale((int) DeviceUtils.getDensity(getActivity()));
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLightTouchEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

            //improve webview loading
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            webSettings.setSaveFormData(true);

            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadsImagesAutomatically(true);
            jsHandler = new JsHandler(activity, this);
            webView.addJavascriptInterface(jsHandler, "JsHandler");
            jsHandler.mobAppSDKexecute("mobGetError", "");
            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int progress) {
                    if (eventListeners != null) {
                        eventListeners.onProgressChanged(view, progress);
                    }
                }
            });
            webView.setOnScrollChangedListener(new SuphoWebViewDetail.OnPropertyChangedListener() {

                @Override
                public void onScroll(int l, int t, int oldl, int oldt) {
                    if (eventListeners != null) {
                        eventListeners.onScrollChanged(webView, l, t, oldl, oldt);
                    }
                }

                @Override
                public void onSizeChanged(int w, int h, int ow, int oh) {
                    if (eventListeners != null) {
                        eventListeners.onSizeChanged(webView, w, h, ow, oh);
                    }
                }

            });

            webView.setWebViewClient(new WebViewClient() {
                long startLoadPage = 0, endLoadPage = 0;

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    showProgress(true);
                    String url = request.getUrl().toString();
                    if (Utils.isPlfUrl(url)
                            && !url.contains("appkey=" + GameConfigs.getInstance().getAppKey())) {
                        Log.i(TAG, "loadUrlWithMobHeaders from shouldOverrideUrlLoading() ");
                        loadUrlWithMobHeaders(url);
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    showProgress(true);
                    if (Utils.isPlfUrl(url)
                            && !url.contains("appkey=" + GameConfigs.getInstance().getAppKey())) {
                        Log.i(TAG, "loadUrlWithMobHeaders from shouldOverrideUrlLoading() ");
                        loadUrlWithMobHeaders(url);
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    try {
                        showProgress(true);
                        LogUtils.d(TAG, "onPageStarted: url=" + url);
                        super.onPageStarted(view, url, favicon);
                        if (eventListeners != null) {
                            eventListeners.onPageStarted(view, url, favicon);
                        }
                        startLoadPage = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    showProgress(false);
                    LogUtils.d(TAG, "onPageFinished: ");
                    try {
                        super.onPageFinished(view, url);
                        if (eventListeners != null) {
                            eventListeners.onPageFinished(view, url);
                        }
                        endLoadPage = System.currentTimeMillis();
                        long deltaLoadPage = endLoadPage - startLoadPage;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Log.d(TAG, "onReceivedError: ");
                    try {
                        showProgress(false);
                        selectButton = 4;
                        if (!isConnected()) {
                            dialogView.setVisibility(View.GONE);
                            showDialog();
                        } else {
                            selectButton = 5;
                            dialogView.setVisibility(View.GONE);
                        }
                        showProgress(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    Log.d(TAG, "onReceivedError: ");
                    selectButton = 4;
                    showProgress(false);
                    if (!isConnected()) {
                        dialogView.setVisibility(View.GONE);
                        showDialog();
                    } else {
                        WvUtil.showErrorPage(getActivity(), view, failingUrl);
                    }
                    showProgress(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is used to call the hard back button on the phone screen near the home key.
     *
     * @return If true, you have pressed hard back button
     */
    private void handlerBackButton() {
        try {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (event != null && (keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_UP)) {
                        SdkConfigObj.Pop mobPopup = GameConfigs.getInstance().getPopup();
                        String url = mobPopup.getUrl();
                        if (url !=null && url.contains("btnc=1")) {
                            Log.d(TAG, "handlerBackButton: url " + url);
                            closeDialog();//TODO ko cho close dialog
                        }
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
            Log.d(TAG, "Show " + TAG);
            Intent intent = new Intent(Constants.INTENT_FILTER);
            intent.putExtra("category", "isShowPopupLink");
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        try {
            Log.d(TAG, "Show " + TAG);
            Intent intent = new Intent(Constants.INTENT_FILTER);
            intent.putExtra("category", "hideShowPopupLink");
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try {
            int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
            //xu ly webview khi loading
            //xu ly dialog loading
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isCheck = false;

    private void showWebViewDetail(String title, String url) {
        Log.d(TAG, "onCreateDialog: url " + url);
        try {
            if (url.contains("isCheck=1")) {
                isCheck = true;
//                webView.setIsTextEditor(true);
            } else {
                isCheck = false;
            }
            Log.i(TAG, "loadUrlWithMobHeaders from showWebViewDetail() ");
            loadUrlWithMobHeaders(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMobAds() {
        try {
            SdkConfigObj.Pop mobPopup = GameConfigs.getInstance().getPopup();
            String url = mobPopup.getUrl();
            SdkConfigObj sdk = GameConfigs.getInstance().getSdkConfig();
            if (sdk != null && sdk.getMaintenance() != null && !TextUtils.isEmpty(sdk.getMaintenance().getUrl())) {
                url = sdk.getMaintenance().getUrl();
            }
            if (url.contains("btnc=0") || mobPopup.getCanClose() == 0) {
                dialogCloseButton.setVisibility(View.GONE);
            } else {
                dialogCloseButton.setVisibility(View.VISIBLE);
            }
            loadUrlWithMobHeaders(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void postUrl(MUrl url) {
        postUrl(url.getPath());
    }

    private void postUrl(String url) {
        try {
            webView.postUrl(url, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUrlWithMobHeaders(String url) {
        try {
            Log.d(TAG, "loadUrlWithMobHeaders: " + url);
            if (Utils.isPlfUrl(url)) {
                Context context = activity.getApplicationContext();
                Uri uri = Uri.parse(url);

                setCookieToURLWebview(context, url);
                Map<String, String> extraHeaders = new HashMap<String, String>();
                String token = AuthenConfigs.getInstance().getAccessToken();
                if (token != null && !TextUtils.isEmpty(token))
                    extraHeaders.put(ConstantApi.HEADER_ATHORIZATION, "Bearer " + token);
                webView.stopLoading();
                webView.loadUrl(uri.toString(), extraHeaders);
            } else {
                loadUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCookieToURLWebview(Context context, String url) {
        try {
            HashSet<String> cookies =
                    (HashSet<String>) Preference.getStringSet(context, Constants.SHARED_PREF_COOKIES, new HashSet<String>());
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            for (String cookie : cookies) {
                Log.d(TAG, "loadUrlWithMobHeaders form cookie : " + cookie);
                cookieManager.setCookie(url, cookie);
            }
            cookieSyncManager.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl(String url) {
        try {
            if (isAdded()) {
                webView.stopLoading();
                webView.loadUrl(url);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            try {
                String action = intent.getAction();
                if (action == null) return;
                switch (action) {
                    case "UploadBitmap":
                        try {
                            String result = intent.getExtras().getString("result");
                            Log.d(TAG, "onReceive: " + result);
                            if (webView != null) {
                                String jsFunction = String.format("loadImage('%s');",
                                        result);
                                Log.d(TAG, "onReceive: " + jsFunction);
                                invokeJavascript(jsFunction);
                            } else Log.d(TAG, "onReceive: webview = null");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

                String message = intent.getStringExtra("message");
                Log.d("receiver", "Got message: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void invokeJavascript(String script) {
        webView.loadUrl("javascript:" + script);
    }


    public boolean isConnected() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            LogUtils.d(TAG, "isConn : " + isConnected);
            return isConnected;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            LogUtils.e(TAG, "error : " + ex.getMessage());
            return false;
        }
    }

    public void showDialog() {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setTitle("Mất kết nối");
            dialog.setPositiveButton("Thử lại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isConnected()) {
                        switch (selectButton) {
                            case 1:
                                webView.goBack();
                                break;
                            case 2:
                                webView.goForward();
                                break;
                            case 3:
                                webView.reload();
                                break;
                            case 4:
                                webView.reload();
                                dialogView.setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                webView.reload();
                                dialogView.setVisibility(View.VISIBLE);
                                break;
                            default:
                        }
                        dialog.dismiss();
                    } else {
                        showDialog();
                    }
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (selectButton == 4) {
                        dismiss();
                    }
                }
            });
            dialog.setMessage(getString(R.string.error_network));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final int DefaultKeyboardDP = 100;
    private final int EstimatedKeyboardDP = DefaultKeyboardDP
            + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
    private int heightDiff = 0;
    private boolean wasOpened;

    private void setKeyboardListener() {
        try {

            final View activityRootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);

            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                private final Rect r = new Rect();

                @Override
                public void onGlobalLayout() {
                    // Convert the dp to pixels.
                    int estimatedKeyboardHeight = (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                    // Conclude whether the keyboard is shown or not.
                    activityRootView.getWindowVisibleDisplayFrame(r);
                    heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                    Resources res = activity.getResources();
                    int heightDiffPixel = (int) (TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, heightDiff, res.getDisplayMetrics()));
                    boolean isShown = heightDiff >= estimatedKeyboardHeight;

                    if (isShown == wasOpened) {
                        Log.d("Keyboard state", "Ignoring global layout change...");
                        return;
                    }

                    wasOpened = isShown;

                    if (isShown) {
                        if (webView != null) {
                            webView.getSettings().setJavaScriptEnabled(true);
                            String script = "getHeightKeyBoard(" + orientation + ")";

                            webView.evaluateJavascript(script, new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
//                                        Log.e(TAG , "eo do duoc");
                                }
                            });

                        }
                    } else {
                        webView.getSettings().setJavaScriptEnabled(true);
                        Log.d(TAG, "closeKeyBoard()");
                        String script = "closeKeyBoard()";

                        webView.evaluateJavascript(script, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                            }
                        });
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
