package com.pachia.ui.login;


import com.pachia.comon.presenter.BaseInteractor;

/**
 * Created by Dungnv
 */

public interface ILoginInteractor extends BaseInteractor {
    void loginEmail(String user, String pass);
    void getAuthenConfig();
    void getUser();
    void loginPlayNow();
    void loginFtFacebook(String token);
    void loginFtGoogle(String token);
    void getSdkConfig();
}
