package com.pachia.comon.login;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.pachia.comon.listener.IAuthentGoogleListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;

public class GoogleManager {
    public static final int RC_SIGN_IN = 9700;

    private static GoogleManager googleManager;
    private Context context;
    GoogleSignInClient mGoogleSignInClient;
    IAuthentGoogleListener listener;
    private String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    Handler mHandler;
    Runnable runnable;


    public static GoogleManager getInstance(Context context) {
        if (googleManager == null)
            googleManager = new GoogleManager(context);
        return googleManager;
    }

    public GoogleManager(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mHandler = new Handler();

    }


    public void startAuthForResult(Activity activity, @NonNull IAuthentGoogleListener listener) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void startAuthForResult(Fragment fragment, @NonNull IAuthentGoogleListener listener) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void startAuthForResult(DialogFragment dialogFragment, @NonNull IAuthentGoogleListener listener) {
        this.listener = listener;
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                logout();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                dialogFragment.startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        };
        mHandler.postDelayed(runnable, 300);

    }

    public void logout() {
        mGoogleSignInClient.signOut();
    }

    public void onAuthGoogleResult(Intent data, int resultCode) {
        if (listener != null && resultCode == RESULT_CANCELED) {
            listener.onAuthGGCancel();
            return;
        }

        try {
            Task<GoogleSignInAccount> signinTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = signinTask.getResult(ApiException.class);
            if (account != null) {
                // Signed in successfully, show authenticated UI.
                String str = account.getIdToken();
                if (listener != null) {
                    new GetToken(account, SCOPE).execute();
                }
            } else {
                if (listener != null) {
                    listener.onAuthGGFailed(-1, "Google Signin Error");
                }
            }


        } catch (ApiException e) {
            listener.onAuthGGFailed(e.getStatusCode(), e.getMessage());
        }

    }


    public String getToken(String email, String scope) throws IOException, GoogleAuthException {
        return GoogleAuthUtil.getToken(context, email, scope);
    }

    class GetToken extends AsyncTask<Void, Void, String> {
        String mScope;
        GoogleSignInAccount account;

        public GetToken(GoogleSignInAccount account, String scope) {
            this.account = account;
            this.mScope = scope;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                return getToken(account.getEmail(), mScope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (!TextUtils.isEmpty(aVoid) && listener != null)
                    listener.onAuthGGSuccess(account, aVoid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
