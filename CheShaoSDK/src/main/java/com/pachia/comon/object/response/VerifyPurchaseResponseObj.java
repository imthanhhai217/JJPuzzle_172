package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.VerifyPurchaseObj;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class VerifyPurchaseResponseObj extends BaseObj {
    @SerializedName("data")
    private VerifyPurchaseObj data;


    public VerifyPurchaseObj getData() {
        return data;
    }

    public void setData(VerifyPurchaseObj data) {
        this.data = data;
    }
}
