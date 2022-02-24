package com.pachia.balat.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.appsflyer.AppsFlyerLib;
import com.brother.balat.demo.R;
import com.pachia.comon.api.RetrofitClient;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.ConstantApi;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.js.command.CmdDashboard;
import com.pachia.comon.listener.ILoginListener;
import com.pachia.comon.listener.ILogoutListener;
import com.pachia.comon.listener.IMesssageListener;
import com.pachia.comon.listener.IPaymentListener;
import com.pachia.comon.listener.ISaveCharactorListener;
import com.pachia.comon.object.MessInGameObj;
import com.pachia.comon.object.VerifyPurchaseObj;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.supho.CheShaoSDK;
import com.supho.utils.TimeUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();
    private EditText txt_roleID, txt_areaID, txt_level;
    private TextView tvUrl;
    private Button btnSaveUrl;
    private RadioButton rbHozial, rbPortrat, rbVi, rbEN;
    private Activity activity;

    @Override
    protected void onStart() {
        super.onStart();
        DeviceUtils.setLocale(GameActivity.this, PrefManager.getString(activity, Constants.APP_LANG, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        activity = GameActivity.this;

        setRequestedOrientation(PrefManager.getInt(activity, Constants.SCREEN_ORIENTATION_CURRENT, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        CheShaoSdk.getInstance().initSdkOneSignal(activity, "b3fabe11-3e31-405b-a9fd-3bf264da28f7");
        initGUI();
        LogUtils.d(TAG, "AppsFlyerLib " + AppsFlyerLib.getInstance().getAppsFlyerUID(this));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       CheShaoSdk.getInstance().onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.gc();
            System.exit(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CheShaoSdk.getInstance().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CheShaoSdk.getInstance().onWindowFocusChanged(hasFocus);
    }

    private PackageInfo getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info;
    }

    private int mYear, mMonth, mDay, mHour, mMinute;

    private void initGUI() {
        setUpTimeForReminderFunction();
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("versionCode:" + getVersion().versionCode);
        rbEN = (RadioButton) findViewById(R.id.rbEn);
        rbVi = (RadioButton) findViewById(R.id.rbVi);
        rbVi.setChecked(PrefManager.getString(activity, Constants.APP_LANG, "en").equals("vi"));
        rbEN.setChecked(!rbVi.isChecked());

        rbVi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DeviceUtils.setLocale(activity, "vi");
                }

            }
        });
        rbEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DeviceUtils.setLocale(activity, "en");
                }

            }
        });


        rbHozial = (RadioButton) findViewById(R.id.rbHozial);
        rbPortrat = (RadioButton) findViewById(R.id.rbPort);
        rbPortrat.setChecked(PrefManager.getInt(activity, Constants.SCREEN_ORIENTATION_CURRENT, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        rbHozial.setChecked(!rbPortrat.isChecked());
        rbHozial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    PrefManager.saveSetting(activity, Constants.SCREEN_ORIENTATION_CURRENT, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }

            }
        });
        rbPortrat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    PrefManager.saveSetting(activity, Constants.SCREEN_ORIENTATION_CURRENT, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }

            }
        });

        findViewById(R.id.btn_login_native).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheShaoSdk.getInstance().login(activity, new ILoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        // TODO: 7/16/2020 Call after login success and dialog login is dismiss

                    }

                    @Override
                    public void onRegisterSuccess(String param) {

                    }
                });
            }
        });

        findViewById(R.id.btn_dashboard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: showDashboard");
                CheShaoSdk.getInstance().showDashBoard(activity);
            }
        });

        findViewById(R.id.btn_share_image).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap image = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic);
                CheShaoSdk.getInstance().shareImageToFacebook(activity, image);

            }
        });
        findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CheShaoSdk.getInstance().logOut(activity, new ILogoutListener() {
                    @Override
                    public void onLogoutSuccess() {

                    }
                });
            }
        });

        findViewById(R.id.btn_payment).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        CheShaoSdk.getInstance().payment(activity, null, new IPaymentListener() {
                            @Override
                            public void onPaymentSuccess(VerifyPurchaseObj obj) {
                                ToastUtils.showShortToast(activity, "Thanh toán thành công");

                            }
                        });
                    }
                });


        findViewById(R.id.btn_payment_with_state).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        CheShaoSdk.getInstance().payment(activity, null, new IPaymentListener() {
                            @Override
                            public void onPaymentSuccess(VerifyPurchaseObj obj) {
                                ToastUtils.showShortToast(activity, "Thanh toán thành công ");
                            }
                        });
                    }
                });

        findViewById(R.id.btn_save_charactor).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String roleId = txt_roleID.getText().toString();
                        String areaId = txt_areaID.getText().toString();
                        if (TextUtils.isEmpty(roleId) || TextUtils.isEmpty(areaId)) {
                            ToastUtils.showShortToast(activity, "RoleId và AreaId không được bỏ trống");
                            return;
                        }

                        CheShaoSdk.getInstance().saveCharacter(activity, roleId, areaId, new ISaveCharactorListener() {
                            @Override
                            public void onSuccess() {
                                LogUtils.d("Save charactor", "save Success");
                            }
                        });
                    }
                });


        findViewById(R.id.btn_pick).setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {
                CheShaoSdk.getInstance().getMessagesInGame(activity, new IMesssageListener() {
                    @Override
                    public void onSuccess(ArrayList<MessInGameObj> data) {
                        CheShaoSdk.getInstance().showTextScroll(data);
                    }
                });

            }
        });



        txt_roleID = (EditText) findViewById(R.id.txt_roleID);
        txt_areaID = (EditText) findViewById(R.id.txt_areaID);
        txt_level = (EditText) findViewById(R.id.txt_level);

        findViewById(R.id.btn_send_email).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.getLanguage() == "vi") {
                    CmdDashboard.getInstance().mobSendEmail(activity, "{\n" +
                            "  \"subject\":\"hotro@funtap.vn\",\n" +
                            "  \"body\":\"\"\n" +
                            "}");
                } else {
                    CmdDashboard.getInstance().mobSendEmail(activity, "{\n" +
                            "  \"subject\":\"support@mobgame.mobi\",\n" +
                            "  \"body\":\"\"\n" +
                            "}");
                }
            }
        });

        findViewById(R.id.btn_remind_me).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String formatPattern = "HH:mm dd/MM/yyyy";
                    String givenStartTimeString = ((TextView) findViewById(R.id.tv_event_time_start)).getText().toString() + " " + ((TextView) findViewById(R.id.tv_event_date_start)).getText().toString();
                    String givenEndTimeString = ((TextView) findViewById(R.id.tv_event_time_end)).getText().toString() + " " + ((TextView) findViewById(R.id.tv_event_date_end)).getText().toString();
                    long startMiliTime = TimeUtils.parseTimeStringToMiliSec(givenStartTimeString, formatPattern);
                    long endMiliTime = TimeUtils.parseTimeStringToMiliSec(givenEndTimeString, formatPattern);
                    String dateStart = Utils.convertLongToDate(startMiliTime);
                    String dateEnd = Utils.convertLongToDate(startMiliTime);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("start_date", dateStart);
                    jsonObject.put("end_date", dateEnd);
                    jsonObject.put("event_title", ((TextView) findViewById(R.id.tv_event_title)).getText().toString());
                    jsonObject.put("description", null);
                    jsonObject.put("deeplink", ((TextView) findViewById(R.id.tv_event_deeplink)).getText().toString());

                    Log.d(TAG, "eventEndTime : " + jsonObject.toString());
                    CmdDashboard.getInstance().setUpReminder(activity, jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.tv_event_date_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker((TextView) v);
            }
        });

        findViewById(R.id.tv_event_date_end).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker((TextView) v);
            }
        });

        findViewById(R.id.tv_event_time_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker((TextView) v);
            }
        });

        findViewById(R.id.tv_event_time_end).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker((TextView) v);
            }
        });

        findViewById(R.id.force_crash).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                float a = 1 / 0;
            }
        });

        findViewById(R.id.btn_level_archived).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameConfigs.getInstance().getUser() != null) {
                    String level = txt_level.getText().toString();
                    if (TextUtils.isEmpty(level)) {
                        ToastUtils.showShortToast(activity, "Level can not be empty");
                        return;
                    }
                    CheShaoTracking.getInstance().trackLevelAchieved(activity, GameConfigs.getInstance().getUser().getId(), txt_roleID.getText().toString(), txt_areaID.getText().toString(), level);
                } else
                    ToastUtils.showShortToast(activity, "Need Login");
            }
        });

        findViewById(R.id.btn_vip_archived).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = txt_level.getText().toString();
                if (TextUtils.isEmpty(level)) {
                    ToastUtils.showShortToast(activity, "Vip Level can not be empty");
                    return;
                }
                if (GameConfigs.getInstance().getUser() != null)
                    CheShaoTracking.getInstance().trackVipAchieved(GameConfigs.getInstance().getUser().getId(), txt_roleID.getText().toString(), txt_areaID.getText().toString(), level);
                else
                    ToastUtils.showShortToast(activity, "Need Login");
            }
        });

        btnSaveUrl = (Button) findViewById(R.id.btnSaveUrl);
        tvUrl = (TextView) findViewById(R.id.tvUrl);
        tvUrl.setText(PrefManager.getString(activity, ConstantApi.APP_UR, ConstantApi.BASE_URL));
        btnSaveUrl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String base = tvUrl.getText().toString();
                RetrofitClient.clearInstant();
                PrefManager.saveSetting(activity, ConstantApi.APP_UR, base);
                CheShaoSdk.getInstance().logOut(activity);
                CheShaoSDK.getInstance().logout();
                ToastUtils.showShortToast(activity, "Url Saved");
                DeviceUtils.hideKeyboardFrom(activity, v);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButton_start_extract:
                if (checked) CheShaoTracking.getInstance().trackStartExtractData(activity);
                break;
            case R.id.radioButton_finish_extract:
                if (checked) CheShaoTracking.getInstance().trackFinishExtractData();
                break;
            case R.id.radioButton_resource_start:
                if (checked) CheShaoTracking.getInstance().trackDownloadResourceStarted();
                break;
            case R.id.radioButton_resource_finish:
                if (checked) CheShaoTracking.getInstance().trackDownloadResourceFinished();
                break;

            case R.id.radioButton_tutorial_completed:
                if (checked) {
                    if (GameConfigs.getInstance().getUser() != null)
                        CheShaoTracking.getInstance().trackTutorialCompletion(GameConfigs.getInstance().getUser().getId(), txt_roleID.getText().toString(), txt_areaID.getText().toString());
                    else
                        ToastUtils.showShortToast(activity, "Need Login");
                }
                break;

            case R.id.radioButton_extract_cdn_finish:
                if (checked) {
                    CheShaoTracking.getInstance().trackExtractCDNFinished(activity);
                }
                break;
            case R.id.radioButton_enter_gamebtn_clicked:
                if (checked) {
                    CheShaoTracking.getInstance().trackClickBtnEnterGame("50");
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheShaoSdk.getInstance().onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
    }

    private void setUpTimeForReminderFunction() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }


    private void showDialogTimePicker(TextView v) {
        TimePickerDialog dialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                v.setText(String.valueOf(hourOfDay + ":" + minute));
                mHour = hourOfDay;
                mMinute = minute;
            }
        }, mHour, mMinute, true);
        dialog.show();
    }

    private void showDialogDatePicker(TextView textView) {
        DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthOfYear = month + 1 + "";
                if (month < 10) monthOfYear = "0" + monthOfYear;
                String date = dayOfMonth + "";
                if (date.length() < 2) date = "0" + date;

                textView.setText(String.valueOf(date + "/" + monthOfYear + "/" + year));
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void onBackPressed() {
        CheShaoSdk.getInstance().onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DeviceUtils.setLocale(this, PrefManager.getString(this, Constants.APP_LANG, "en"));
    }
}
