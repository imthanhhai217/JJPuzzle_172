package com.supho.gui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.quby.R;

import java.util.ArrayList;

/**
 * Created by HATIBOY on 9/19/2017.
 */

public class SuphoAutoScrollTextView extends TextSwitcher {

    private static final String TAG = SuphoAutoScrollTextView.class.getName();


/*@Declare Variable*/

    private ArrayList<String> stringArrayList;
    private Long duration;
    private boolean repeat;
    private boolean reverse = true;
    private int i = 0;


/*End @Declare Variable*/

/*@Declare Constructor*/

    public SuphoAutoScrollTextView(Context context) {
        super(context);
    }

    public SuphoAutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

/*End @Declare Constructor*/


/*Getter And Setter*/

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }


    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }


    public void setArrayListText(ArrayList<String> arrayListText) {
        this.stringArrayList = arrayListText;
    }

    public ArrayList<String> getArrayListText() {
        return stringArrayList;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

/*End Getter And Setter*/

    /*start Animation*/
    public synchronized void startAutoScroll(final ArrayList<String> listText, final long duration, final boolean repeat, final boolean reverse, final OnTextViewScrollListener onTextViewScrollListener) {
        //settings
        try {
            Log.d(TAG, "startAutoScroll: Switcher childcount: " + getChildCount());
            if (getChildCount() == 0)
                setFactory(new ViewFactory() {
                    public View makeView() {
                        final TextView myText = new TextView(getContext());
                        myText.setGravity(Gravity.CENTER);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                                Gravity.CENTER);
                        myText.setLayoutParams(params);
                        myText.setTextSize(15);
                        myText.setTextColor(Color.WHITE);
                        return myText;
                    }
                });

            setInAnimation(AnimationUtils.loadAnimation(getContext(),
                    R.anim.push_up_in));
            setOutAnimation(getContext(), R.anim.push_up_out);

            //handler TextView scroll
            long timeCount = (listText.size() + 1) * duration;
            i = 0;
            setText("");
            CountDownTimer countDownTimer = new CountDownTimer(timeCount, duration) {
                public void onTick(long millisUntilFinished) {
                    try {
                        if (onTextViewScrollListener != null)
                            onTextViewScrollListener.onTextViewScroll(listText.get(i), reverse);
                        setText(listText.get(i));
                        Log.d(TAG, "onTick: " + listText.get(i));
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    onTextViewScrollListener.onTextViewScrollFinished();
                    removeAllViews();
                    i = 0;
                }
            };
            countDownTimer.start();
        } catch (Exception e) {
            removeAllViews();
            e.printStackTrace();
        }
    }

    public void startAutoScroll(ArrayList<String> listText, long duration, boolean repeat, OnTextViewScrollListener onTextViewScrollListener) {
        //reverse is up to top
        startAutoScroll(listText, duration, repeat, true, onTextViewScrollListener);
    }

    // call this function for Mob Notification
    public void startAutoScroll(ArrayList<String> listText, long duration, OnTextViewScrollListener onTextViewScrollListener) {
        //reverse is up to top, repeat is true
        startAutoScroll(listText, duration, false, false, onTextViewScrollListener);
    }


    /*Listener*/
    public interface OnTextViewScrollListener {
        void onTextViewScroll(String text, boolean reverse);

        void onTextViewScrollFinished();
    }

}
