package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseObj implements Serializable {
    @SerializedName(value = "error_code")
    private int status;

    @SerializedName("message")
    private String message;

    public static BaseObj createError(String type, String message) {
        BaseObj baseObj = new BaseObj();
        baseObj.setMessage(message);
        return baseObj;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        if (getStatus() == 0)
            return true;
        return false;
    }


}
