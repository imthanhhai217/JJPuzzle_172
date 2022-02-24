package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;

public class UserObj {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("account")
    private Account account;

    public int getId() {
        return id;
    }

    @SerializedName("login_type")
    private String loginType;
    @SerializedName("login_or_register")
    private String loginOrRegister;

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public class Account {
        @SerializedName("account_id")
        private String accountId;

        @SerializedName("account_created")
        private String accountCreated;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountCreated() {
            return accountCreated;
        }

        public void setAccountCreated(String accountCreated) {
            this.accountCreated = accountCreated;
        }
    }

    public String getLoginType() {
        if (loginType == null)
            return "";
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginOrRegister() {
        return loginOrRegister;
    }

    public void setLoginOrRegister(int isRegister) {
        if (isRegister == 0) {
            this.loginOrRegister = "Login Success";
        } else
            this.loginOrRegister = "Register Success";

    }
}
