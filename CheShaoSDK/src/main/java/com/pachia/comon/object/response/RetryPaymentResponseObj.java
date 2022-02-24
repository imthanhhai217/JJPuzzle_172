package com.pachia.comon.object.response;


import com.google.gson.annotations.SerializedName;
import com.pachia.comon.object.BaseObj;


public class RetryPaymentResponseObj extends BaseObj {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}