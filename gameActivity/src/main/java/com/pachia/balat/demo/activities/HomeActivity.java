package com.pachia.balat.demo.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.appsflyer.AppsFlyerLib;
import com.brother.balat.demo.R;
import com.pachia.balat.demo.GameActivity;
import com.pachia.balat.demo.utils.Global;
import com.pachia.comon.config.AuthenConfigs;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.listener.ILoginListener;
import com.pachia.comon.listener.ILogoutListener;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
    @Nullable
    @BindView(R.id.llPlayNow)
    LinearLayout llPlayNow;
    @Nullable
    @BindView(R.id.llGallery)
    LinearLayout llGallery;
    @Nullable
    @BindView(R.id.llCamera)
    LinearLayout llCamera;
    @Nullable
    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @Nullable
    @BindView(R.id.llContactUs)
    LinearLayout llContactUs;
    @Nullable
    @BindView(R.id.llPremium)
    LinearLayout llPremium;
    @Nullable
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @Nullable
    @BindView(R.id.tvLogout)
    TextView tvLogout;

    private String mPhotoPath;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        activity = HomeActivity.this;

        CheShaoSdk.getInstance().initSdkOneSignal(activity, "b3fabe11-3e31-405b-a9fd-3bf264da28f7");
        initView();
        LogUtils.d(TAG, "AppsFlyerLib " + AppsFlyerLib.getInstance().getAppsFlyerUID(this));
        login();
    }

    private void login() {
        CheShaoSdk.getInstance().login(activity, new ILoginListener() {
            @Override
            public void onLoginSuccess() {
                Log.d(TAG, "onLoginSuccess: ");
                tvWelcome.setText("Wellcome, " + GameConfigs.getInstance().getUser().getName() + "!");
            }

            @Override
            public void onRegisterSuccess(String param) {
                tvWelcome.setText("Wellcome, " + GameConfigs.getInstance().getUser().getName() + "!");
            }
        });
        if (GameConfigs.getInstance().isLogin()) {
            tvLogout.setText("LOGOUT");
            tvWelcome.setText("Wellcome, " + GameConfigs.getInstance().getUser().getName() + "!");
        } else {
            tvLogout.setText("LOGIN");
            tvWelcome.setText("Wellcome, guest!");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DeviceUtils.setLocale(HomeActivity.this, PrefManager.getString(activity, Constants.APP_LANG, "en"));
    }

    private void initView() {
        llPlayNow.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        llContactUs.setOnClickListener(this);
        llPremium.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!GameConfigs.getInstance().isLogin()) {
            login();
        } else {
            switch (v.getId()) {
                case R.id.llPlayNow:
                    gotoLibActivity();
                    break;
                case R.id.llGallery:
                    gotoGalleryActivity();
                    break;
                case R.id.llCamera:
                    gotoTakePhotoActivity(v);
                    break;
                case R.id.llSettings:
                    gotoSettingActivity();
                    break;
                case R.id.llContactUs:
                    CheShaoSdk.getInstance().showDashBoard(activity);
                    break;
                case R.id.llPremium:
                    gotoPremium();
                    break;
                case R.id.tvLogout:
                    CheShaoSdk.getInstance().logOut(activity, new ILogoutListener() {
                        @Override
                        public void onLogoutSuccess() {
                            login();
                        }
                    });
                    break;
            }
        }
    }

    private void gotoPremium() {
        Intent intent = new Intent(HomeActivity.this, InAppPurchaseActivity.class);
        startActivity(intent);
    }

    private void gotoLibActivity() {
        Intent intent = new Intent(HomeActivity.this, LibActivity.class);
        startActivity(intent);
    }

    private void gotoGalleryActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Global.PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Global.CHOOSE_PHOTO);
        }
    }

    private void gotoTakePhotoActivity(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = takePhoto();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, Global.TAKE_PHOTO);
            }
        }
    }

    private File takePhoto() throws IOException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, Global.PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            mPhotoPath = image.getAbsolutePath();
            return image;
        }

        return null;
    }

    private void gotoSettingActivity() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CheShaoSdk.getInstance().onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CheShaoSdk.getInstance().onActivityResult(requestCode, resultCode, data);
        if (requestCode == Global.TAKE_PHOTO && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra(Global.PHOTO_PATH, mPhotoPath);
            startActivity(intent);
        }
        if (requestCode == Global.CHOOSE_PHOTO && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra(Global.PHOTO_URI, uri.toString());
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Global.PERMISSION_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gotoTakePhotoActivity(new View(this));
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CheShaoSdk.getInstance().onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.gc();
            System.exit(0);
        }
    }
}