package com.supho.utils;

/**
 * Created by Khai Tran on 5/26/2017.
 */
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.quby.R;
import com.supho.model.NtfModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class NotificationUtils {

    public void showNotification() {
        String name = "channel";
        String description = "channel des"; // The user-visible description of the channel.
        Log.i(TAG , "showNotification:title=" + title + ";msg=" + message);
        try {
            Log.i(TAG, "mainClass:" + mainClass);
            if (mainClass == null) {
                try {
                    String className = Preference.getString(context,
                            Constants.SHARED_PREF_MAIN_ACTIVITY);
                    mainClass = Class.forName(className);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Drawable ico = context.getApplicationInfo().loadIcon(context.getPackageManager());
            Bitmap bitmap = Utils.drawableToBitmap(ico);
            int bitmapSize = (int) Math.min(64 * DeviceUtils.getDensity(context), bitmap.getWidth());

            CharSequence appTitle = context.getApplicationInfo().loadLabel(context.getPackageManager());

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.bigText(message);
            bigTextStyle.setSummaryText(appTitle);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = mNotificationManager.getNotificationChannel(title);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(title, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,title)
                    .setWhen(System.currentTimeMillis())
                    .setContentText(message)
                    .setContentTitle(title)
                    .setSmallIcon(getDrawableSmall(context))
                    .setLargeIcon(Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false))
                    .setAutoCancel(true)
                    .setTicker(title)
                    .setNumber(countNtfs(context))
                    .setStyle(bigTextStyle)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, mainClass);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("deep_link", deeplink);

            // The stack builder object will contain an artificial back stack for
            // the started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(mainClass);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 959,
                    resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showNotificationWithImage() {
        String name = "channel with image";
        String description = "channel img des"; // The user-visible description of the channel.
        Log.i(TAG, "showNotification:title=" + title + ";msg=" + message);
        try {
            Log.i(TAG, "mainClass:" + mainClass);
            if (mainClass == null) {
                try {
                    String className = Preference.getString(context,
                            Constants.SHARED_PREF_MAIN_ACTIVITY);
                    mainClass = Class.forName(className);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Drawable ico = context.getApplicationInfo().loadIcon(context.getPackageManager());
            Bitmap bitmap = Utils.drawableToBitmap(ico);
            int bitmapSize = (int) Math.min(64 * DeviceUtils.getDensity(context), bitmap.getWidth());

            //hiendv(24/03/2017): set Data for bigPictureStyle
            Bitmap bigPictureBitmap = Glide.with(context).asBitmap().load(imageUrl).apply(new RequestOptions().centerCrop()).into(512, 256)
                    .get();
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//            bigPictureStyle.bigPicture(Bitmap.createScaledBitmap(bigPictureBitmap, 120, 120, false));
            bigPictureStyle.setSummaryText(message);
            bigPictureStyle.bigPicture(bigPictureBitmap);
            bigPictureStyle.setBigContentTitle(title);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = mNotificationManager.getNotificationChannel(title);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(title, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,title)
                    .setWhen(System.currentTimeMillis())
                    .setContentText(message)
                    .setContentTitle(title)
                    .setSmallIcon(getDrawableSmall(context))
                    .setLargeIcon(Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false))
                    .setAutoCancel(true)
                    .setTicker(title)
                    .setNumber(NotificationUtils.countNtfs(context))
                    .setStyle(bigPictureStyle)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, mainClass);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("deep_link", deeplink);
            // The stack builder object will contain an artificial back stack for
            // the started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(mainClass);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 959,
                    resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getDrawableSmall(Context c) {
        return Res.drawableResource(c, R.drawable.ic_send);
    }
    static ArrayList<NtfModel> listNtf = new ArrayList<>();
    public static ArrayList<NtfModel> getListNtfFromAfterAuth(){
        try {
            return listNtf;
        }catch (Exception e){
            e.printStackTrace();
            return listNtf;
        }

    }
    public static void setListNtfFromAfterAuth(ArrayList<NtfModel> listNtfs){
        try {
            listNtf = listNtfs;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addNtf(Context c, NtfModel ntfModel) {
        try {
            String ntfsString = Preference.getString(c , Constants.SHARED_PREF_NOTIFICATONS);
            ArrayList<NtfModel> listNtfs = new ArrayList<>();
            try {
                if(ntfsString != null || !ntfsString.equals("")){
                    Log.d(TAG , "ntfsString : " + ntfsString);
                    JSONArray ntfs = new JSONArray(ntfsString);
                    if(ntfs.length() > 0){
                        for (int i = 0 ; i < ntfs.length() ; i++){
                            JSONObject obj = new JSONObject();
                            obj = ntfs.getJSONObject(i);
                            NtfModel ntf = new NtfModel();
                            ntf = new Gson().fromJson(obj.toString() , NtfModel.class);
                            listNtfs.add(ntf);
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            listNtfs.add(ntfModel);
            String strNtf = new Gson().toJson(listNtfs);
            Preference.save(c,
                    Constants.SHARED_PREF_NOTIFICATONS,
                    strNtf);
            Log.d(TAG, "NotificationId : " + strNtf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeNtf(Context c, int id) {
        Log.d(TAG, "removeNotificationId" + id);
        try {
            String ntfsString = Preference.getString(c,
                    Constants.SHARED_PREF_NOTIFICATONS);
            JSONArray ntfs = new JSONArray(ntfsString);
            JSONArray newNtfs = new JSONArray();
            ArrayList<NtfModel> newlistNtfs = new ArrayList<>();
            for (int i = 0; i < ntfs.length(); i++) {
                JSONObject jsObj = new JSONObject();
                jsObj = ntfs.getJSONObject(i);
                NtfModel ntf = new NtfModel();
                ntf.setId(jsObj.getInt("id"));
                ntf.setTitle(jsObj.getString("title"));
                ntf.setNoti(jsObj.getBoolean("noti"));

                if (id != ntf.getId()) {
                    newlistNtfs.add(ntf);
                    newNtfs = new JSONArray(new Gson().toJson(newlistNtfs));
                }
            }
            Log.d(TAG, "NotificationId remove():" + newNtfs.toString());
            Preference.save(c,
                    Constants.SHARED_PREF_NOTIFICATONS,
                    newNtfs.toString());
        } catch (JSONException e) {}
    }

    public static boolean hasNtf(Context c) {
        Log.d(TAG, "NTF : " + !getNtfs(c).isEmpty());
        return !getNtfs(c).isEmpty();
    }

    public static boolean hasNtf(Context c, int id) {
        try {
            ArrayList<NtfModel> ntfModels = new ArrayList<>();
            ntfModels = getNtfs(c);
            for (NtfModel ntf:ntfModels) {
                if(ntf.getId() == id){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<NtfModel> getNtfs(Context c) {
        try {
            String ntfsString = Preference.getString(c,
                    Constants.SHARED_PREF_NOTIFICATONS);
            Log.d(TAG ,"ntfsString : " +ntfsString);
            if(ntfsString != null || !ntfsString.equals("")){
                JSONArray ntfs = new JSONArray(ntfsString);
                ArrayList<NtfModel> ntfModels = new ArrayList<>();
                for (int i = ntfs.length() - 1; i >= 0; i--) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = ntfs.getJSONObject(i);
                    NtfModel ntfModel = new NtfModel();
                    ntfModel.setId(jsonObject.getInt("id"));
                    ntfModel.setTitle(jsonObject.getString("title"));
                    ntfModel.setNoti(jsonObject.getBoolean("noti"));
                    ntfModels.add(ntfModel);
                }
                return ntfModels;
            }
            return new ArrayList<NtfModel>();
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<NtfModel>();
        }

    }

    public static int countNtfs(Context c) {
        try {
            return getNtfs(c).size();
        } catch (Exception e) {
            return 0;
        }
    }


    private static String TAG = "NotificationUtils";

    private Class mainClass;
    private Context context;
    private String title;
    private String message;
    private String imageUrl;
    private int smallIcon;
    private String deeplink;
    private String urlDeepLink;
    private String typeLink;
    private String urlBanner;

    private static NotificationUtils instance;


    public NotificationUtils(Context context) {
        this.context = context;
    }

    public static NotificationUtils getInstance(Context context){
        if(instance == null){
            instance = new NotificationUtils(context);
        }
        return instance;
    }

    public NotificationUtils setMainClass(Class mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    public NotificationUtils setImageUrl(String url){
        this.imageUrl = url;
        return this;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public Context getContext() {
        return context;
    }

    public NotificationUtils setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NotificationUtils setTitle(String title) {
        if (title != null) {
            this.title = title;
        } else {
            this.title = "";
        }
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationUtils setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public NotificationUtils setDeeplink(String deeplink) {
        this.deeplink = deeplink;
        return this;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public NotificationUtils setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }
}
