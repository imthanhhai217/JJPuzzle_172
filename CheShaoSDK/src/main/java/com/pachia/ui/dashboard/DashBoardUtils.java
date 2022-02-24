package com.pachia.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.PermissionUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.supho.CheShaoSDK;
import com.supho.gui.SuphoImageGaleryActivity;
import com.supho.utils.Res;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.quby.R;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.listener.IAuthentFBListener;
import com.pachia.comon.presenter.BaseView;
import com.pachia.ui.other.GamePresenterImpl;
import com.pachia.ui.other.IGamePresenter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

public class DashBoardUtils {
    private static final String TAG = DashBoardUtils.class.getSimpleName();
    private static DashBoardUtils INSTANCE;

    private DashBoardUtils() {
    }

    public static DashBoardUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DashBoardUtils();
        }
        return INSTANCE;
    }

    /**
     * Open Facebook
     *
     * @param activity
     * @param params
     */
    public void openFanpage(Activity activity, String params) {
        Log.i(TAG, "mobOpenFanPage:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String pageid = obj.getString("pageid");
            try {
                activity.getPackageManager().getPackageInfo(
                        "com.facebook.katana", 0);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("fb://page/" + pageid));
                activity.startActivity(intent);
            } catch (Exception e) {
                openBrowser(
                        activity,
                        Uri.parse("https://m.facebook.com/profile.php?id="
                                + pageid));
            }

        } catch (Exception e) {
            ToastUtils.showLongToast(activity,
                    activity.getResources().getString(R.string.err_server_unresponse));
        }
    }


    /**
     * Open group
     *
     * @param activity
     * @param params
     */
    public void mobOpenFBGroup(Activity activity, String params) {
        Log.i(TAG, "mobOpenGroup:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String groupid = obj.getString("groupid");
            try {
                activity.getPackageManager().getPackageInfo(
                        "com.facebook.katana", 0);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("fb://group/" + groupid));
                activity.startActivity(intent);
            } catch (Exception e) {
                openBrowser(activity,
                        Uri.parse("https://facebook.com/groups/" + groupid));
            }
        } catch (Exception e) {
            ToastUtils.showLongToast(activity,
                    activity.getResources().getString(R.string.err_server_unresponse));
        }
    }

    public void openBrowser(Activity activity, Uri uri) {
        Log.i(TAG, "mobOpenBrowser:" + uri);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showLongToast(activity,
                    activity.getResources().getString(R.string.err_server_unresponse));
        }
    }

    public void openBrowser(Activity activity, String params) {
        Log.i(TAG, "mobOpenBrowser:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String url = obj.getString("url");
            if (url.startsWith("http://") || url.startsWith("https://")) {
                openBrowser(activity, Uri.parse(url));
            } else {
                url = "http://" + url;
                openBrowser(activity, Uri.parse(url));
            }
        } catch (Exception e) {
            ToastUtils.showLongToast(activity,
                    activity.getResources().getString(R.string.err_server_unresponse));
        }
    }

    BaseWebFragment webFragment;
    private HashMap<String, Bitmap> issuePhotos;

    public void selectImage(Activity activity, BaseWebFragment webFragment, String params) {
        try {
            Log.d(TAG, "mobSelectImage 2: " + params);
            if (issuePhotos != null) {
                if (issuePhotos.size() > 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage(activity.getString(R.string.alert_image_validate));
                    alert.setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    Dialog dialog = alert.create();
                    dialog.setCancelable(false);
                    dialog.show();
                    return;
                }
            }
            this.webFragment = webFragment;

            boolean grantedReadStorage = PermissionUtils
                    .hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (!grantedReadStorage) {
                PermissionUtils.requestPermission(CheShaoSDK.currentActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

            }
            if (grantedReadStorage) {
                Bundle bundle = new Bundle();
                int limit = 3;
                if (issuePhotos != null) {
                    limit = 3 - issuePhotos.size();
                }
                bundle.putInt("limit", limit);
                Intent intent = new Intent(activity, SuphoImageGaleryActivity.class);
                intent.putExtra("bundle", bundle);
                activity.startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void deleteImageData(Activity activity, String params) {
        Log.i(TAG, "deleteImageData:" + params);
        if (issuePhotos == null) {
            return;
        }
        if (issuePhotos.isEmpty()) {
            return;
        }
        try {
            JSONObject obj = new JSONObject(params);
            String index = obj.optString("index");

            if (issuePhotos.containsKey(index)) {
                issuePhotos.remove(index);
            }
            Log.d(TAG, "issue photo size:" + issuePhotos.size());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showLongToast(activity,
                    activity.getResources().getString(R.string.err_server_unresponse));
        }
    }

    /**
     * clear image
     */
    public void clearImage() {
        Log.d(TAG, "clearImageData");
        if (issuePhotos == null) {
            return;
        }

        if (issuePhotos.isEmpty()) {
            return;
        }
        issuePhotos.clear();
    }


    public void handleResult(Activity activity, int requestCode,
                             int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PICKER:
                    if (data.getStringExtra("data") != null) {
                        String[] imagesPath = data.getStringExtra("data").split("\\|");
                        for (int i = 0; i < imagesPath.length; i++) {
                            Log.d(TAG, "handleResult: " + imagesPath[i]);
                            handleIssuePhoto(activity, imagesPath[i]);
                        }
                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleIssuePhoto(Activity activity, String imagePath) {
        try {
            Bitmap bitmap = getCompressedBitmapFromPath(activity, imagePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70,
                    byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String key = "img" + Calendar.getInstance().getTimeInMillis();
            String jsFunction = String.format("getImageData('%s', '%s');",
                    encoded, key);
            Log.d(TAG, "js function: " + jsFunction);
            webFragment.invokeJavascript(jsFunction);
            if (issuePhotos == null) {
                issuePhotos = new HashMap<String, Bitmap>();
            }
            issuePhotos.put(key, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getCompressedBitmapFromPath(Activity activity, String imagePath) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            BitmapFactory.decodeFile(imagePath, o);
            final int REQUIRED_SIZE = 75;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFile(imagePath, o2);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
        }
        return bitmap;
    }

    public void upgradeFacebook(final Activity activity, BaseWebFragment frg) {

        FacebookManager.getInstance(activity).startAuth(frg, new IAuthentFBListener() {
            @Override
            public void onAuthFBSuccess(LoginResult loginResult) {
                try {
                    HashMap<String, String> postParams = new HashMap<String, String>();
                    postParams.put("fb_token", loginResult.getAccessToken().getToken());

                    IGamePresenter iGamePresenter = new GamePresenterImpl(new BaseView() {
                        @Override
                        public void showProgress(String message) {
                            Utils.showLoading(activity, true);
                        }

                        @Override
                        public void hideProgress() {
                            Utils.showLoading(activity, false);
                        }

                        @Override
                        public void success(Object x) {
                            if (x instanceof BaseObj) {
                                DialogUtils.showInfoDialog(activity, "", ((BaseObj) x).getMessage(), new DialogUtils.DlgListener() {
                                    @Override
                                    public void onOK() {
                                        if (frg != null)
                                            frg.dismiss();
                                        Log.d(TAG, "onOK: ");

                                    }
                                });
                            }
                        }

                        @Override
                        public void error(Object o) {
                            if (o instanceof BaseObj) {
                                DialogUtils.showErrorDialog(activity, ((BaseObj) o).getMessage());
                            }
                        }
                    });
                    iGamePresenter.connectFaceBook(loginResult.getAccessToken().getToken());
                } catch (Exception e) {
                    handleException(activity, Res.string(activity, R.string.something_went_wrong));
                }
            }

            @Override
            public void onAuthFBFailed(FacebookException error) {
                ToastUtils.showLongToast(activity, activity.getString(R.string.err_connect_facebook));

            }

            @Override
            public void onAuthFBCancel() {
                ToastUtils.showLongToast(activity, activity.getString(R.string.err_connect_facebook));

            }
        });
    }

    private void handleException(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
