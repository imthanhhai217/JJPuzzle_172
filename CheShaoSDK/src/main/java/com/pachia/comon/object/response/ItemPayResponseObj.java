package com.pachia.comon.object.response;

import com.google.gson.annotations.SerializedName;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.ItemPayObj;

public class ItemPayResponseObj extends BaseObj {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ItemPayObj data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ItemPayObj getData() {
        return data;
    }

    public void setData(ItemPayObj data) {
        this.data = data;
    }
}
