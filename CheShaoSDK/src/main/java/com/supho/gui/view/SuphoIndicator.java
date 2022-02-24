package com.supho.gui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.quby.R;

/**
 * Created by khaitran on 9/12/17.
 */

public class SuphoIndicator extends LinearLayout {
    private SparseArray<ImageButton> btnIndicators;
    private LinearLayout layoutIndicators;
    private String TAG = SuphoIndicator.this.getClass().getSimpleName();
    private boolean init = false;
    private View rootView;
    private ViewPager mViewPager;
    public SuphoIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     */
    @SuppressLint("InflateParams")
    private void init(Context context) {
        this.setIndicatorContext(context);
        if (init) {
            return;
        }

        rootView = LayoutInflater.from(context).inflate(
                R.layout.indicator_view, null);

        layoutIndicators = (LinearLayout) rootView.findViewById(R.id.indicator);

        this.addView(rootView);
        init = true;
    }

    public void initialize() {
        Log.d(TAG, "number of indicator: " + numberOfItem);
        if (this.numberOfItem <= 1) {
            this.setVisibility(View.GONE);
            return;
        }
        btnIndicators = new SparseArray<ImageButton>();
        for (int i = 0; i < this.numberOfItem; i++) {
            float density = this.getDensity(indicatorContext);
            int padding = (int) (4 * density);
            ImageButton btnIndicator = new ImageButton(indicatorContext);
            btnIndicator.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            if (i == 0) {
                btnIndicator.setImageResource(R.drawable.indicator_orange);
            } else {
                btnIndicator.setImageResource(R.drawable.indicator_orange_inactive);
            }
            btnIndicator.setBackgroundResource(0);
            btnIndicator.setScaleType(ImageView.ScaleType.FIT_XY);
            btnIndicator.setPadding(padding, 0, padding, 0);
            layoutIndicators.addView(btnIndicator);
            btnIndicators.put(i, btnIndicator);
        }

        this.setVisibility(View.VISIBLE);
    }

    public void updatePosition(int index) {
        if(btnIndicators == null){
            return;
        }
        int itemSize = btnIndicators.size();
        for (int i = 0; i < itemSize; i++) {
            if (i == index) {
                btnIndicators.get(i).setImageResource(
                        R.drawable.indicator_orange);
            } else {
                btnIndicators.get(i).setImageResource(
                        R.drawable.indicator_orange_inactive);
            }
        }
    }

    private float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private int numberOfItem;

    public SuphoIndicator setNumberOfItem(int numberOfItem) {
        this.numberOfItem = numberOfItem;
        return this;
    }

    public SuphoIndicator setViewPager(ViewPager mViewpager) {
        this.mViewPager = mViewpager;
        if (mViewpager != null && mViewpager.getAdapter() != null) {
            this.numberOfItem = mViewPager.getAdapter().getCount();
        }
        mViewPager.addOnPageChangeListener(mInternalPageChangeListener);
        layoutIndicators.removeAllViews();
        initialize();
        return this;
    }

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() <= 0) {
                return;
            }
            else {
                updatePosition(position);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public int getNumberOfItem() {
        return this.numberOfItem;
    }

    private Context indicatorContext;

    public Context getIndicatorContext() {
        return indicatorContext;
    }

    public void setIndicatorContext(Context indicatorContext) {
        this.indicatorContext = indicatorContext;
    }
}
