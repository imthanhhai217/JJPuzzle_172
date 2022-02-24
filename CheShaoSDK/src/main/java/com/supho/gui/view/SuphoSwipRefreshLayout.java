package com.supho.gui.view;

import android.content.Context;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by nguye on 4/28/2017.
 */

public class SuphoSwipRefreshLayout extends SwipeRefreshLayout {
    private long refreshTimeout = 15000;
    private Handler handler;
    public Object setColorSchemeResources;

    public SuphoSwipRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        if (refreshing) {
            if (handler == null) {
                handler = new Handler();
                handler.postDelayed(swipeTimeoutRunner, refreshTimeout);
            }
            return;
        }
        if (handler != null) {
            handler.removeCallbacks(swipeTimeoutRunner);
            super.setRefreshing(false);
        }
    }

    Runnable swipeTimeoutRunner = new Runnable() {
        @Override
        public void run() {
            SuphoSwipRefreshLayout.super.setRefreshing(false);
        }
    };

    public void setTimeout(long refreshTimeout) {
        this.refreshTimeout = refreshTimeout;
    }
}
