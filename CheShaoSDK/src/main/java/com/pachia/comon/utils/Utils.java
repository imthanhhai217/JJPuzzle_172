package com.pachia.comon.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.view.CustomProgressDialog;
import com.quby.R;
import com.supho.model.MUrl;
import com.supho.utils.Constants;
import com.supho.utils.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static String TAG = Utils.class.getSimpleName();

    public static String getReferrer(Context c) {
        return Preference.getString(c, Constants.SHARED_PREF_REFERRER);
    }

    public static void setReferrer(Context c, String value) {
        Preference.save(c, Constants.SHARED_PREF_REFERRER, value);
    }

    public static String getGameVersion(Context c) {
        if (c == null)
            c = CheShaoSdk.getInstance().getApplication();
        String versionName = null;
        final PackageManager packageManager = c.getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(c.getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = null;
            }
        }
        return versionName;
    }

    public static String getGameVersionCode(Context c) {
        String versionCode = null;
        if (c == null)
            c = CheShaoSdk.getInstance().getApplication();
        final PackageManager packageManager = c.getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(c.getPackageName(), 0);
                versionCode = packageInfo.versionCode + "";
            } catch (PackageManager.NameNotFoundException e) {
                versionCode = null;
            }
        }
        return versionCode;
    }

    public static String getSDKVersion(Context c) {
        return Constants.SDK_VERSION;
    }

    public static String getNetwork(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null)
                return info.getTypeName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isDashboardEnabled(Context c) {
        return PrefManager.getBoolean(c,
                Constants.SHARED_PREF_DB_ENABLED,
                false);
    }

    public static boolean isPlfUrl(MUrl url) {
        return isPlfUrl(url.getPath());
    }

    public static String stringNormalize(String s) {
        try {
            if (s != null) {
                s = Normalizer.normalize(s, Normalizer.Form.NFD);
                String resultString = s.replaceAll("[^\\x00-\\x7F]", "");
                return "" + resultString;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isPlfUrl(String url) {
        try {
            String u = url.toLowerCase(Locale.ENGLISH);
            return u.startsWith("http://")
                    || u.startsWith("https://");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Dialog progressDialog;


    public static void showLoading(Activity mActivity, boolean show) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null)
                    progressDialog = new CustomProgressDialog(mActivity, mActivity.getString(R.string.please_wait));
                progressDialog.setCancelable(false);
                if (progressDialog == null && show) {
                    progressDialog.show();
                } else if (progressDialog != null && show && progressDialog.isShowing())
                    return;
                else if (progressDialog != null && show && !progressDialog.isShowing())
                    progressDialog.show();
                else if (progressDialog == null && !show)
                    return;
                else if (progressDialog != null && !show && progressDialog.isShowing())
                    progressDialog.dismiss();
                else if (progressDialog != null && !show && !progressDialog.isShowing())
                    return;
            }
        });
    }


    public static String getSHACheckSum(String checksum) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(checksum.getBytes());
            byte byteData[] = md.digest();

            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isJSONValid(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return false;
        } else {
            try {
                new JSONObject(jsonString);
            } catch (JSONException ex) {
                return false;
            }
        }
        return true;
    }

    public static boolean canDrawOverApp(Activity a) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Method m = Settings.class.getDeclaredMethod("canDrawOverlays", Context.class);
                m.setAccessible(true); //if security settings allow this
                Boolean o = (Boolean) m.invoke(null, a);
                return o;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        try {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            int width = drawable.getIntrinsicWidth();
            width = width > 0 ? width : 1;
            int height = drawable.getIntrinsicHeight();
            height = height > 0 ? height : 1;

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
        }

        return bitmap;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static long getTime(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = format.parse(date);
            long longtime = d.getTime();
            return longtime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String convertLongToDate(long timeLong) {
        Date d = new Date(timeLong);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(d);
    }
}
