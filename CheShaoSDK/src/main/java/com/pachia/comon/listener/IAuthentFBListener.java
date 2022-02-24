package com.pachia.comon.listener;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

public interface IAuthentFBListener {
    void onAuthFBSuccess(LoginResult loginResult);

    void onAuthFBFailed(FacebookException error);

    void onAuthFBCancel();
}
