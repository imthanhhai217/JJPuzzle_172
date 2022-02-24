package com.pachia.comon.listener;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface IAuthentGoogleListener {
    void onAuthGGSuccess(GoogleSignInAccount account, String mToken);

    void onAuthGGFailed(int code, String mess);

    void onAuthGGCancel();
}
