package com.supho.model;

import com.pachia.comon.object.SdkConfigObj;

import java.util.ArrayList;

public class ItemMenuDashboard {

    private int id_menu;
    private String title_menu;
    private ArrayList<SdkConfigObj.SubMenu> subId_menu;
    private String urlIcon;
    private String urlIconActive;
    private boolean isClick;
    private boolean hasNtf;
    private int openType;
    private String openLink;
    private int priority;

    public boolean isHasNtf() {
        return hasNtf;
    }

    public void setHasNtf(boolean hasNtf) {
        this.hasNtf = hasNtf;
    }

    private long endTimer;

    public ArrayList<SdkConfigObj.SubMenu> getSubId_menu() {
        return subId_menu;
    }

    public void setSubId_menu(ArrayList<SdkConfigObj.SubMenu> subId_menu) {
        this.subId_menu = subId_menu;
    }

    public ItemMenuDashboard() {
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public String getTitle_menu() {
        return title_menu;
    }

    public void setTitle_menu(String title_menu) {
        this.title_menu = title_menu;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public String getUrlIconActive() {
        return urlIconActive;
    }

    public void setUrlIconActive(String urlIconActive) {
        this.urlIconActive = urlIconActive;
    }

    public long getEndTimer() {
        return endTimer;
    }

    public void setEndTimer(long endTimer) {
        this.endTimer = endTimer;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public String getOpenLink() {
        return openLink;
    }

    public void setOpenLink(String openLink) {
        this.openLink = openLink;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
