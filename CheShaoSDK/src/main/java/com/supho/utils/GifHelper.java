package com.supho.utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.pachia.comon.utils.LogUtils;
import com.google.gson.Gson;
import com.supho.model.giftimage.GifData;
import com.supho.model.giftimage.ImageData;

public class GifHelper {
	private static final String TAG = "GifHelper";

	private static void saveGifData(Context context, GifData gifData){
		Preference.remove(context, Constants.SHARED_PREF_GIF);
		Preference.save(context, Constants.SHARED_PREF_GIF
				, new Gson().toJson(gifData));
	}
	
	public static GifData getGifData(Context context){
		String gifDataString = Preference.getString(context, Constants.SHARED_PREF_GIF);
		LogUtils.d("getGifData", gifDataString);
		return new Gson().fromJson(gifDataString, GifData.class);
	}
	
	public static void fetchFramesImage(final Context context, GifData gifData){
		gifDataGlobal = null;
		gifData.fetchFrameImages(context, new GifData.FetchDataListener() {
			
			@Override 
			public void fail(String message) {
				Log.d(TAG, "Fetch image data error: " + message);
			}
			
			@Override
			public void done(GifData gifData) {
				Log.d(TAG, "Fetch gif data done! Saving...");
				saveGifData(context, gifData);
				playGif(context);
			}
		});
	}

	private static int index = 0;
	private static GifData gifDataGlobal;
	public static void playGif(final Context context) {
		if(gifDataGlobal == null){
			gifDataGlobal = getGifData(context);
		}
		final ArrayList<ImageData> imageDatas = gifDataGlobal.getImages();
		final long now = Calendar.getInstance().getTimeInMillis();
		
		runGif(context, now, imageDatas);
	}
	
	private static void runGif(final Context context, final long now
			, final ArrayList<ImageData> imageDatas) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				long runningTime = Calendar.getInstance().getTimeInMillis();
				long totalRunningTime = runningTime - now;
				
				if(totalRunningTime >= gifDataGlobal.getSecondsToRun() * 1000){
					resetGifImage(context, imageDatas.get(0), true);
					return;
				}
				updateNotificationBanner(context, imageDatas.get(index).getUrl(), false);
				
				if(index < imageDatas.size() - 1){
					index++;	
				}else{
					index = 0;
				}
				runGif(context, now, imageDatas);
			}
		}, gifDataGlobal.getMilisecondsToRefesh());
	}

	private static void updateNotificationBanner(Context context, String frameImageUrl, boolean pause ){
		Log.d(TAG, "frameImageUrl: " + frameImageUrl);
		
		if(pause){
			//TODO display pause button 
			return;
		}
		// TODO update frame image
	}
	
	private static void resetGifImage(Context context, ImageData imageData, boolean reset){
		updateNotificationBanner(context, imageData.getUrl(), reset);
	}
}
