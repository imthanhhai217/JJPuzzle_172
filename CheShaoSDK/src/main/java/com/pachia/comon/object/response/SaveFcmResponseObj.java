package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class SaveFcmResponseObj extends BaseObj {

    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
