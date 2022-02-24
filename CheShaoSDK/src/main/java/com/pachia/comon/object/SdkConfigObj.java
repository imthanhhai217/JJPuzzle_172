package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SdkConfigObj {
    @SerializedName("maintenance")
    public Maintenance maintenance;

    @SerializedName("enable_dashboard")
    public int enableDashboard;

    @SerializedName("menu")
    public ArrayList<Menu> menu;

    @SerializedName("ads")
    public Ads ads;

    @SerializedName("pop")
    public Pop pop;

    @SerializedName("tutorial_level")
    public int tutorialLevel;

    @SerializedName("level")
    public String level;

    @SerializedName("session_out_time")
    public int sessionOutTime;

    @SerializedName("icon18")
    public Ex ex;

    @SerializedName("message_in_game")
    public int messageInGame;

    public int getEnableDashboard() {
        return enableDashboard;
    }

    public void setEnableDashboard(int enableDashboard) {
        this.enableDashboard = enableDashboard;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public Pop getPop() {
        return pop;
    }

    public void setPop(Pop pop) {
        this.pop = pop;
    }

    public int getTutorialLevel() {
        return tutorialLevel;
    }

    public void setTutorialLevel(int tutorialLevel) {
        this.tutorialLevel = tutorialLevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSessionOutTime() {
        return sessionOutTime;
    }

    public void setSessionOutTime(int sessionOutTime) {
        this.sessionOutTime = sessionOutTime;
    }

    public Ex getEx() {
        return ex;
    }

    public void setEx(Ex ex) {
        this.ex = ex;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public int getMessageInGame() {
        return messageInGame;
    }

    public void setMessageInGame(int messageInGame) {
        this.messageInGame = messageInGame;
    }

    public boolean isMessageInGame() {
        return messageInGame == 1 ? true : false;
    }

    public static class GoogleIAB {
        @SerializedName("hash_key")
        public String hashKey;

        public String getHashKey() {
            return hashKey;
        }

        public void setHashKey(String hashKey) {
            this.hashKey = hashKey;
        }
    }

    public static class Menu {
        @SerializedName("id")
        public int id;

        @SerializedName("title")
        public String title;

        @SerializedName("icon")
        public String icon;

        @SerializedName("icon_active")
        public String iconActive;

        @SerializedName("priority")
        public int priority;

        @SerializedName("sub_menu")
        public ArrayList<SubMenu> subMenu;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIconActive() {
            return iconActive;
        }

        public void setIconActive(String iconActive) {
            this.iconActive = iconActive;
        }

        public ArrayList<SubMenu> getSubMenu() {
            return subMenu;
        }

        public void setSubMenu(ArrayList<SubMenu> subMenu) {
            this.subMenu = subMenu;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    public static class SubMenu {

        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("action")
        public String action;

        @SerializedName("priority")
        public int priority;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    public static class Ads {
        @SerializedName("is_show")
        public int isShow;
        @SerializedName("api_key")
        public String apiKey;

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }

    public static class Pop {
        @SerializedName("can_close")
        public int canClose;
        @SerializedName("url")
        public String url;

        public int getCanClose() {
            return canClose;
        }

        public void setCanClose(int canClose) {
            this.canClose = canClose;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean canClose() {
            return canClose == 1;
        }


    }

    public static class Ex {
        @SerializedName("is_show")
        public int showLogo;
        @SerializedName("can_hide")
        public int neverHideEx;
        @SerializedName("url")
        public String logoUrl;
        @SerializedName("text")
        public String text;

        public int getShowLogo() {
            return showLogo;
        }

        public void setShowLogo(int showLogo) {
            this.showLogo = showLogo;
        }

        public int getNeverHideEx() {
            return neverHideEx;
        }

        public void setNeverHideEx(int neverHideEx) {
            this.neverHideEx = neverHideEx;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isShowLogo() {
            return getShowLogo() == 1 ? true : false;
        }

        public boolean isNeverHide() {
            return getNeverHideEx() == 1 ? true : false;
        }
    }

    public static class Maintenance {
        @SerializedName("url")
        public String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


}
