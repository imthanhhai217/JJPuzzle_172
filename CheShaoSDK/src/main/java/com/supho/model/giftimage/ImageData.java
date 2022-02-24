package com.supho.model.giftimage;

import android.graphics.Bitmap;

public class ImageData{
	private String url;
	private Bitmap bitmapData;
	
	public ImageData(){}
	
	public ImageData(String url){
		this.url = url;
	}
	
	public ImageData(String url, Bitmap bitmapData){
		this.url = url;
		this.bitmapData = bitmapData;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Bitmap getBitmapData() {
		return bitmapData;
	}
	public void setBitmapData(Bitmap bitmapData) {
		this.bitmapData = bitmapData;
	}
}