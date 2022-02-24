package com.pachia.ui.login;

import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.pachia.comon.api.ApiUtils;
import com.pachia.comon.api.ErrorCode;
import com.pachia.comon.api.RetrofitClient;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.BaseDialogFragment;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.listener.IAuthentFBListener;
import com.pachia.comon.listener.IAuthentGoogleListener;
import com.pachia.comon.listener.ILoginListener;
import com.pachia.comon.login.FacebookManager;
import com.pachia.comon.login.GoogleManager;
import com.pachia.comon.object.AuthenConfigObj;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.UserObj;
import com.pachia.comon.object.err.AuthenConfigErrObj;
import com.pachia.comon.object.err.LoginEmailErrObj;
import com.pachia.comon.object.err.LoginFacebookErrObj;
import com.pachia.comon.object.err.LoginGGErrObj;
import com.pachia.comon.object.err.LoginPlayNowErrObj;
import com.pachia.comon.object.err.SdkConfigErrObj;
import com.pachia.comon.object.response.AuthenConfigResponseObj;
import com.pachia.comon.object.response.LoginByRegisterObj;
import com.pachia.comon.object.response.LoginEmailResponseObj;
import com.pachia.comon.object.response.LoginFacebookResponseObj;
import com.pachia.comon.object.response.LoginGoogleResponseObj;
import com.pachia.comon.object.response.LoginPlayNowResponseObj;
import com.pachia.comon.object.response.SdkConfigResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.KeyboardHeightObserver;
import com.pachia.comon.utils.KeyboardHeightProvider;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.NetworkUtils;
import com.pachia.comon.utils.StringUtils;
import com.pachia.ui.login.term.TermDialogFragment;
import com.pachia.ui.register.RegistryFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.quby.R;
import com.supho.CheShaoSDK;

import java.util.Objects;


public class LoginFragment extends BaseDialogFragment implements View.OnClickListener,
        BaseView, IAuthentFBListener, IAuthentGoogleListener, KeyboardHeightObserver {

    ImageButton btnFaceBook, btnPlayNow, btnGoogle;
    TextInputEditText edtLoginPassword, edtLoginAccount;
    ImageButton btnClose;
    TextView btnLogin, tvTerm, tvPlayTimeNoti, tvRegister, btnLoginCenter;
    ImageView iv18Plus;
    ILoginPresenter loginPresenter;
    LinearLayout termContent, layout_content_top, contentIv18, layout_content_bottom, btnLoginFacebook2, btnGoogle2, btnLoginPlayNow2;
    ConstraintLayout layout_content_mid;
    ILoginListener listener;
    TextView tvErrUser, tvErrPass, tvForgotPass, tvHeaderLoginSocial;
    Handler mHandler;
    Runnable mRunnable;
    Constants.RTF_STATUS status;
    LinearLayout layoutKeyboardSpace;
    private KeyboardHeightProvider keyboardProvider;
    int errEmailCount;
    CardView contentPanel;
    private static final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment newInstance(ILoginListener listener) {
        LoginFragment frag = new LoginFragment();
        frag.setListener(listener);
        Bundle bulde = new Bundle();
        frag.setArguments(bulde);
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(d.getWindow()).setLayout(width, height);


        }
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_account_container;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mActivity.registerReceiver(networkChangeReceiver, intentFilter);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        loginPresenter.getAuthenConfig();
        keyboardProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        keyboardProvider.close();
    }

    public void initView(View v) {
        mHandler = new Handler();
        loginPresenter = new LoginPresenterImpl(this);
        iv18Plus = (ImageView) v.findViewById(R.id.iv18Plus);
        layoutKeyboardSpace = (LinearLayout) v.findViewById(R.id.layout_keyboard_space);
        edtLoginAccount = (TextInputEditText) v.findViewById(R.id.edtLoginAccount);
        edtLoginPassword = (TextInputEditText) v.findViewById(R.id.edtLoginPassword);
        tvTerm = (TextView) v.findViewById(R.id.tvTerm);
        tvPlayTimeNoti = (TextView) v.findViewById(R.id.tvPlayTimeNoti);
        layout_content_top = (LinearLayout) v.findViewById(R.id.layout_content_top);
        layout_content_mid = (ConstraintLayout) v.findViewById(R.id.layout_content_mid);
        termContent = (LinearLayout) v.findViewById(R.id.termContent);
        contentIv18 = (LinearLayout) v.findViewById(R.id.contentIv18);
        btnFaceBook = (ImageButton) v.findViewById(R.id.btnFacebook);
        btnGoogle = (ImageButton) v.findViewById(R.id.btnGoogle);
        btnLogin = (TextView) v.findViewById(R.id.btnLogin);
        tvRegister = (TextView) v.findViewById(R.id.tvRegister);
        btnLoginCenter = (TextView) v.findViewById(R.id.btnLoginCenter);
        btnPlayNow = (ImageButton) v.findViewById(R.id.btnPlayNow);
        btnClose = (ImageButton) v.findViewById(R.id.btnClose);
        tvErrUser = (TextView) v.findViewById(R.id.tvErrUser);
        tvErrPass = (TextView) v.findViewById(R.id.tvErrPass);
        tvForgotPass = (TextView) v.findViewById(R.id.tvForgotPass);
        tvHeaderLoginSocial = (TextView) v.findViewById(R.id.tvHeaderLoginSocial);
        contentPanel = (CardView) v.findViewById(R.id.contentPanel);

        layout_content_bottom = (LinearLayout) v.findViewById(R.id.layout_content_bottom);
        btnLoginFacebook2 = (LinearLayout) v.findViewById(R.id.btnLoginFacebook2);
        btnGoogle2 = (LinearLayout) v.findViewById(R.id.btnGoogle2);
        btnLoginPlayNow2 = (LinearLayout) v.findViewById(R.id.btnLoginPlayNow2);


        tvTerm.setText(Html.fromHtml(getString(R.string.lbl_term)));
        edtLoginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                edtLoginAccount.setBackground(mActivity.getResources().getDrawable(R.drawable.slt_input_text_login_land));
                tvErrUser.setVisibility(View.GONE);
            }
        });
        edtLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                edtLoginPassword.setBackground(mActivity.getResources().getDrawable(R.drawable.slt_input_text_login_land));
                tvErrPass.setVisibility(View.GONE);
            }
        });
        edtLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    DeviceUtils.hideKeyboardFrom(mActivity, v);
                    onLoginEmail();
                    handled = true;
                }
                return handled;
            }
        });

        btnFaceBook.setOnClickListener(this);
        btnPlayNow.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginCenter.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        tvTerm.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgotPass.setOnClickListener(this);
        layout_content_bottom.setOnClickListener(this);
        btnLoginFacebook2.setOnClickListener(this);
        btnGoogle2.setOnClickListener(this);
        btnLoginPlayNow2.setOnClickListener(this);
        errEmailCount = 0;
        keyboardProvider = new KeyboardHeightProvider(mActivity);
        keyboardProvider.start();

    }

    DialogFragment newFragment;
    ILoginListener registerListener = new ILoginListener() {
        @Override
        public void onLoginSuccess() {

        }

        @Override
        public void onRegisterSuccess(String param) {
            newFragment.dismiss();
            newFragment = null;
            //init data for login success
            try {
                LoginByRegisterObj obj = LoginByRegisterObj.parse(param);
                obj.getData().setLoginType(Constants.LOGIN_TYPE.EMAIL);
                obj.getData().setLoginOrRegister(obj.getIsRegister());
                (LoginFragment.this).onLoginSuccess(obj.getData(), obj.getAccessToken());

                //Get SDK configs , must call
                loginPresenter.getSdkConfig();
                if (listener != null)
                    listener.onRegisterSuccess(param);
            } catch (Exception e) {
                DialogUtils.showErrorDialog(mActivity, getString(R.string.err_422));
            }
        }
    };

    public void showRegisterDialog() {
        newFragment = (DialogFragment) mActivity.getFragmentManager().findFragmentByTag(RegistryFragment.class.getSimpleName());
        if (newFragment == null)
            newFragment = RegistryFragment.newInstance(registerListener);
        newFragment.setCancelable(true);
        if (newFragment.getDialog() != null && newFragment.getDialog().isShowing())
            return;
        newFragment.show(mActivity.getFragmentManager(), RegistryFragment.class.getSimpleName());
    }

    public void showTermDialog() {

        DialogFragment newFragment = (DialogFragment) mActivity.getFragmentManager().findFragmentByTag(TermDialogFragment.class.getSimpleName());
        if (newFragment == null)
            newFragment = TermDialogFragment.newInstance(ApiUtils.getUrlTerm(mActivity));
        newFragment.setCancelable(true);
        if (newFragment.getDialog() != null && newFragment.getDialog().isShowing())
            return;
        newFragment.show(mActivity.getFragmentManager(), TermDialogFragment.class.getSimpleName());

    }

    /**
     * Login Email
     */
    private void onLoginEmail() {
        if (!NetworkUtils.checkNetwork(mActivity)) {
            DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.error_network));
            return;
        }
        if (GameConfigs.getInstance().isLogin())
            return;

        if (errEmailCount >= 5) {
            DialogUtils.showErrorInputDialog(mActivity, mActivity.getString(R.string.err_input_more_than_5), new DialogUtils.DlgCloseListener() {
                @Override
                public void onClose() {
                    errEmailCount = 0;
                    onLoginPlayNow();
                }
            }, new DialogUtils.DlgCloseListener() {
                @Override
                public void onClose() {
                    errEmailCount = 0;
                }
            });
            return;
        }

        CheShaoTracking.getInstance().trackClickLoginEmailButton();

        String user = edtLoginAccount.getText().toString();
        String pass = edtLoginPassword.getText().toString();
        String checkUser = StringUtils.validateUser(mActivity, user);
        if (!checkUser.isEmpty()) {
            CheShaoTracking.getInstance().trackLoginEmailInputError(1, checkUser);
            tvErrUser.setVisibility(View.VISIBLE);
            tvErrUser.setText(checkUser);
            edtLoginAccount.setBackground(mActivity.getResources().getDrawable(R.drawable.slt_input_text_login_err));
            edtLoginAccount.requestFocus();
            errEmailCount += 1;
            return;
        }
        String checkPass = StringUtils.validatePassword(mActivity, pass);
        if (!checkPass.isEmpty()) {
            CheShaoTracking.getInstance().trackLoginEmailInputError(2, checkPass);
            tvErrPass.setVisibility(View.VISIBLE);
            tvErrPass.setText(checkPass);
            edtLoginPassword.setBackground(mActivity.getResources().getDrawable(R.drawable.slt_input_text_login_err));
            edtLoginPassword.requestFocus();
            errEmailCount += 1;
            return;
        }

        CheShaoTracking.getInstance().trackLoginEmailCallApi();
        loginPresenter.loginEmail(user, pass);
    }

    private void onLoginPlayNow() {
        if (!NetworkUtils.checkNetwork(mActivity)) {
            DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.error_network));
            return;
        }
        if (GameConfigs.getInstance().isLogin())
            return;
        CheShaoTracking.getInstance().trackLoginPlayNowCallApi();
        loginPresenter.loginPlayNow();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GoogleManager.RC_SIGN_IN:
                GoogleManager.getInstance(mActivity).onAuthGoogleResult(data, resultCode);
                break;
            case Constants.REQUEST_CODE_FACEBOOK_LOGIN:
                FacebookManager.getInstance(mActivity).onAuthResult(requestCode, resultCode, data);
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (mHandler != null && mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    DeviceUtils.hideKeyboardFrom(mActivity, v);
                    if (v.getId() == R.id.btnFacebook || v.getId() == R.id.btnLoginFacebook2) {
                        if (status == Constants.RTF_STATUS.AUTHENING)
                            return;
                        status = Constants.RTF_STATUS.AUTHENING;
                        authenFacebook();
                    } else if (v.getId() == R.id.btnGoogle || v.getId() == R.id.btnGoogle2) {
                        if (status == Constants.RTF_STATUS.AUTHENING)
                            return;
                        status = Constants.RTF_STATUS.AUTHENING;
                        authenGooogle();
                    } else if (v.getId() == R.id.btnLogin || v.getId() == R.id.btnLoginCenter ) {
                        onLoginEmail();
                    } else if (v.getId() == R.id.btnClose) {
                        CheShaoTracking.getInstance().trackLoginScreenClose(contentPanel, edtLoginAccount, btnLogin);
                        dismiss();
                    } else if (v.getId() == R.id.btnPlayNow || v.getId() == R.id.btnLoginPlayNow2) {
                        onLoginPlayNow();
                    } else if (v.getId() == R.id.tvTerm) {
                        showTermDialog();
                    } else if (v.getId() == R.id.tvRegister) {
                        CheShaoTracking.getInstance().trackClickBtnRegister();
                        showRegisterDialog();
                    } else if (v.getId() == R.id.tvForgotPass) {
                        DialogUtils.showInfoDialog(mActivity, getString(R.string.notice), getString(R.string.info_forget_pass));
                        CheShaoTracking.getInstance().trackForgotPassword();
                    }
                } catch (Exception e) {
                } finally {
                    mHandler = null;
                    mRunnable = null;
                }
            }
        };
        if (mHandler == null)
            mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 150);


    }

    private void authenFacebook() {
        if (!NetworkUtils.checkNetwork(mActivity)) {
            DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.error_network));
            return;
        }
        //Track event
        CheShaoTracking.getInstance().trackClickLoginFacebookButton();
        //start auth
        if (isAdded())
            FacebookManager.getInstance(mActivity).startAuth(this, this);
    }

    private void authenGooogle() {
        if (!NetworkUtils.checkNetwork(mActivity)) {
            DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.error_network));
            return;
        }
        //Track event
        CheShaoTracking.getInstance().trackClickLoginGoogleButton();
        //start auth
        if (isAdded())
            GoogleManager.getInstance(mActivity).startAuthForResult(this, this);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        CheShaoTracking.getInstance().trackLoginScreenOpen();
    }

    public void applyConfig(@NonNull AuthenConfigObj obj) {
        AuthenConfigObj.LoginConfig cf = obj.getLoginConfig();

        /**
         *  Config return
         *  1 is show
         *  0 is hide
         *
         * */

        if (cf == null)
            return;
        if (cf.getLogin_google() == 0 & cf.getLogin_facebook() == 0 && cf.getPlay_now() == 0 && cf.getLogin_email() == 0) {
            getView().setVisibility(View.GONE);
            onLoginPlayNow();
        }
        if (cf.getLogin_email() == 0) {
            layout_content_top.setVisibility(View.GONE);
            tvHeaderLoginSocial.setText(getString(R.string.lbl_or_login_with));
            tvHeaderLoginSocial.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
//        tvRegister.setVisibility((obj != null && obj.getRegisterConfig() == 1) ? View.VISIBLE : View.GONE);

        if(obj.getRegisterConfig() == 1){
            btnLogin.setVisibility(View.VISIBLE);
            tvRegister.setVisibility(View.VISIBLE);
            btnLoginCenter.setVisibility(View.GONE);
        }
        else {
            btnLogin.setVisibility(View.GONE);
            tvRegister.setVisibility(View.GONE);
            btnLoginCenter.setVisibility(View.VISIBLE);
        }

        btnFaceBook.setVisibility(cf.getLogin_facebook() == 0 ? View.GONE : View.VISIBLE);
        btnLoginFacebook2.setVisibility(cf.getLogin_facebook() == 0 ? View.GONE : View.VISIBLE);

        btnGoogle.setVisibility(cf.getLogin_google() == 0 ? View.GONE : View.VISIBLE);
        btnGoogle2.setVisibility(cf.getLogin_google() == 0 ? View.GONE : View.VISIBLE);

        btnPlayNow.setVisibility(cf.getPlay_now() == 0 ? View.GONE : View.VISIBLE);
        btnLoginPlayNow2.setVisibility(cf.getPlay_now() == 0 ? View.GONE : View.VISIBLE);

        layout_content_top.setVisibility(cf.getLogin_email() == 0 ? View.GONE : View.VISIBLE);
        layout_content_mid.setVisibility(cf.getLogin_email() == 0 ? View.GONE : View.VISIBLE);
        layout_content_bottom.setVisibility(cf.getLogin_email() == 0 ? View.VISIBLE : View.GONE);

        if(cf.getPlay_now()==0 && cf.getLogin_google()==0 && cf.getLogin_facebook()==0 ){
            layout_content_mid.setVisibility(View.GONE);
        }
        if (obj.getLogoOlder() == null || (obj.getLogoOlder().getUrl() == null && obj.getLogoOlder().getText() == null))
            contentIv18.setVisibility(View.GONE);

        if (obj.getLogoOlder() != null && obj.getLogoOlder().getUrl() != null) {

            RequestOptions options = new RequestOptions()
                    .centerInside()
                    .placeholder(R.drawable.ic_logo18)
                    .error(R.drawable.ic_logo18)
                    .priority(Priority.HIGH);
            Glide.with(mActivity)
                    .load(obj.getLogoOlder().getUrl())
                    .apply(options)
                    .into(iv18Plus);
        } else {
            iv18Plus.setVisibility(View.INVISIBLE);
        }
        if (obj.getLogoOlder() != null && obj.getLogoOlder().getText() != null) {
            tvPlayTimeNoti.setText(obj.getLogoOlder().getText());
        }
    }


    @Override
    public void showProgress(String mess) {
        showProgressDialog(true, mess);
    }

    @Override
    public void hideProgress() {
        showProgressDialog(false, "");
    }

    @Override
    public void success(Object x) {
        if (x instanceof AuthenConfigResponseObj) {
            AuthenConfigObj obj = ((AuthenConfigResponseObj) x).getData();
            AuthenConfigs.getInstance().setAuthenConfigObj(obj);
            applyConfig(obj);
            CheShaoTracking.getInstance().trackCallAuthenConfigSuccess();
        } else if (x instanceof LoginPlayNowResponseObj) {
            CheShaoTracking.getInstance().trackLoginPlayNowCallApiSuccess();
            //init data for login success\
            LoginPlayNowResponseObj obj = (LoginPlayNowResponseObj) x;
            obj.getData().setLoginType(Constants.LOGIN_TYPE.PLAYNOW);
            obj.getData().setLoginOrRegister(obj.getIsRegister());
            onLoginSuccess(obj.getData(), obj.getAccessToken());

            //Get SDK configs , must call
            loginPresenter.getSdkConfig();

        } else if (x instanceof LoginEmailResponseObj) {
            CheShaoTracking.getInstance().trackLoginEmailCallApiSuccess();
            //init data for login success
            LoginEmailResponseObj obj = (LoginEmailResponseObj) x;
            obj.getData().setLoginType(Constants.LOGIN_TYPE.EMAIL);
            obj.getData().setLoginOrRegister(obj.getIsRegister());
            onLoginSuccess(obj.getData(), obj.getAccessToken());

            //Get SDK configs , must call
            loginPresenter.getSdkConfig();

        } else if (x instanceof LoginFacebookResponseObj) {
            CheShaoTracking.getInstance().trackLoginFacebookCallApiSuccess();
            //init data for login success
            LoginFacebookResponseObj obj = (LoginFacebookResponseObj) x;
            obj.getData().setLoginType(Constants.LOGIN_TYPE.FACEBOOK);
            obj.getData().setLoginOrRegister(obj.getIsRegister());
            onLoginSuccess(obj.getData(), obj.getAccessToken());

            //Get SDK configs , must call
            loginPresenter.getSdkConfig();

        } else if (x instanceof LoginGoogleResponseObj) {
            CheShaoTracking.getInstance().trackLoginGoogleCallApiSuccess();
            //init data for login success
            LoginGoogleResponseObj obj = (LoginGoogleResponseObj) x;
            obj.getData().setLoginType(Constants.LOGIN_TYPE.GOOGLE);
            obj.getData().setLoginOrRegister(obj.getIsRegister());
            onLoginSuccess(obj.getData(), obj.getAccessToken());

            //Get SDK configs , must call
            loginPresenter.getSdkConfig();

        } else if (x instanceof SdkConfigResponseObj) {
            CheShaoTracking.getInstance().trackGetSdkConfigSuccess();
            SdkConfigResponseObj obj = (SdkConfigResponseObj) x;
            GameConfigs.getInstance().setSdkConfig(obj.getData());
            LogUtils.d("LifeCycleActivity", "get game info success");
            if (obj.getData() != null && obj.getData().getMaintenance() != null && !TextUtils.isEmpty(obj.getData().getMaintenance().getUrl())) {
                CheShaoTracking.getInstance().trackMaintainScreenOpened();
                CheShaoSDK.getInstance().getQueuePopups().add(CheShaoSDK.POPUP_LINK);
                CheShaoSDK.getInstance().showPopup();
            } else {
                CheShaoSdk.getInstance().onLoginSuccess(mActivity, "login");
                if (listener != null)
                    listener.onLoginSuccess();
            }
            dismiss();
        }

    }


    public void onLoginSuccess(UserObj obj, String token) {
        GameConfigs.getInstance().setUser(obj);
        AuthenConfigs.getInstance().setAccessToken(token);
        RetrofitClient.clearInstant();
        CheShaoTracking.getInstance().trackLoginSuccess(GameConfigs.getInstance().getUser().getId(), GameConfigs.getInstance().getUser().getLoginOrRegister(), GameConfigs.getInstance().getUser().getLoginType());
    }


    @Override
    public void error(Object o) {
        BaseObj apiErrorObj = (BaseObj) o;
        if (apiErrorObj.getStatus() == ErrorCode.NO_INTERNET) {
            DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.error_network));
            return;
        }
        if (o instanceof LoginPlayNowErrObj) {
            LoginPlayNowErrObj obj = (LoginPlayNowErrObj) o;
            CheShaoTracking.getInstance().trackLoginPlayNowCallApiFail(obj.getStatus(), obj.getMessage());
            DialogUtils.showErrorDialog(mActivity, obj.getMessage());
        } else if (o instanceof LoginEmailErrObj) {
            LoginEmailErrObj obj = (LoginEmailErrObj) o;
            CheShaoTracking.getInstance().trackLoginEmailCallApiFail(obj.getStatus(), obj.getMessage());
            DialogUtils.showErrorDialog(mActivity, obj.getMessage());

        } else if (o instanceof LoginFacebookErrObj) {
            LoginFacebookErrObj obj = (LoginFacebookErrObj) o;
            CheShaoTracking.getInstance().trackLoginFacebookCallApiFail(obj.getStatus(), obj.getMessage());
            DialogUtils.showErrorDialog(mActivity, obj.getMessage());
        } else if (o instanceof LoginGGErrObj) {
            LoginGGErrObj obj = (LoginGGErrObj) o;
            CheShaoTracking.getInstance().trackLoginGoogleCallApiFail(obj.getStatus(), obj.getMessage());
            DialogUtils.showErrorDialog(mActivity, obj.getMessage());
        } else if (o instanceof AuthenConfigErrObj) {
            AuthenConfigObj obj = new AuthenConfigObj();
            AuthenConfigObj.LoginConfig loginConfig = new AuthenConfigObj.LoginConfig();
            obj.setRegisterConfig(1);
            loginConfig.setLogin_apple(1);
            loginConfig.setLogin_email(1);
            loginConfig.setLogin_facebook(0);
            loginConfig.setPlay_now(1);
            loginConfig.setLogin_google(0);
            obj.setLoginConfig(loginConfig);
            applyConfig(obj);
            CheShaoTracking.getInstance().trackCallAuthenConfigFailed();
        } else if (o instanceof SdkConfigErrObj) {
            SdkConfigErrObj obj = (SdkConfigErrObj) o;
            CheShaoTracking.getInstance().trackGetSdkConfigFailed(obj.getStatus());
            if (obj.getStatus() == Constants.USER_ERR_CODE.INVALID_TOKEN) {
                Log.d("zxcvbnm,./"," "+obj.getStatus());
                Log.d("zxcvbnm,./"," "+obj.getMessage());
                DialogUtils.showExpireDialog(mActivity);
            } else
                DialogUtils.showRetryDialog(mActivity, obj.getMessage(), new DialogUtils.Listener() {
                    @Override
                    public void onRetry() {
                        loginPresenter.getSdkConfig();
                    }
                });
        }
    }


    //----------------------------------------------Facebook-----------------------------------------------
    @Override
    public void onAuthFBSuccess(LoginResult loginResult) {
        status = Constants.RTF_STATUS.DONE;
        LogUtils.d(TAG, "facebook token : " + loginResult.getAccessToken().getToken());
        CheShaoTracking.getInstance().trackLoginFacebookCallApi();
        if (GameConfigs.getInstance().isLogin())
            return;
        loginPresenter.loginFtFacebook(loginResult.getAccessToken().getToken());
    }

    @Override
    public void onAuthFBFailed(FacebookException error) {
        status = Constants.RTF_STATUS.DONE;
        DialogUtils.showErrorDialog(mActivity, "Auth FB failed : " + error.getMessage());
        if (error != null && error.getMessage() != null)
            CheShaoTracking.getInstance().trackLoginFacebookSdkErr(error.getMessage());
    }

    @Override
    public void onAuthFBCancel() {
        status = Constants.RTF_STATUS.DONE;
        LogUtils.d("Login Facebook", " Auth FB cancel");
        CheShaoTracking.getInstance().trackCancelLoginFacebook();
    }

    //-----------------------------------------Google--------------------------------------------------

    @Override
    public void onAuthGGSuccess(GoogleSignInAccount account, String mToken) {
        status = Constants.RTF_STATUS.DONE;
        LogUtils.d(TAG, "google token : " + mToken);
        if (GameConfigs.getInstance().isLogin())
            return;
        CheShaoTracking.getInstance().trackLoginGoogleCallApi();
        loginPresenter.loginFtGoogle(mToken);
    }

    @Override
    public void onAuthGGFailed(int code, String mess) {
        if (code == SIGN_IN_CURRENTLY_IN_PROGRESS)
            GoogleManager.getInstance(mActivity).startAuthForResult(this, this);
        else {
            status = Constants.RTF_STATUS.DONE;
            DialogUtils.showErrorDialog(mActivity, "Auth GG failed : " + mess);
        }
    }

    @Override
    public void onAuthGGCancel() {
        status = Constants.RTF_STATUS.DONE;
        LogUtils.d(TAG, " Auth GG cancel");
        CheShaoTracking.getInstance().trackCancelLoginGoogle();
    }

    public void setListener(ILoginListener listener) {
        this.listener = listener;
    }


    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AuthenConfigs.getInstance().getAuthenConfigObj() == null)
                loginPresenter.getAuthenConfig();
        }
    };


    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        changeKeyboardHeight(height);
        LogUtils.d(TAG, "onKeyboardHeightChanged: " + orientation);
    }

    private void changeKeyboardHeight(int height) {
        try {
            if (height > 100) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                layoutKeyboardSpace.setLayoutParams(params);
                layoutKeyboardSpace.setVisibility(View.VISIBLE);
                return;
            }

            layoutKeyboardSpace.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardProvider.setKeyboardHeightObserver(null);
        mActivity.unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

}
