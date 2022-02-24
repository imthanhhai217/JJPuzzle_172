package com.pachia.comon.object.response;

import com.pachia.comon.object.AuthenConfigObj;
import com.pachia.comon.object.BaseObj;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class AuthenConfigResponseObj extends BaseObj {

    @SerializedName("data")
    private AuthenConfigObj data;

    public AuthenConfigObj getData() {
        return data;
    }

    public void setData(AuthenConfigObj data) {
        this.data = data;
    }
}
