package com.supho.model.giftimage;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class GifData {
	public interface FetchDataListener{
		void done(GifData gifData);
		void fail(String message);
	}
	
	private final String TAG = "GifData";
	public static GifData parse(String jsonDataString) throws Exception{
		GifData gifData = new GifData();
		JSONObject jsonGifData = new JSONObject(jsonDataString);
		
		ArrayList<ImageData> images = new ArrayList<>();
		JSONArray imagesDataObj = jsonGifData.optJSONArray("images");
		for (int i = 0; i < imagesDataObj.length(); i++) {
			images.add(new ImageData(imagesDataObj.optString(i)));
		}
		gifData.setImages(images);
		
		gifData.setMilisecondsToRefesh(jsonGifData.optLong("millisecondsToRefresh"));
		gifData.setSecondsToRun(jsonGifData.optInt("secondsToRun"));
		return gifData;
	}

	
	private int numberImageLoaded = 0;
	private void increeNumberImageLoaded(){
		this.numberImageLoaded++;
	}
	private void resetNumberImageLoaded(){
		this.numberImageLoaded = 0;
	}
	
	public void fetchFrameImages(Context context, final FetchDataListener fetchDataListener){
		if(images == null){
			return;
		}
		
		final int imageSize = images.size();
		
		for (int i = 0; i < imageSize; i++) {
			final int index = i;
			Glide.with(context).asBitmap().load(images.get(index).getUrl())
			.into(new SimpleTarget<Bitmap>() {

				public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
					try {
//						images.get(index).setBitmapData(bitmap);
						increeNumberImageLoaded();
						if(numberImageLoaded == imageSize){
							resetNumberImageLoaded();
							if(fetchDataListener != null){
								fetchDataListener.done(GifData.this);	
							}
						}
					} catch (Exception e) {
						fetchDataListener.fail(e.getMessage());	
					}
				}

				@Override
				public void onLoadFailed(@Nullable Drawable errorDrawable) {
					if(fetchDataListener != null){
						String errorMessage = "Load image fail! "
								+ " - " + images.get(index).getUrl();
						fetchDataListener.fail(errorMessage);	
					}
				}
			});
		}
	}
	
	private ArrayList<ImageData> images;
	private int secondsToRun;
	private long milisecondsToRefesh;
	private long startTime;
	public ArrayList<ImageData> getImages() {
		return images;
	}
	public void setImages(ArrayList<ImageData> images) {
		this.images = images;
	}
	public int getSecondsToRun() {
		return secondsToRun;
	}
	public void setSecondsToRun(int secondsToRun) {
		this.secondsToRun = secondsToRun;
	}
	public long getMilisecondsToRefesh() {
		return milisecondsToRefesh;
	}
	public void setMilisecondsToRefesh(long milisecondsToRefesh) {
		this.milisecondsToRefesh = milisecondsToRefesh;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
