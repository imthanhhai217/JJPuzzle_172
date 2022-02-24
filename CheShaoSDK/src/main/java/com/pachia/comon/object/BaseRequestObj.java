package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;

public class BaseRequestObj {

    @SerializedName("appkey")
    private String appKey;

    @SerializedName("signature")
    private String signature;

    public String getAppKey() {
        if (appKey == null) {
            appKey = "436090d39bde404684ca8ea5928498ab";
        }
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSignature() {
        if (signature == null)
            signature = "302fe786ab8b5b5a44e762568f33516dba31e58a2d2d783d506fb19f2f5db8eb";
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
