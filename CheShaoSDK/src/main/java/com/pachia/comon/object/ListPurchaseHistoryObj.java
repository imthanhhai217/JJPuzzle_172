package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListPurchaseHistoryObj {
    @SerializedName("charactor_id")
    private ArrayList<PurchaseHistoryObj> data;

    public ArrayList<PurchaseHistoryObj> getData() {
        return data;
    }

    public void setData(ArrayList<PurchaseHistoryObj> data) {
        this.data = data;
    }
}
