package com.pachia.comon.object.request;

import com.google.gson.annotations.SerializedName;

public class InitalizePuchaseRequestObj {
    @SerializedName("x_request")
    private String request;

    @SerializedName("device_id")
    private String device_id;

    @SerializedName("method")
    private String method;

    @SerializedName("character_id")
    private String character_id;

    @SerializedName("character_name")
    private String character_name;

    @SerializedName("area_id")
    private String area_id;

    @SerializedName("area_name")
    private String area_name;

    @SerializedName("item_no")
    private String item_no;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("payload")
    private String payload;

    @SerializedName("invoice_no")
    private String invoice_no;

    @SerializedName("app_version")
    private String app_version;

    @SerializedName("sdk_version")
    private String sdk_version;

    @SerializedName("device_name")
    private String device_name;

    @SerializedName("device_os")
    private String device_os;

    @SerializedName("resolution")
    private String resolution;

    @SerializedName("network")
    private String network;

    @SerializedName("advertising_id")
    private String advertising_id;

    @SerializedName("appsflyer_id")
    private String appsflyer_id;

    @SerializedName("locale")
    private String locale;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCharacter_name() {
        return character_name;
    }

    public String getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(String character_id) {
        this.character_id = character_id;
    }

    public void setCharacter_name(String character_name) {
        this.character_name = character_name;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSdk_version() {
        return sdk_version;
    }

    public void setSdk_version(String sdk_version) {
        this.sdk_version = sdk_version;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_os() {
        return device_os;
    }

    public void setDevice_os(String device_os) {
        this.device_os = device_os;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getAdvertising_id() {
        return advertising_id;
    }

    public void setAdvertising_id(String advertising_id) {
        this.advertising_id = advertising_id;
    }

    public String getAppsflyer_id() {
        return appsflyer_id;
    }

    public void setAppsflyer_id(String appsflyer_id) {
        this.appsflyer_id = appsflyer_id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
