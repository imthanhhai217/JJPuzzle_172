package com.supho.gui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.quby.R;
import com.supho.utils.Res;

public class SuphoDrawOverAppsFragment extends DialogFragment {

	public static SuphoDrawOverAppsFragment newInstance() {
		SuphoDrawOverAppsFragment fr = new SuphoDrawOverAppsFragment();
	    return fr;
	}
	
	@TargetApi(23)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = Res.string(getActivity(), R.string.attention_overlay);
		String message = Res.string(getActivity(), R.string.draw_over_apps_message);
		String ok = Res.string(getActivity(), R.string.ok);
		String cancel = Res.string(getActivity(), R.string.cancel);
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String packageName = getActivity().getPackageName();
					    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
					    startActivity(intent);
					}
				})
				.setNegativeButton(cancel, null)
				.create();
		return dialog;
	}

}