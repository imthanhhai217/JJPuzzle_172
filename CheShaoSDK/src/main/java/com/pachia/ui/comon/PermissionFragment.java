package com.pachia.ui.comon;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.quby.R;
import com.supho.model.MPermission;
import com.pachia.comon.tracking.CheShaoTracking;

import java.util.ArrayList;
import java.util.Objects;

import static com.supho.CheShaoSDK.getApplicationContext;

public class PermissionFragment extends DialogFragment {
    public static Activity activity;
    public static String PACKAGE_NAME;
    private AppOpsManager.OnOpChangedListener onOpChangedListener = null;
    private Dialog dialog;

    public final static String PERMISSIONS = "permissions";


    private static final int REQUEST_CODE_PERMISSIONS = 20001;

    public static PermissionFragment newInstance(String[] permissions) {
        PermissionFragment fr = new PermissionFragment();

        Bundle args = new Bundle();
        args.putStringArray(PERMISSIONS, permissions);
        fr.setArguments(args);

        return fr;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    @TargetApi(23)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("PermissionFragment", "vào day");
        Bundle args = getArguments();
        if (args != null) {
            Log.d("PermissionFragment", "vào args ! null");
            String[] permisisons = args.getStringArray(PERMISSIONS);
            CheShaoTracking.getInstance().trackStartPermisison();
            requestPermissions(
                    permisisons,
                    REQUEST_CODE_PERMISSIONS);
        } else {
            dismiss();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            CheShaoTracking.getInstance().trackEndPermisison();
            try {
                Log.d("chieuhv", "fail");
                // If request is cancelled, the result arrays are empty.
                ArrayList<MPermission> result = new ArrayList<MPermission>();
                boolean grantDeny = false;
                for (int i = 0; i < grantResults.length; i++) {
                    String permission = permissions[i];
                    int granted = grantResults[i];
                    MPermission m = new MPermission(permission, granted);
                    result.add(m);
                    if (granted == -1) {
                        grantDeny = true;
                    }
                }
                if (!grantDeny) {
                    dialog.dismiss();
                    return;
                }


                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(getString(R.string.text_dashboard_sheet));
                alert.setCancelable(true);
                alert.setNegativeButton("SETTING",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getApplicationContext().getPackageName())));
                                Log.d("chieuhv", "package: " + getApplicationContext().getPackageName());

                            }

                        });

                AlertDialog dialog_card = alert.create();
                Objects.requireNonNull(dialog_card.getWindow()).setGravity(Gravity.BOTTOM);
                dialog_card.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogMore) {
                        dialog.dismiss();
                    }
                });
                dialog_card.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Log.d("qwewqewqqweq", "success: ");
        }

    }


}
