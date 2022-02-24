package com.supho.gui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.supho.gui.float18button.NotiOverlayView;
import com.supho.utils.Constants;
import com.pachia.comon.utils.DeviceUtils;
import com.quby.R;
import com.supho.utils.Preference;

@SuppressLint("ValidFragment")
public class SuphoConfirmDialogWithWebView extends DialogFragment {

    private static String TAG = NotiOverlayView.class.getSimpleName();
    private final String message;
    private final String strConfirm;
    private final String strCancel;
    TextView txtMessage;
    Activity activity;
    EventListener eventListener;
    private WebView webView;
    private String url;

    @SuppressLint("ValidFragment")
    public SuphoConfirmDialogWithWebView(Activity activity, String message, String strConfirm, String strCancel, EventListener eventListener) {
        this.activity = activity;
        this.message = message;
        this.strConfirm = strConfirm;
        this.strCancel = strCancel;
        this.eventListener = eventListener;
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }
    public SuphoConfirmDialogWithWebView(Activity activity, String url, String message, String strConfirm, String strCancel, EventListener eventListener) {
        this.url = url;
        this.activity = activity;
        this.message = message;
        this.strConfirm = strConfirm;
        this.strCancel = strCancel;
        this.eventListener = eventListener;
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Preference.save(activity , Constants.SHARED_PREF_SHOW_DASHBOARD , true);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog_with_webview, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        try {
            txtMessage = (TextView) view.findViewById(R.id.txt_message_w);
            final Button btnConfirm = (Button)view.findViewById(R.id.btn_confirm_w);
            final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel_w);
            webView = (WebView) view.findViewById(R.id.confirm_webview);
            initWebview();
            btnConfirm.setText(strConfirm);
            btnCancel.setText(strCancel);
            txtMessage.setText(message);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eventListener != null) {
                        eventListener.onCancelClick(view);
                    }
                    dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eventListener != null) {
                        eventListener.onConfirmClick(view);
                    }
                    dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @SuppressWarnings({"unchecked"})
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "ClickableViewAccessibility"})
    private void initWebview() {
        try {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

            webView.setInitialScale((int) DeviceUtils.getDensity(getActivity()));
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLightTouchEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(false);

            webSettings.setAllowFileAccess(true);
            webSettings.setDatabaseEnabled(true);

//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadsImagesAutomatically(true);

            //improve webview loading
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setAppCacheEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            webSettings.setSaveFormData(true);

            if(url!=null){
                txtMessage.setVisibility(View.GONE);
                webView.loadUrl(url);
            }else{
                webView.setVisibility(View.GONE);
            }

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        if (eventListener != null) {
            eventListener.onCancelClick(null);
        }
    }

    public void show() {
        try {
            show(activity.getFragmentManager(), TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public interface EventListener {
        public void onCancelClick(View v);

        public void onConfirmClick(View v);
    }

}