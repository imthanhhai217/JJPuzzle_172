package com.supho.gui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.supho.gui.float18button.NotiOverlayView;
import com.supho.utils.Constants;
import com.supho.utils.Preference;
import com.quby.R;
import com.supho.utils.Res;

@SuppressLint("ValidFragment")
public class SuphoFloatConfirmDialog extends DialogFragment {

	private static String TAG = NotiOverlayView.class.getSimpleName();

	Activity activity;
	EventListener eventListener;

	@SuppressLint("ValidFragment")
	public SuphoFloatConfirmDialog(Activity activity, EventListener eventListener) {
		this.activity = activity;
		this.eventListener = eventListener;
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		Preference.save(activity , Constants.SHARED_PREF_SHOW_DASHBOARD , true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.float_dialog, container, false);
		getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
		try {
			TextView txtTitle = (TextView) view.findViewById(R.id.txt_guide);
			ImageView imgGuide = (ImageView) view.findViewById(R.id.img_guide);
			final Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
			final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

			imgGuide.setImageResource(Res.drawableResource(activity, R.drawable.float_hide_tips_animation));
			AnimationDrawable frameAnimation = (AnimationDrawable) imgGuide.getDrawable();
			frameAnimation.start();

			txtTitle.setText(Res.string(activity, R.string.dialog_tips_title));
			btnCancel.setText(Res.string(activity , R.string.cancel));
			btnConfirm.setText(Res.string(activity , R.string.hide));

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

		}catch (Exception e){
			e.printStackTrace();
		}
		return view;
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
		}catch (Exception e){
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