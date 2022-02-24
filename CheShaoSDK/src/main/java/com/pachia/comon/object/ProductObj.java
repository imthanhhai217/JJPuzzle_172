package com.pachia.comon.object;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ProductObj {
    @SerializedName("product_id")
    private String product_id;

    @SerializedName("order_no")
    private String order_no;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public ProductObj() {
    }

    public ProductObj(String data) {
        getObjectFromData(data);
    }

    public void getObjectFromData(String data) {
        ProductObj obj;
        obj = new Gson().fromJson(data, ProductObj.class);
        setOrder_no(obj.getOrder_no());
        setProduct_id(obj.getProduct_id());
    }
}
