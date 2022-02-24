package com.pachia.comon.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pachia.comon.game.CheShaoSdk;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pachia.comon.listener.IAuthentFBListener;

import java.security.MessageDigest;
import java.util.Arrays;

public class FacebookManager {
    private static FacebookManager facebookManager;
    private Context context;
    private CallbackManager callbackManager;
    Handler mHandler;
    Runnable runnable;

    public static FacebookManager getInstance(Context context) {
        if (facebookManager == null)
            facebookManager = new FacebookManager(context);
        return facebookManager;
    }

    public FacebookManager(Context context) {
        this.context = context;
    }

    public void init(String applicationId) {
        FacebookSdk.setApplicationId(applicationId);
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(CheShaoSdk.getInstance().getApplication());
        callbackManager = CallbackManager.Factory.create();
        mHandler = new Handler();
    }

    public void startAuth(Fragment fragment, @NonNull IAuthentFBListener listener) {
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
        if (callbackManager == null)
            callbackManager = CallbackManager.Factory.create();
        //logout();
        runnable = new Runnable() {
            @Override
            public void run() {

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                listener.onAuthFBSuccess(loginResult);
                            }

                            @Override
                            public void onCancel() {
                                listener.onAuthFBCancel();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                listener.onAuthFBFailed(error);
                                Log.d(TAG, "onError: " + error.getLocalizedMessage() + " " + error.getMessage());
                            }
                        });
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
                LoginManager.getInstance().logInWithReadPermissions(fragment,
                        Arrays.asList("public_profile", "email"));
            }
        };
        mHandler.postDelayed(runnable, 300);


    }

    private static final String TAG = "startAuth";

    public void startAuth(Activity activity, @NonNull IAuthentFBListener listener) {
        if (mHandler != null && runnable != null) {
            Log.d(TAG, "startAuth: remove callback");
            mHandler.removeCallbacks(runnable);
        }
        logout();
        runnable = new Runnable() {
            @Override
            public void run() {

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                listener.onAuthFBSuccess(loginResult);
                            }

                            @Override
                            public void onCancel() {
                                listener.onAuthFBCancel();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                listener.onAuthFBFailed(error);
                            }
                        });
                LoginManager.getInstance().logOut();
//                LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
                LoginManager.getInstance().logInWithReadPermissions(activity,
                        Arrays.asList("public_profile", "email"));
//                LoginManager.getInstance().logInWithReadPermissions(fragment,
//                        Arrays.asList("public_profile", "email"));
            }
        };
        mHandler.postDelayed(runnable, 300);


    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    public void onAuthResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public String getHashkey(Context c) {
        try {
            String packagename = c.getPackageName();
            PackageInfo info = c.getPackageManager().getPackageInfo(
                    packagename, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (Exception e) {
        }
        return "";
    }

}
