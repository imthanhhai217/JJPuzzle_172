package com.supho.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khaitran on 9/14/17.
 */

public class NtfModel {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("noti")
    private boolean noti;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getNoti() {
        return noti;
    }

    public void setNoti(boolean noti) {
        this.noti = noti;
    }
}
