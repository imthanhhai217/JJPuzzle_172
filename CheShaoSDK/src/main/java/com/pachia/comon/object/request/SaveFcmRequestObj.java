package com.pachia.comon.object.request;

import com.google.gson.annotations.SerializedName;

public class SaveFcmRequestObj {
    @SerializedName("regid")
    private String regid;

    @SerializedName("user_id")
    private String user_id;

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
