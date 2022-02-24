package com.pachia.balat.demo.models;

import android.graphics.Bitmap;

public class ImageModel {
    private Bitmap bitmap;
    private String name;
    private Boolean lockState;

    public ImageModel(Bitmap bitmap, String name, Boolean lockState) {
        this.bitmap = bitmap;
        this.name = name;
        this.lockState = lockState;
    }

    public ImageModel() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLockState() {
        return lockState;
    }

    public void setLockState(Boolean lockState) {
        this.lockState = lockState;
    }
}
