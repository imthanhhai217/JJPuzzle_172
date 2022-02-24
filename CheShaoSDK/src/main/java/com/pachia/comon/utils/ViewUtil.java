package com.pachia.comon.utils;

import android.view.View;

import com.pachia.comon.constants.Constants;
import com.pachia.comon.object.AuthenConfigObj;

public class ViewUtil {

    public static void applyButton(AuthenConfigObj.LoginConfig cf, View btnFaceBook, View btnPlayNowTop, View btnGoogle, View btnPlayNow, View space, View spaceRight) {
        if (cf == null)
            return;
        if (cf.getLogin_facebook() == Constants.VISIBLE && cf.getLogin_google() == Constants.VISIBLE && cf.getPlay_now() == Constants.VISIBLE) {
            btnFaceBook.setVisibility(View.VISIBLE);
            btnPlayNowTop.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.VISIBLE);
            btnPlayNow.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
            spaceRight.setVisibility(View.GONE);
        } else if (cf.getLogin_facebook() == Constants.HIDE && cf.getLogin_google() == Constants.HIDE && cf.getPlay_now() == Constants.HIDE) {
            btnFaceBook.setVisibility(View.GONE);
            btnPlayNowTop.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.GONE);
            btnPlayNow.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
            spaceRight.setVisibility(View.GONE);
        } else if (cf.getLogin_facebook() == Constants.VISIBLE && cf.getLogin_google() == Constants.VISIBLE && cf.getPlay_now() == Constants.HIDE) {
            btnFaceBook.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
            btnGoogle.setVisibility(View.VISIBLE);
            spaceRight.setVisibility(View.GONE);
            btnPlayNowTop.setVisibility(View.GONE);
            btnPlayNow.setVisibility(View.GONE);
        } else if (cf.getLogin_facebook() == Constants.VISIBLE && cf.getLogin_google() == Constants.HIDE && cf.getPlay_now() == Constants.VISIBLE) {
            btnFaceBook.setVisibility(View.VISIBLE);
            space.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.GONE);
            spaceRight.setVisibility(View.VISIBLE);
            btnPlayNowTop.setVisibility(View.VISIBLE);
            btnPlayNow.setVisibility(View.GONE);
        } else if (cf.getLogin_facebook() == Constants.HIDE && cf.getLogin_google() == Constants.VISIBLE && cf.getPlay_now() == Constants.VISIBLE) {
            btnFaceBook.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.VISIBLE);
            spaceRight.setVisibility(View.VISIBLE);
            btnPlayNowTop.setVisibility(View.VISIBLE);
            btnPlayNow.setVisibility(View.GONE);
        } else if (cf.getLogin_facebook() == Constants.HIDE && cf.getLogin_google() == Constants.HIDE && cf.getPlay_now() == Constants.VISIBLE) {
            btnFaceBook.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.GONE);
            spaceRight.setVisibility(View.GONE);
            btnPlayNowTop.setVisibility(View.GONE);
            btnPlayNow.setVisibility(View.VISIBLE);
        } else if (cf.getLogin_facebook() == Constants.VISIBLE && cf.getLogin_google() == Constants.HIDE && cf.getPlay_now() == Constants.HIDE) {
            btnFaceBook.setVisibility(View.VISIBLE);
            space.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.GONE);
            spaceRight.setVisibility(View.GONE);
            btnPlayNowTop.setVisibility(View.GONE);
            btnPlayNow.setVisibility(View.GONE);
        } else if (cf.getLogin_facebook() == Constants.HIDE && cf.getLogin_google() == Constants.VISIBLE && cf.getPlay_now() == Constants.HIDE) {
            btnFaceBook.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.VISIBLE);
            spaceRight.setVisibility(View.GONE);
            btnPlayNowTop.setVisibility(View.GONE);
            btnPlayNow.setVisibility(View.GONE);
        }
    }
}
