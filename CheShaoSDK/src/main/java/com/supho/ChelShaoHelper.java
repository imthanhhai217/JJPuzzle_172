package com.supho;

import android.app.Activity;

import com.pachia.comon.object.SdkConfigObj;
import com.pachia.comon.utils.LogUtils;
import com.supho.gui.float18button.NotiOverlayView;

public class ChelShaoHelper {

    private final static String TAG = ChelShaoHelper.class.getSimpleName();
    private static Activity activity = CheShaoSDK.activity;

    static boolean didShowPermissionDialog = false;
    private static NotiOverlayView notiFloatButton;
    public static boolean doHideFloatButton = false;

    public static void showNotiFloatButton(SdkConfigObj.Ex ex) {
        if (ex == null || !ex.isShowLogo())
            return;
        if (!doHideFloatButton) {
            if (notiFloatButton == null) notiFloatButton = new NotiOverlayView(activity, ex);
            notiFloatButton.show();
            LogUtils.i("NotiButton", "Showing");
        }
    }

    public static void hideNotiFloatButton() {
        if (notiFloatButton != null && activity != null && notiFloatButton.isShowing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (notiFloatButton != null) {
                        notiFloatButton.hide();
                    }
                }
            });
        }
        LogUtils.i("NotiButton", "Hiding");
    }

    public static boolean isShowingNotiFloatButton() {
        if (notiFloatButton != null) return notiFloatButton.isShowing();
        return false;
    }
}
