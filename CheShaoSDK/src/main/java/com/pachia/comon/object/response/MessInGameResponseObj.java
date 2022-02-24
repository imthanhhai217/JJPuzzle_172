package com.pachia.comon.object.response;

import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.MessInGameObj;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by dungnv
 */

public class MessInGameResponseObj extends BaseObj {

    @SerializedName("data")
    private ArrayList<MessInGameObj> data;

    public ArrayList<MessInGameObj> getData() {
        return data;
    }

    public void setData(ArrayList<MessInGameObj> data) {
        this.data = data;
    }
}
