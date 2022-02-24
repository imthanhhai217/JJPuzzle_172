package com.pachia.comon.listener;

import android.graphics.Bitmap;
import android.webkit.WebView;

public interface IWebViewClientListener {
    void shouldOverrideUrlLoading(WebView view, String url);

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onReceivedError(WebView view, int errorCode,
                         String description, String failingUrl);

    void onPageFinished(WebView view, String url);
}
