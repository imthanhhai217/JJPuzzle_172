package com.supho.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MUrl implements Parcelable {

	private String path;
	private boolean isEcrypted;
	
	public MUrl(String path) {
		this.path = path;
		this.isEcrypted = false;
	}
	
	public MUrl(String path, boolean isEcrypted) {
		this.path = path;
		this.isEcrypted = isEcrypted;
	}
	
	public MUrl(Parcel in) {
		this.path = in.readString();
		this.isEcrypted = in.readByte() != 0;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String url) {
		this.path = url;
	}

	public boolean isEcrypted() {
		return isEcrypted;
	}

	public void setEcrypted(boolean isEcrypted) {
		this.isEcrypted = isEcrypted;
	}
	
	public Uri toUri() {
		try {
			return Uri.parse(this.path);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(path);
		dest.writeByte((byte) (isEcrypted ? 1 : 0));
	}

	public static final Parcelable.Creator<MUrl> CREATOR = new Parcelable.Creator<MUrl>() {

		public MUrl createFromParcel(Parcel in) {
			return new MUrl(in);
		}

		public MUrl[] newArray(int size) {
			return new MUrl[size];
		}
	};
	
}
