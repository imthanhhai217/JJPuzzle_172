package com.pachia.comon.object;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class VerifyPurchaseObj {

    @SerializedName("order_no")
    public String order_no;

    @SerializedName("status")
    public int status;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("user_id")
    public int user_id;

    @SerializedName("item_value")
    public int item_value;

    @SerializedName("description")
    public String description;

    @SerializedName("character_id")
    public String character_id;

    @SerializedName("platform_price")
    public long platform_price;

    @SerializedName("payload")
    public String payload;

    @SerializedName("is_sandbox")
    public boolean is_sandbox;

    @SerializedName("area_id")
    public String area_id;


    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getItem_value() {
        return item_value;
    }

    public void setItem_value(int item_value) {
        this.item_value = item_value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(String character_id) {
        this.character_id = character_id;
    }

    public long getPlatform_price() {
        return platform_price;
    }

    public void setPlatform_price(long platform_price) {
        this.platform_price = platform_price;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isIs_sandbox() {
        return is_sandbox;
    }

    public void setIs_sandbox(boolean is_sandbox) {
        this.is_sandbox = is_sandbox;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getDataString(VerifyPurchaseObj obj) {
        try {
            String userJson = new Gson().toJson(obj);
            return userJson;
        } catch (Exception e) {
            return "";
        }
    }
}
