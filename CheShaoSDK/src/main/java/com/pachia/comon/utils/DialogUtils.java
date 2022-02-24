package com.pachia.comon.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.pachia.comon.game.CheShaoSdk;
import com.supho.CheShaoSDK;
import com.quby.R;

public class DialogUtils {
    public static AlertDialog cf;

    public static void showExpireDialog(Activity activity) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(false);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(activity.getString(R.string.title_force_logout));
        builder1.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheShaoSDK.getInstance().logout();
                CheShaoSdk.getInstance().logOut(activity);
                System.gc();
                System.exit(0);
            }
        });
        cf = builder1.create();
        cf.show();
    }

    public static void showRetryDialog(Activity activity, String mess, Listener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(false);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.lbl_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
                if (listener != null)
                    listener.onRetry();
            }
        });
        builder1.setNeutralButton(activity.getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.gc();
                System.exit(0);
            }
        });
        cf = builder1.create();
        cf.show();
    }

    public static void showRetryDialogWithLogOut(Activity activity, String mess, Listener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(false);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.lbl_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
                if (listener != null)
                    listener.onRetry();
            }
        });
        builder1.setNeutralButton(activity.getString(R.string.lbl_log_out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheShaoSdk.getInstance().logOut(activity);
                dialog.dismiss();
            }
        });
        cf = builder1.create();
        cf.show();
    }

    public static void showInfoDialog(Activity activity, String title, String mess, DlgListener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        builder1.setCancelable(true);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(title);
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onOK();
                cf.dismiss();
            }
        });
        cf = builder1.create();
        cf.show();

    }

    public static void showInfoDialog(Activity activity, String title, String mess) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        builder1.setCancelable(true);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(title);
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
            }
        });
        cf = builder1.create();
        cf.show();

    }

    public static void showErrorDialog(Activity activity, String mess) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(true);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
            }
        });
        cf = builder1.create();
        cf.show();
    }

    public static void showErrorDialog(Activity activity, String mess, DlgCloseListener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(true);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
                if (listener != null)
                    listener.onClose();
            }
        });
        cf = builder1.create();
        cf.show();
    }

    public static void showErrorInputDialog(Activity activity, String mess, DlgCloseListener listener, DlgCloseListener onDismiss) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(true);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.lbl_play_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
                if (listener != null)
                    listener.onClose();
            }
        });

        builder1.setNegativeButton(activity.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
                if (onDismiss != null)
                    onDismiss.onClose();
            }
        });
        cf = builder1.create();
        cf.show();
    }


    public static void showPaymentRetryDialog(Activity activity, String mess, Listener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setCancelable(false);
        //cf = getDialog(activity);
        if (cf != null && cf.isShowing())
            cf.dismiss();
        builder1.setTitle(activity.getString(R.string.err_title_dialog));
        builder1.setMessage(mess);
        builder1.setPositiveButton(activity.getString(R.string.lbl_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
                if (listener != null)
                    listener.onRetry();
            }
        });
        builder1.setNeutralButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cf.dismiss();
            }
        });
        cf = builder1.create();
        cf.show();
    }

    public static void closeDialog() {
        if (cf != null && cf.isShowing()) {
            cf.dismiss();
        }
    }

    public interface Listener {
        void onRetry();
    }

    public interface DlgListener {
        void onOK();
    }

    public interface DlgCloseListener {
        void onClose();
    }


//    private static ConfirmDialog getDialog(Activity activity) {
//        Fragment fragment = (Fragment) activity.getFragmentManager().findFragmentByTag(ConfirmDialog.class.getSimpleName());
//        if (fragment == null)
//            return ConfirmDialog.newInstance();
//        else {
//            return (ConfirmDialog) fragment;
//        }
//    }
}
