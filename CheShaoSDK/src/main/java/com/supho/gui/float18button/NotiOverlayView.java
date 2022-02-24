package com.supho.gui.float18button;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.supho.model.TimerData;
import com.supho.utils.Constants;
import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.LogUtils;
import com.quby.R;
import com.supho.utils.FloatButtonTimerHelper;
import com.supho.utils.Preference;


@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class NotiOverlayView {
	//	private static String TAG = MobFloatOverlayView.class.getSimpleName();
	private static final String TAG = NotiOverlayView.class.getSimpleName();

	private static Activity activity;
	private View rootView, layoutBottom, layoutFloat,layout_root;/*, layoutMenu*/
	private TextView txtDismiss;

	private NotiFloatGestureView floatArea;
	private ImageView floatButton;
	//private View animationLayout;
	private boolean isOutsideBottomArea;
	// Screen
	private int boundWidth, boundHeight;
	private int relativePositionOnScreen;
	private int statusBarHeight;
	//	private int screenWidth, screenHeight;
	private float density;

	private SdkConfigObj.Ex ex;

	//private boolean isShowDialogAgain;

	public NotiOverlayView(Activity a , SdkConfigObj.Ex ex) {
		activity = a;
		this.ex = ex;

		floatArea = new NotiFloatGestureView(activity , ex);

		density = DeviceUtils.getDensity(a);
		statusBarHeight = DeviceUtils.getStatusBarHeightInPixels(activity);

		attachView();
		initView();
		hide();
	}

	public void attachView() {
		try {
			rootView = LayoutInflater.from(activity).inflate(R.layout.noti_float_overlay_view, null);
			layout_root = rootView.findViewById(R.id.layout_root);
			WindowManager windowManager = (WindowManager) activity.getApplicationContext()
					.getSystemService(Activity.WINDOW_SERVICE);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
				WindowManager.LayoutParams params = new WindowManager.LayoutParams(
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						PixelFormat.TRANSPARENT);
				params.gravity = Gravity.LEFT | Gravity.TOP;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
				windowManager.addView(rootView, params);
			} else {
				WindowManager.LayoutParams params = new WindowManager.LayoutParams(
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.TYPE_PHONE,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						PixelFormat.TRANSPARENT);
				params.gravity = Gravity.LEFT | Gravity.TOP;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
				windowManager.addView(rootView, params);
			}
			rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@SuppressLint("NewApi")
				public void onGlobalLayout() {

					if (rootView != null) {
						boundWidth = rootView.getWidth();
						boundHeight = rootView.getHeight();
						floatArea.setMovableWidth(boundWidth);
						floatArea.setMovableHeight(boundHeight);
						LogUtils.d(TAG, "boundWidth:"+boundWidth+"||" + "boundHeight"+ boundHeight +"||");
						int[] locations = new int[2];
						rootView.getLocationOnScreen(locations);
						relativePositionOnScreen = locations[1];
						//floatArea.moveTo(0, 0);
						if (Build.VERSION.SDK_INT < 16) {
							rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						} else {
							rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						}
					}
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void initView() {
		try {
			layoutBottom = rootView.findViewById(R.id.layout_bottom_noti);
			layoutFloat = rootView.findViewById(R.id.layout_float_noti);
			txtDismiss = (TextView) rootView.findViewById(R.id.txt_dismiss_noti);
			floatButton = (ImageView) rootView.findViewById(R.id.img_float_noti); 

			floatArea.setEventListener(mButtonEventListener);
			int x = Preference.getInt(activity , "params_x" , 0);
			int y = Preference.getInt(activity , "params_y" , 0);
			LogUtils.d(TAG , "toado : " + x + ", " + y);
			floatArea.moveTo(x,y);
			txtDismiss.setText(activity.getResources().getString(R.string.dismiss));
			rootView.post(new Runnable() {
	            @Override
	            public void run() {
	            	layoutBottom.setY(boundHeight + statusBarHeight - 125 * density);
	            }
	        } );
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	private NotiFloatGestureView.EventListener mButtonEventListener = new NotiFloatGestureView.EventListener() {

		@Override
		public void onTouchDown(MotionEvent event) {
			isOutsideBottomArea = true;
		}

		@Override
		public void onTouchMove(MotionEvent event) {
			try {
				if (ex.isNeverHide()){
					return;
				}
				if (isInBottomArea(event.getRawX(), event.getRawY())) {
					if (isOutsideBottomArea) {
						showLayoutBottom();
					}
					isOutsideBottomArea = false;
				} else {
					if (!isOutsideBottomArea) {
						hideLayoutBottom();
					}
					isOutsideBottomArea = true;
				}
				if (isInDismissArea(event.getRawX(), event.getRawY())) {
					txtDismiss.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.float_hide_active, 0, 0);
					txtDismiss.setTextColor(Color.rgb(194, 39, 45));
				} else {
					txtDismiss.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.float_hide_normal, 0, 0);
					txtDismiss.setTextColor(Color.rgb(255, 255, 255));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onTouchUp(MotionEvent event) {
			try {
				floatArea.moveToEdge(true);
				if (ex.isNeverHide()){
					return;
				}
				if (event != null) {
					if (isInDismissArea(event.getRawX(), event.getRawY())) {
						hideLayoutBottom();
						layout_root.setVisibility(View.GONE);
						layoutFloat.setVisibility(View.GONE);
						floatArea.hide();
						Intent intent = new Intent(Constants.INTENT_FILTER);
						intent.putExtra("category", "hide_float_button");
						LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
					} else {
						hideLayoutBottom();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onClick(MotionEvent event) {
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onMove(int x, int y) {
			try {
				x = Math.max(0, Math.min(boundWidth- floatArea.getSizeInPixels() , x)); //
				y = Math.max(0, Math.min(boundHeight - relativePositionOnScreen - floatArea.getSizeInPixels(), y)); //

				layoutFloat.setX(x);
				layoutFloat.setY(y);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	};

	@SuppressLint("NewApi")
	private void showCountdownTimer(int visibility){
		try{
			TimerData timerData = FloatButtonTimerHelper.getFloatButtonTimerData(activity);
			if(timerData!=null){
				if(timerData.getListTimerObject().isEmpty()){
					return;
				}
			}
			if(rootView!=null){
				rootView.postInvalidateOnAnimation();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private boolean isInBottomArea(float left, float top) {
		try {
			int dY = (int) (boundHeight - 100 * density);
			return top >= dY || isInDismissArea(left, top);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private boolean isInDismissArea(float left, float top) {
		try {
			int dX = boundWidth / 2;
			int dY = (int) (boundHeight - 50 * density);
			double distance = Math.sqrt(Math.pow(left - dX, 2) + Math.pow(top - dY, 2));
			double maxDistance = Math.min(70 * density, boundWidth / 5);

			return distance <= maxDistance
					|| (top >= dY && left >= dX - maxDistance && left <= dX + maxDistance);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private void showLayoutBottom() {
		try {
			layoutBottom.setVisibility(View.VISIBLE);	
			layoutBottom.animate()
				.setDuration(200)
				.y(boundHeight + statusBarHeight - 125 * density);
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	private void hideLayoutBottom() {
		try {
			layoutBottom.animate()
					.setDuration(200)
					.y(boundHeight + statusBarHeight);
		
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	
	public void show() {
		try {
			layout_root.setVisibility(View.VISIBLE);
			layoutFloat.setVisibility(View.VISIBLE);
			layoutBottom.setVisibility(View.GONE);
			floatArea.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public void hide() {
		try {
			layout_root.setVisibility(View.GONE);
			layoutFloat.setVisibility(View.GONE);
			layoutBottom.setVisibility(View.GONE);
			floatArea.hide();
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public boolean isShowing() {
		try {
			return layoutFloat.getVisibility() == View.VISIBLE;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public void dismiss() {
		try {
			layout_root.setVisibility(View.GONE);
			rootView.setVisibility(View.GONE);
			WindowManager windowManager = (WindowManager) activity.getApplicationContext().getSystemService(
					Activity.WINDOW_SERVICE);
			windowManager.removeView(rootView);
			if (floatArea != null) {
				floatArea.dismiss();
			}
			rootView = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getFloatButtonX() {
		try {
			return floatArea.getX();
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public int getFloatButtonY() {
		try {
			return floatArea.getY();
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
}
