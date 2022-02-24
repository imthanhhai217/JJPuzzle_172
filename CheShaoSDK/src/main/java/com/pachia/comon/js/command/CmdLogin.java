package com.pachia.comon.js.command;

import android.app.Activity;
import android.widget.Toast;

import com.pachia.comon.listener.IAuthentFBListener;
import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.ui.other.GamePresenterImpl;
import com.pachia.ui.other.IGamePresenter;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.quby.R;
import com.supho.gui.dialog.SuphoDialogStartWebFragment;
import com.supho.gui.dialog.SuphoDialogWebviewFragment;
import com.supho.utils.Res;

import java.util.HashMap;


public final class CmdLogin {

    private static final String TAG = CmdLogin.class.getSimpleName();
    private static CmdLogin INSTANCE;

    private CmdLogin() {
    }

    public static CmdLogin getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CmdLogin();
        }
        return INSTANCE;
    }

    public void upgradeFacebook(final Activity activity, final SuphoDialogWebviewFragment webDialogFragment, final SuphoDialogStartWebFragment dialogStartWebFragment, String params) {
        FacebookManager.getInstance(activity).startAuth(activity, new IAuthentFBListener() {
            @Override
            public void onAuthFBSuccess(LoginResult loginResult) {
                try {
                    HashMap<String, String> postParams = new HashMap<String, String>();
                    postParams.put("fb_token", loginResult.getAccessToken().getToken());

                    IGamePresenter iGamePresenter = new GamePresenterImpl(new BaseView() {
                        @Override
                        public void showProgress(String message) {
                            Utils.showLoading(activity, true);
                        }

                        @Override
                        public void hideProgress() {
                            Utils.showLoading(activity, false);
                        }

                        @Override
                        public void success(Object x) {
                            if (x instanceof BaseObj) {
                                DialogUtils.showInfoDialog(activity, "", ((BaseObj) x).getMessage(), new DialogUtils.DlgListener() {
                                    @Override
                                    public void onOK() {
                                        if (webDialogFragment != null)
                                            webDialogFragment.dismiss();
                                        if (dialogStartWebFragment != null)
                                            dialogStartWebFragment.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void error(Object o) {
                            if (o instanceof BaseObj) {
                                DialogUtils.showErrorDialog(activity, ((BaseObj) o).getMessage());
                            }
                        }
                    });
                    iGamePresenter.connectFaceBook(loginResult.getAccessToken().getToken());
                } catch (Exception e) {
                    handleException(activity, Res.string(activity, R.string.something_went_wrong));
                }
            }

            @Override
            public void onAuthFBFailed(FacebookException error) {
                ToastUtils.showLongToast(activity,activity.getString(R.string.err_connect_facebook));

            }

            @Override
            public void onAuthFBCancel() {
                ToastUtils.showLongToast(activity,activity.getString(R.string.err_connect_facebook));

            }
        });
    }


    private void handleException(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });


    }

}
