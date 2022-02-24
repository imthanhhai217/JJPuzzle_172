package com.pachia.comon.object.request;

import com.google.gson.annotations.SerializedName;

public class VerifyPurchaseRequestObj {
    @SerializedName("order_no")
    private String order_no;

    @SerializedName("receipt")
    private Receipt receipt;

    @SerializedName("method")
    private String method;

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

    public VerifyPurchaseRequestObj() {
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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



    public static class Receipt {
        @SerializedName("data")
        private String data;

        @SerializedName("itemType")
        private String itemType;

        @SerializedName("signature")
        private String signature;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
