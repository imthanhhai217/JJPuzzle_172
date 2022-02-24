package com.pachia.comon.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessInGameObj {
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private Integer type;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
