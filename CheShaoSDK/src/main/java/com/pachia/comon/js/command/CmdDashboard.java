package com.pachia.comon.js.command;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.share.widget.GameRequestDialog.Result;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.PermissionUtils;
import com.pachia.comon.utils.Utils;
import com.supho.CheShaoSDK;
import com.quby.R;
import com.supho.component.FacebookManager;
import com.supho.component.FacebookManager.InviteGameCallback;
import com.supho.component.FacebookManager.InviteGameContent;
import com.supho.gui.SuphoImageGaleryActivity;
import com.supho.gui.dialog.SuphoDialogWebviewFragment;
import com.supho.utils.Constants;
import com.supho.utils.Res;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * edit date: 30/09/2020
 * by @dungnv
 */
public final class CmdDashboard {

    private static final String TAG = CmdDashboard.class.getSimpleName();
    private static CmdDashboard INSTANCE;
    private FirebaseAnalytics mFirebaseAnalytics;

    private HashMap<String, Object> reminderCache = new HashMap<>();


    private CmdDashboard() {
    }

    public static CmdDashboard getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CmdDashboard();
        }
        return INSTANCE;
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

    public void openContact(Activity activity, String params) {
        Log.i(TAG, "nQENmzOB:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String support_email = obj.getString("support_email");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
                    + support_email));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void mobSendSMS(Activity activity, String params) {
        Log.i(TAG, "SendSMS:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String body = obj.getString("body");
            Intent smsVIntent = new Intent(Intent.ACTION_VIEW);
            // prompts only sms-mms clients
            smsVIntent.setType("vnd.android-dir/mms-sms");
            // extra fields for number and message respectively
            smsVIntent.putExtra("sms_body", body);
            activity.startActivity(smsVIntent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void mobSendEmail(Activity activity, String params) {
        Log.i(TAG, "SendEmail:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String subject = obj.getString("subject");
            String body = obj.getString("body");
            Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
            email.setType("text/plain");
            email.putExtra(Intent.EXTRA_EMAIL, "");
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
            activity.startActivity(Intent.createChooser(email,
                    "Choose an email client from..."));
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }


    public void openBrowser(Activity activity, String params) {
        Log.i(TAG, "OpenBrowser:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String url = obj.getString("url");
            if (url.startsWith("http://") || url.startsWith("https://")) {
                myOpenBrowser(activity, Uri.parse(url));
            }else {
                url = "http://" + url;
                myOpenBrowser(activity, Uri.parse(url));
            }
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void openDialogWebView(Activity activity, String params) {
        Log.i(TAG, "openDialogWebView:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String url = "", title = "";
            if (obj.has("link")) {
                url = obj.getString("link");
            }
            if (obj.has("title")) {
                title = obj.getString("title");
            }
            //thu show dialog webview
            SuphoDialogWebviewFragment dialogWebViewFragment = new SuphoDialogWebviewFragment(activity, title, url);
            dialogWebViewFragment.show(activity.getFragmentManager(), "dialog webview");

            //Hide process bar
            Intent intent = new Intent(Constants.INTENT_FILTER);
//            intent.putExtra(QubyDashboardNewDialog.CLOSE_PROGRESS_BAR, true);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void myOpenBrowser(Activity activity, Uri uri) {
        Log.i(TAG, "myOpenBrowser:" + uri);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void refreshBugForm(Activity activity, String params, SuphoDialogWebviewFragment dialogWebviewFragment) {
        Log.i(TAG, "RefreshBugForm:" + params);
        try {
            dialogWebviewFragment.dismiss();
            if (Utils.isJSONValid(params)) {
                JSONObject jsonObject = new JSONObject(params);
                int isRefresh = 0;
                if (jsonObject.has("is_refresh")) {
                    isRefresh = jsonObject.getInt("is_refresh");
                }

                if (isRefresh == 1) {
                    // refresh support image .
                    Intent intent = new Intent(Constants.INTENT_FILTER);
                    intent.putExtra("category_from_bug", "refresh_from_bug");
                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFBFanpage(Activity activity, String params) {
        Log.i(TAG, "SRAWoDvA:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            String pageid = obj.getString("pageid");
            String callbackUrl = null;
            boolean isSocial = false;
            if (obj.has("callbackUrl")) {
                callbackUrl = obj.getString("callbackUrl");
            }
            if (obj.has("isSocial")) {
                isSocial = obj.getBoolean("isSocial");
            }
            try {
                activity.getPackageManager().getPackageInfo(
                        "com.facebook.katana", 0);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("fb://page/" + pageid));
                activity.startActivity(intent);
            } catch (Exception e) {
                myOpenBrowser(
                        activity,
                        Uri.parse("https://m.facebook.com/profile.php?id="
                                + pageid));
            }
            if (!TextUtils.isEmpty(callbackUrl)) {
                final Activity a = activity;
//                if (!Utils.hasDomain(callbackUrl)) {
//                    callbackUrl = Constants.BASE_URL_DOMAIN + callbackUrl;
//                }
//                final boolean hasSocial = isSocial;
//                new PostTask(callbackUrl, null, false, new PostTask.Callback() {
//
//                    @Override
//                    public void onSuccess(final String response) {
//                        if (!hasSocial) {
//                            Intent intent = new Intent(Constants.INTENT_FILTER);
//                            intent.putExtra("category", "reload");
//                            LocalBroadcastManager.getInstance(a).sendBroadcast(
//                                    intent);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable e) {
//                    }
//                }).execute();
            }

        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void openFBGroup(Activity activity, String params) {
        Log.i(TAG, "bpmyEufN:" + params);
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
                myOpenBrowser(activity,
                        Uri.parse("https://facebook.com/groups/" + groupid));
            }
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void mobOpenModal(Activity activity, String params) {
        Log.i(TAG, "OpenModal:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            Intent intent = new Intent(Constants.INTENT_FILTER);
            intent.putExtra("category", "dashboard");
            intent.putExtra("urls", "[{'action':'" + obj.getString("url")
                    + "'}]");
            LocalBroadcastManager.getInstance(activity.getApplicationContext())
                    .sendBroadcast(intent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void makePhoneCall(Activity activity, String params) {
        Log.i(TAG, "makePhoneCall:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + obj.getString("number")));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }


    @SuppressWarnings("deprecation")
    public static void mobCopyToClipboard(Activity activity, String params) {
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            JSONObject obj = new JSONObject(params);
            String data = obj.getString("data").trim();
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(data);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText("clipboard", data);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(activity, Res.string(activity, R.string.copied),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }

    private SuphoDialogWebviewFragment webFragment;
    private HashMap<String, Bitmap> issuePhotos;

    public void getIssue(Activity activity, SuphoDialogWebviewFragment webFragment,
                            String params) {
        Log.i(TAG, "getIssue:" + params);
        try {
            JSONObject obj = new JSONObject(params);
            long issueId = Long.parseLong(obj.getString("id"));
            if (issueId != 0) {
                if (issuePhotos != null) {
                    ArrayList<Bitmap> listBitmap = new ArrayList<>();
                    for (Entry<String, Bitmap> entry : issuePhotos.entrySet()) {
                        String key = entry.getKey();
                        Bitmap bitmap = entry.getValue();
                        listBitmap.add(bitmap);
                    }
//                    if (listBitmap.size() > 0) {
//                        new UploadListBitmapTask(activity, Constants.URL_UPLOAD_ISSUE_IMAGE
//                                + "?id=" + issueId, listBitmap).execute();
//                    }
                }
                issuePhotos = null;
            }
        } catch (Exception e) {
            Toast.makeText(activity,
                    Res.string(activity, R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show();
        }
    }


    // clear all photo if user exit report view or submit data error
    public void clearImageData(Activity activity) {
        Log.d(TAG, "clearImageData");
        if (issuePhotos == null) {
            return;
        }

        if (issuePhotos.isEmpty()) {
            return;
        }
        issuePhotos.clear();
    }

    // TODO file choose
    public void selectImage(Activity activity, SuphoDialogWebviewFragment webFragment, String params) {
        try {
            Log.d(TAG, "selectImage: " + params);
            if (issuePhotos != null) {
                if (issuePhotos.size() > 2) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage(Res.string(activity, R.string.alert_image_validate));
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
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                activity.startActivityForResult(
                        Intent.createChooser(intent, "Choose photo"),
                        Constants.REQUEST_CODE_PICKER);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectImage2(Activity activity, SuphoDialogWebviewFragment webFragment, String params) {

//        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
//        alert.setMessage(Res.string(activity,R.string.text_dashboard_sheet));
//        alert.setNegativeButton("SETTING",
//                new DialogInterface.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.M)
//                    @Override
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getApplicationContext().getPackageName())));
//                        Log.d("chieuhv","package: "+getApplicationContext().getPackageName());
//
//                    }
//                });
//
//        AlertDialog dialog_card = alert.create();
//        Objects.requireNonNull(dialog_card.getWindow()).setGravity(Gravity.BOTTOM);
//        dialog_card.show();
//        return;

        try {
            Log.d(TAG, "selectImage 2: " + params);
            if (issuePhotos != null) {
                if (issuePhotos.size() > 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage(Res.string(activity, R.string.alert_image_validate));
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

    public void chooseFriend(final Activity activity, String params) {
        // TODO Auto-generated method stub
        try {
            JSONObject obj = new JSONObject(params);
            String callbackUrl = null;
            if (obj.has("callbackUrl")) {
                callbackUrl = obj.getString("callbackUrl");
            }
            final String url = callbackUrl;
            InviteGameContent parameter = new InviteGameContent();
            parameter.setTitle("Invite friend play game");
            parameter.setMessage("Play game with me");
            FacebookManager.getInstance().inviteGameRequest(activity, parameter, new InviteGameCallback() {

                @Override
                public void onSuccess(Result data) {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "invite onSuccess");
                    List<String> list = data.getRequestRecipients();
                    HashMap<String, String> maps = new HashMap<>();
                    String str = "";

                    for (int i = 0; i < list.size(); i++) {
                        str = str + list.get(i) + ",";
                    }
                    Log.d("STR", "str:" + str);
                    String id = null;
                    if (GameConfigs.getInstance().getUser() != null && GameConfigs.getInstance().getUser().getAccount() != null) {
                        id = GameConfigs.getInstance().getUser().getAccount().getAccountId();
                        maps.put("account_id", id);
                    }
                    maps.put("fbid_friends", str);
//                    new PostTask(url, maps, true, true, id, new PostTask.Callback() {
//                        @Override
//                        public void onSuccess(String response) {
//                            // TODO Auto-generated method stub
//
//                        }
//
//                        @Override
//                        public void onFailure(Throwable e) {
//                            // TODO Auto-generated method stub
//                        }
//                    }).execute();
                }

                @Override
                public void onError(Throwable t) {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "invite onError");
                }

                @Override
                public void onCancel() {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "invite onCancel");
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void trackingDetailWeb(Activity activity, String params) {
        try {
            Log.d(TAG, "params : " + params);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
            JSONObject json = new JSONObject(params);
            String typeEvent = json.getString("type");
            String title = json.getString("title");
            Bundle bundle = new Bundle();
            bundle.putString("type", typeEvent);
            bundle.putString("title", title);
            mFirebaseAnalytics.logEvent(Constants.STR_EVENT_TRACK_DETAIL_WEB, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trackingFromWeb(Activity activity, String params) {
        try {
            Log.d(TAG, "label : params - " + params);

            JSONObject json = new JSONObject(params);
            String label = json.getString("label");
            String value = json.getString("value");
            JSONObject jsonChild = new JSONObject(value);
            String name = jsonChild.getString("name");
            Log.d(TAG, "label :" + label + " , name : " + name);
            CheShaoTracking.getInstance().trackFromWeb(label, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case Constants.REQUEST_CALENDAR_PERMISSION: {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        String startDate = Utils.convertLongToDate((long) reminderCache.get("eventStartTimeInMiliSec"));
                        jsonObject.put("start_date", startDate);
                        String endDate = Utils.convertLongToDate((long) reminderCache.get("eventEndTimeInMiliSec"));
                        jsonObject.put("end_date", endDate);
                        jsonObject.put("event_title", String.valueOf(reminderCache.get("eventTitle")));
                        jsonObject.put("description", String.valueOf(reminderCache.get("desc")));
                        jsonObject.put("deeplink", String.valueOf(reminderCache.get("deeplink")));
                        setUpReminder(activity, jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.noti_after_user_deny_permission), Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpReminder(Activity activity, String params) {
        try {
            long eventEndTimeInMiliSec = 0, eventStartTimeInMiliSec = 0;
            String eventTitle = "", desc = "", deeplink = "";
            Log.d(TAG, "eventEndTime 2: " + params);
            JSONObject jsonObject = new JSONObject(params);
            if (jsonObject.has("start_date")) {
                eventStartTimeInMiliSec = Long.parseLong(jsonObject.getString("start_date"));
            }
            if (jsonObject.has("end_date")) {
                eventEndTimeInMiliSec = Long.parseLong(jsonObject.getString("end_date"));
            }
            if (jsonObject.has("event_title")) {
                eventTitle = jsonObject.getString("event_title");
            }
            if (jsonObject.has("deeplink")) {
                deeplink = jsonObject.getString("deeplink");
            }
            if (jsonObject.has("description")) {
                desc = jsonObject.getString("description");
            }
            Log.d(TAG, "eventEndTime : " + eventEndTimeInMiliSec + " , currentTime : " + System.currentTimeMillis());
            if (eventEndTimeInMiliSec <= System.currentTimeMillis()) {
                Toast.makeText(activity, activity.getResources().getString(R.string.event_end), Toast.LENGTH_SHORT).show();
                return;
            }
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, Constants.REQUEST_CALENDAR_PERMISSION);
                if (reminderCache != null) {
                    reminderCache.clear();
                    reminderCache = new HashMap<>();
                    reminderCache.put("eventTitle", eventTitle);
                    reminderCache.put("eventStartTimeInMiliSec", eventStartTimeInMiliSec);
                    reminderCache.put("eventEndTimeInMiliSec", eventEndTimeInMiliSec);
                    reminderCache.put("desc", desc);
                    reminderCache.put("deeplink", deeplink);
                }

                return;
            }
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1);
            eventValues.put("title", eventTitle);
            if (desc != null)
                eventValues.put("description", desc + " Open link from browser: " + deeplink);
            else
                eventValues.put("description", "Open link from browser: " + deeplink);

            eventValues.put("eventLocation", Utils.getApplicationName(activity));
            eventValues.put("dtstart", eventStartTimeInMiliSec);
            eventValues.put("dtend", eventEndTimeInMiliSec);
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            eventValues.put("eventTimezone", tz.getDisplayName());
            eventValues.put("hasAlarm", 1);
            Uri eventUri = activity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            assert eventUri != null;
            long eventID = Long.parseLong(eventUri.getLastPathSegment());

            String reminderUriString = "content://com.android.calendar/reminders";
            ContentValues reminderValues = new ContentValues();
            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5);
            reminderValues.put("method", 1);
            activity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(reminderUriString), reminderValues);
            Toast.makeText(activity, activity.getResources().getString(R.string.event_scheduled), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.calendar_not_found), Toast.LENGTH_SHORT).show();
        }
    }

}
