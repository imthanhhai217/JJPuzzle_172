package com.supho.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MPermission implements Parcelable {
	
	private String permission;
	private int granted;
	
	public MPermission() {
	}

	public MPermission(String permission, int granted) {
		this.permission = permission;
		this.granted = granted;
	}
	
	public MPermission(Parcel in) {
        this.permission = in.readString();
        this.granted = in.readInt();
    }

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getGranted() {
		return granted;
	}

	public void setGranted(int granted) {
		this.granted = granted;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(permission);
        dest.writeInt(granted);
    }

    public static final Parcelable.Creator<MPermission> CREATOR = new Parcelable.Creator<MPermission>() {

        public MPermission createFromParcel(Parcel in) {
            return new MPermission(in); 
        }

        public MPermission[] newArray(int size) {
            return new MPermission[size];
        }
    };

}
