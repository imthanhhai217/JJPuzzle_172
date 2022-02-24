package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.UserObj;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class LoginEmailResponseObj extends BaseObj {

    @SerializedName("data")
    private UserObj data;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("is_register")
    private int isRegister;

    public UserObj getData() {
        return data;
    }

    public void setData(UserObj data) {
        this.data = data;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(int isRegister) {
        this.isRegister = isRegister;
    }
}
