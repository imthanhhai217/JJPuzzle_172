package com.pachia.comon.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import com.supho.CheShaoSDK;
import com.supho.gui.SuphoDrawOverAppsFragment;
import com.pachia.ui.comon.PermissionFragment;

public class PermissionUtils {
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;

    public static boolean hasPermission(String permission) {
        if (VERSION.SDK_INT >= 23) {
            Context c = CheShaoSDK.getApplicationContext();
            boolean granted = false;
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                granted = isSystemAlertPermissionGranted(c);
            } else {
                granted = ContextCompat.checkSelfPermission(c, permission)
                        == PackageManager.PERMISSION_GRANTED;
            }
            return granted;
        }
        return true;
    }

    public static void requestPermission(Activity a, String permission) {

        if (permission == Manifest.permission.SYSTEM_ALERT_WINDOW) {
            LogUtils.d("PermissionUtils", "mobSelectImage2:");
            FragmentManager fm = a.getFragmentManager();
            SuphoDrawOverAppsFragment fr = SuphoDrawOverAppsFragment.newInstance();
            fm.beginTransaction()
                    .add(fr, "tag_fragment_permission")
                    .commit();
        } else {

            FragmentManager fm = a.getFragmentManager();
			PermissionFragment fr = PermissionFragment.newInstance(new String[]{permission});
            fm.beginTransaction()
                    .add(fr, "tag_fragment_permission")
                    .commit();
        }
    }

    @TargetApi(VERSION_CODES.M)
    private static boolean isSystemAlertPermissionGranted(Context c) {
        return VERSION.SDK_INT < VERSION_CODES.M || Settings.canDrawOverlays(c);
    }
}
