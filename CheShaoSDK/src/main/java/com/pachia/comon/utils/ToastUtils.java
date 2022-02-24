package com.pachia.comon.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.pachia.comon.view.CustomToast;

public class ToastUtils {
    static CustomToast toast;
    public static void showLongToast(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }

    public static void showLengthInfoToast(Context context, String message) {
        if (toast != null)
            toast.cancel();
        toast = new CustomToast(context, CustomToast.TOAST_TYPE.INFO, message);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
