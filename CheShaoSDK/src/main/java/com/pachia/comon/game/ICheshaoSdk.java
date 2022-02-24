package com.pachia.comon.game;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.pachia.comon.listener.ILoginListener;
import com.pachia.comon.listener.IMesssageListener;
import com.pachia.comon.listener.IPaymentListener;
import com.pachia.comon.listener.ISaveCharactorListener;
import com.pachia.comon.object.MessInGameObj;

import java.util.ArrayList;

public interface ICheshaoSdk {

    Application getApplication();

    void init(@NonNull Application application, String appkey, String facebookId);

    void login(Activity activity, ILoginListener listener);

    void showFormLogin(Activity activity, ILoginListener listener);

    void logOut(Activity activity);

    void onLoginSuccess(Activity activity, String tab);

    void getMessagesInGame(Activity activity, IMesssageListener messsageListener);

    void payment(Activity activity, String state, IPaymentListener listener);

    void saveCharacter(Activity activity, String roleId, String areaId, ISaveCharactorListener listener);


    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onDestroy();

    void shareImageToFacebook(Activity activity, Bitmap image);

    void showTextScroll(ArrayList<MessInGameObj> listNoti);

    void onRequestPermissionsResult(Activity gameActivity, int requestCode, String[] permissions, int[] grantResults);

    void onWindowFocusChanged(boolean hasFocus);

    void onBackPressed();

    void showDashBoard(Activity activity);
}
