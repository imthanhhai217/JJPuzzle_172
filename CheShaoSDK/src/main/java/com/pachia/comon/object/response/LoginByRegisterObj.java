package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.UserObj;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class LoginByRegisterObj extends BaseObj {

    @SerializedName("data")
    private UserObj data;

    @SerializedName("access_token")
    private String accessToken;


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
        return 1;
    }

    public static LoginByRegisterObj parse(String json) throws Exception {
        Gson gson = new GsonBuilder().create();
        LoginByRegisterObj response = gson.fromJson(json.trim(), LoginByRegisterObj.class);
        return response;
    }

}
