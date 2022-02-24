package com.supho.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class SuphoWebView2 extends WebView {

//	private static final String TAG = MobWebView.class.getSimpleName();

    private OnPropertyChangedListener mOnScrollChangedCallback;

    public SuphoWebView2(final Context context) {
        super(context);
    }

    public SuphoWebView2(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SuphoWebView2(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null)
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange() - getHeight();
    }

    public OnPropertyChangedListener getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedListener(final OnPropertyChangedListener onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    @Override
    public void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
    }

    public static interface OnPropertyChangedListener {
         void onScroll(int l, int t, int oldl, int oldt);
         void onSizeChanged(int w, int h, int ow, int oh);
    }
}
