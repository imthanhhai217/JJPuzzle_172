package com.pachia.comon.object.response;

import com.google.gson.annotations.SerializedName;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.InitalizePuchaseObj;


public class InitialPurchaseResponseObj extends BaseObj {
    @SerializedName("data")
    private InitalizePuchaseObj data;


    public InitalizePuchaseObj getData() {
        return data;
    }

    public void setData(InitalizePuchaseObj data) {
        this.data = data;
    }
}
