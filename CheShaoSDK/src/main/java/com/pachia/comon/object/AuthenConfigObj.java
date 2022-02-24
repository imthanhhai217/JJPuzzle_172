package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class AuthenConfigObj extends BaseObj {

    @SerializedName("logo_older")
    private logoOlder logoOlder;
    @SerializedName("login_config")
    private LoginConfig loginConfig;

    @SerializedName("register_config")
    private int registerConfig;

    public AuthenConfigObj.logoOlder getLogoOlder() {
        return logoOlder;
    }

    public void setLogoOlder(AuthenConfigObj.logoOlder logoOlder) {
        this.logoOlder = logoOlder;
    }

    public LoginConfig getLoginConfig() {
        return loginConfig;
    }

    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public int getRegisterConfig() {
        return registerConfig;
    }

    public void setRegisterConfig(int registerConfig) {
        this.registerConfig = registerConfig;
    }

    public static class LoginConfig {
        @SerializedName("play_now")
        private int play_now;
        @SerializedName("login_email")
        private int login_email;
        @SerializedName("login_facebook")
        private int login_facebook;
        @SerializedName("login_google")
        private int login_google;
        @SerializedName("login_apple")
        private int login_apple;

        public int getPlay_now() {
            return play_now;
        }

        public void setPlay_now(int play_now) {
            this.play_now = play_now;
        }

        public int getLogin_email() {
            return login_email;
        }

        public void setLogin_email(int login_email) {
            this.login_email = login_email;
        }

        public int getLogin_facebook() {
            return login_facebook;
        }

        public void setLogin_facebook(int login_facebook) {
            this.login_facebook = login_facebook;
        }

        public int getLogin_google() {
            return login_google;
        }

        public void setLogin_google(int login_google) {
            this.login_google = login_google;
        }

        public int getLogin_apple() {
            return login_apple;
        }

        public void setLogin_apple(int login_apple) {
            this.login_apple = login_apple;
        }

    }

    public static class logoOlder {
        @SerializedName("url")
        private String url;
        @SerializedName("text")
        private String text;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
