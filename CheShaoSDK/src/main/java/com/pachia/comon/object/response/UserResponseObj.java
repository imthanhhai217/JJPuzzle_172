package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.UserObj;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class UserResponseObj extends BaseObj {

    @SerializedName("data")
    private UserObj data;

    public UserObj getData() {
        return data;
    }

    public void setData(UserObj data) {
        this.data = data;
    }
}
