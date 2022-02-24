package com.pachia.comon.object;

import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class PurchaseHistoryObj {
    @SerializedName("charactor_id")
    private String charactor_id;

    @SerializedName("area_id")
    private String area_id;

    @SerializedName("receipt")
    private VerifyPurchaseRequestObj.Receipt receipt;

    @SerializedName("order_no")
    private String order_no;

    @SerializedName("is_send")
    private boolean is_send;

    @SerializedName("account_id")
    private String account_id;


    public String getCharactor_id() {
        return charactor_id;
    }

    public void setCharactor_id(String charactor_id) {
        this.charactor_id = charactor_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public VerifyPurchaseRequestObj.Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(VerifyPurchaseRequestObj.Receipt receipt) {
        this.receipt = receipt;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String parseJson(PurchaseHistoryObj obj) {
        if (obj == null)
            return "{}";
        String userJson = new Gson().toJson(obj);
        return userJson;
    }

    public boolean isIs_send() {
        return is_send;
    }

    public void setIs_send(boolean is_send) {
        this.is_send = is_send;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }
}
