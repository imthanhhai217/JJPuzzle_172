package com.pachia.ui.login;


import com.pachia.comon.presenter.BasePresenter;

public interface ILoginPresenter extends BasePresenter {
    void loginEmail(String user, String pass);

    void loginFtFacebook(String token);

    void loginFtGoogle(String token);

    void loginPlayNow();

    void getAuthenConfig();

    void getUser();

    void getSdkConfig();
}
