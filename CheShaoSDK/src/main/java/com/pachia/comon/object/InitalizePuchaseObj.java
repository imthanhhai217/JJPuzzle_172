package com.pachia.comon.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InitalizePuchaseObj {
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("order_no")
    @Expose
    private String order_no;
    @SerializedName("game_id")
    @Expose
    private int game_id;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("platform_price")
    @Expose
    private float platform_price;
    @SerializedName("item_value")
    @Expose
    private int item_value;
    @SerializedName("payload")
    @Expose
    private String payload;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("product_id")
    @Expose
    private String product_id;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public float getPlatform_price() {
        return platform_price;
    }

    public void setPlatform_price(float platform_price) {
        this.platform_price = platform_price;
    }

    public int getItem_value() {
        return item_value;
    }

    public void setItem_value(int item_value) {
        this.item_value = item_value;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
