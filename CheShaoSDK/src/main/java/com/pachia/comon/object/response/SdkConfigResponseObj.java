package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.SdkConfigObj;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class SdkConfigResponseObj extends BaseObj {

    @SerializedName("data")
    private SdkConfigObj data;

    public SdkConfigObj getData() {
        return data;
    }

    public void setData(SdkConfigObj data) {
        this.data = data;
    }
}
