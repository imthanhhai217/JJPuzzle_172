package com.supho.gui.float18button;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.LogUtils;
import com.quby.R;
import com.supho.utils.Preference;

@SuppressLint({"InflateParams", "ClickableViewAccessibility"})
public class NotiFloatGestureView {
    private static final String TAG = NotiFloatGestureView.class.getSimpleName();

    private static final int ANIMAION_TIME_FADE_OUT = 300;
    private static final int ANIMAION_TIME_FADE_IN = 200;
    private static final int IDLE_TIME = 4000;
    private static final int IDLE_TIME2 = 1000;
    static final int ANIMAION_TIME_MOVE_TO_EDGE = 200;
    static final int BUTTON_SIZE = 100;

    Activity activity;
    View rootView, floatArea;
    EventListener listener;
    GestureDetector gestureDetector;
    ImageView floatButton;

    int buttonSize;
    // Screen
    int movableWidth, movableHeight;
    float density;
    // Touch event
    long timeTouchDown, timeTouchUp;
    float deltaX, deltaY;

    //Hiện và mờ image 18+
    private Handler idleHandler = new Handler();
    private View animationLayout, layoutFloat;
    private boolean fadeOuted;
    private boolean isHideFloatButton = false;

    WindowManager windowManager;
    WindowManager.LayoutParams params;
    SdkConfigObj.Ex ex;

    public NotiFloatGestureView(Activity a , SdkConfigObj.Ex ex) {
        activity = a;
        this.ex = ex;
        density = DeviceUtils.getDensity(a);
        movableWidth = DeviceUtils.getScreenWidthInPixels(a);
        movableHeight = DeviceUtils.getScreenHeightInPixels(a);
        buttonSize = (int) (BUTTON_SIZE * density);
        gestureDetector = new GestureDetector(a, new GestureListener());

        initView();
        attachView();
    }

    public void initView() {
        rootView = LayoutInflater.from(activity).inflate(R.layout.float_area_noti, null);
        floatArea = rootView.findViewById(R.id.layout_float_noti);
        animationLayout = rootView.findViewById(R.id.ll_image18);
        layoutFloat = rootView.findViewById(R.id.rl_image18);
        floatButton = rootView.findViewById(R.id.img_float_noti);
        floatArea.getLayoutParams().width = buttonSize;
        //floatArea.getLayoutParams().height = buttonSize;
        floatArea.setOnTouchListener(mOnTouchListener);
        if(ex != null){
            if(ex.getLogoUrl() != null){
                String urlIcon = ex.getLogoUrl();
                if(!TextUtils.isEmpty(urlIcon)){
                    Log.d(TAG , "urlIcon : " + urlIcon);
                    RequestOptions options = new RequestOptions()
                            .centerInside()
//                            .override(32 , 32)
                            .placeholder(R.drawable.ic_logo18)
                            .error(R.drawable.ic_logo18)
                            .priority(Priority.HIGH);
                    Glide.with(activity)
                            .load(urlIcon)
                            .apply(options)
                            .into(floatButton);
                }
            }
        }
    }

    public void attachView() {
        try {
            windowManager = (WindowManager) activity.getApplicationContext()
                    .getSystemService(Activity.WINDOW_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSPARENT);
            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSPARENT);
            }
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.x = Preference.getInt(activity , "params_x" , 0);
            params.y = Preference.getInt(activity , "params_y" , 0);
            Log.d(TAG , "Toa do : " + params.x + " , " + params.y);
            windowManager.addView(rootView, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }

    public void setMovableWidth(int width) {
        movableWidth = width;
    }

    public void setMovableHeight(int height) {
        movableHeight = height;
    }

    public int getSize() {
        return BUTTON_SIZE;
    }

    public int getSizeInPixels() {
        return buttonSize;
    }

    public int getX() {
        if (rootView == null) return 0;
        return params.x;
    }

    public int getY() {
        if (rootView == null) return 0;
        return params.y;
    }

    public void moveTo(int x, int y) {
        moveTo(x, y, false);
    }

    public void moveTo(int x, int y, boolean isAnimated) {
        try {
            params.x = x;
            params.y = y;
            windowManager.updateViewLayout(rootView, params);
            if (listener != null) {
                listener.onMove(params.x, params.y);
                listener.onTouchUp(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToEdge(boolean isAnimated) {
        try {
            if (isAnimated) {
                Animation animation = new Animation() {

                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        super.applyTransformation(interpolatedTime, t);
                        try {
                            float left = params.x;
                            float right = movableWidth - buttonSize - left;

                            if (left <= right) {
                                params.x = (int) (left - left * interpolatedTime);
                            } else {
                                params.x = (int) (left - (left - movableWidth) * interpolatedTime);
                            }

                            windowManager.updateViewLayout(rootView, params);
                            if (listener != null) {
                                listener.onMove(params.x, params.y);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                animation.setDuration(ANIMAION_TIME_MOVE_TO_EDGE);
                animation.setInterpolator(new OvershootInterpolator(0.5f));
                floatArea.startAnimation(animation);
            } else {

                float left = params.x;
                float right = movableWidth - buttonSize - left;
                if (left <= right) {
                    params.x = 0;
                } else {
                    params.x = movableWidth - buttonSize;
                }
                windowManager.updateViewLayout(rootView, params);
                if (listener != null) {
                    listener.onMove(params.x, params.y);
                }
            }
            Preference.save(activity , "params_x" , params.x);
            Preference.save(activity , "params_y" , params.y);
//            Log.e(TAG , "toado 2 : " + params.x + " , " + params.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean comsumed = true;
            try {
                gestureDetector.onTouchEvent(event);
                handlerFadeOut();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        timeTouchDown = System.currentTimeMillis();
                        onTouchDown(event);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        onTouchUp(event);
                        break;
                    }
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        onTouchMove(event);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return comsumed;
        }
    };

    private void onTouchDown(MotionEvent event) {
        try {
            fadeIn(true);
            deltaX = event.getX();
            deltaY = event.getY();

            if (listener != null) {
                listener.onTouchDown(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTouchMove(MotionEvent event) {
        try {
            float left = event.getRawX() - deltaX;
            float top = event.getRawY() - deltaY;

            left = Math.max(0, Math.min(movableWidth, left)); //- buttonSize
            top = Math.max(0, Math.min(movableHeight, top)); //- buttonSize

            params.x = (int) left;
            params.y = (int) top;
            rootView.setLayoutParams(params);

            windowManager.updateViewLayout(rootView, params);

            if (listener != null) {
                listener.onTouchMove(event);
                listener.onMove(params.x, params.y);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTouchUp(MotionEvent event) {
        if (listener != null) {
            listener.onTouchUp(event);
        }
    }

    public void show() {
        try {
            isHideFloatButton = false;
            rootView.setVisibility(View.VISIBLE);
            idleHandler.removeCallbacksAndMessages(null);
            fadeIn(false);
            handlerFadeOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        try {
            if (rootView != null) {
                isHideFloatButton = true;
                rootView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            rootView.setVisibility(View.GONE);
            windowManager.removeView(rootView);
            rootView = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface EventListener {
        public void onTouchUp(MotionEvent event);

        public void onTouchDown(MotionEvent event);

        public void onTouchMove(MotionEvent event);

        public void onClick(MotionEvent event);

        public void onMove(int x, int y);
    }

    GestureListener mGestureListener = new GestureListener();

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (listener != null) {
                listener.onClick(e);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }
    }

    //Lúc hiển thị
    public void fadeIn(boolean isAnimated) {
        try {
            idleHandler.removeCallbacksAndMessages(null);
            if (isAnimated) {
                animationLayout.animate()
                        .setDuration(ANIMAION_TIME_FADE_IN)
                        .setInterpolator(new OvershootInterpolator(1f))
                        .alpha(1f);
            } else {
                animationLayout.setAlpha(1f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlerFadeOut() {
        Log.d(TAG, "handlerFadeOut: ");
        if (idleHandler == null)
            idleHandler = new Handler();
        idleHandler.removeCallbacksAndMessages(null);
        idleHandler.postDelayed(mIdleRunnable, IDLE_TIME);
    }

    private Runnable mIdleRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                fadeOut(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //Làm mờ đi sau 5s
    public void fadeOut(boolean isAnimated) {
        LogUtils.d(TAG, "fadeOut 1");
        try {
            if (isAnimated) {
                Log.d(TAG, "GO HERE 1 ");
                animationLayout.clearAnimation();
                animationLayout.animate()
                        .setDuration(ANIMAION_TIME_FADE_OUT)
                        .setInterpolator(new OvershootInterpolator(1f))
                        .setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator arg0) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator arg0) {

                            }

                            @Override
                            public void onAnimationEnd(Animator arg0) {
                                Log.d(TAG, "AnimationLayout X: " + animationLayout.getX());
                                Log.d(TAG, String.format("float layout: %sx%s - animate: %sx%s", layoutFloat.getX(), layoutFloat.getY()
                                        , animationLayout.getX(), animationLayout.getY()));
                                layoutFloat.clearAnimation();
                                animationLayout.clearAnimation();
                                layoutFloat.refreshDrawableState();
                            }

                            @Override
                            public void onAnimationCancel(Animator arg0) {

                            }
                        }).alpha(0.5f);
            } else {
                Log.d(TAG, "GO HERE 2 ");
//				animationLayout.setX(newX);
            }
            fadeOuted = true;
            idleHandler.postDelayed(mIdleRunnable2, IDLE_TIME2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mIdleRunnable2 = new Runnable() {

        @Override
        public void run() {
            fadeOut2(true);
        }
    };

    public void fadeOut2(boolean isAnimated) {
        Log.d(TAG, "fadeOut 2");
        try {
            if (!isHideFloatButton) {
                if (isAnimated) {
                    animationLayout.animate()
                            .setDuration(ANIMAION_TIME_FADE_OUT)
                            .setInterpolator(new OvershootInterpolator(1f))
                            .alpha(0.5f);
                } else {
                    animationLayout.setAlpha(0.5f);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
