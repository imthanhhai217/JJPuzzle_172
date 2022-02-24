package com.pachia.balat.demo.activities;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brother.balat.demo.R;
import com.bumptech.glide.Glide;
import com.pachia.balat.demo.adapter.JJPuzzleViewAdapter;
import com.pachia.balat.demo.models.ImageModel;
import com.pachia.balat.demo.utils.Global;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibActivity extends AppCompatActivity {

    private static final String TAG = "LibActivity";
    @Nullable
    @BindView(R.id.vLoading)
    View vLoading;
    @Nullable
    @BindView(R.id.imgLoading)
    ImageView imgLoading;
    @Nullable
    @BindView(R.id.rvData)
    RecyclerView rvData;
    private ArrayList<ImageModel> mListData;
    private JJPuzzleViewAdapter JJPuzzleViewAdapter;
    private boolean isPurchase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lib);
        ButterKnife.bind(this);

        initView();
        loadData();
    }

    private void loadData() {
        isPurchase = Global.getPurchaseState(getApplicationContext());
//        isPurchase = true;//to test
        String[] files;
        mListData = new ArrayList<>();
        try {
            AssetManager am = getAssets();
            files = am.list("img");
            for (int i = 0; i < files.length; i++) {
                ImageModel imageModel = new ImageModel();
                Bitmap bitmap = getPicFromAsset(files[i]);
                imageModel.setBitmap(bitmap);
                imageModel.setName(files[i]);

                // Kiem tra xem da mua vip chua
                if (!isPurchase && i > 3){
                    imageModel.setLockState(false);
                }else {
                    imageModel.setLockState(true);
                }
                mListData.add(imageModel);
            }
            Log.d(TAG, "loadData: data size : " + mListData.size());

            JJPuzzleViewAdapter = new JJPuzzleViewAdapter(mListData,getApplicationContext());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
            rvData.setLayoutManager(gridLayoutManager);
            rvData.setAdapter(JJPuzzleViewAdapter);
            JJPuzzleViewAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
            finish();
        }

    }

    private void initView() {
        vLoading.setVisibility(View.VISIBLE);
        imgLoading.setVisibility(View.VISIBLE);

        Glide.with(getApplicationContext()).asGif().load(R.drawable.loading_process).into(imgLoading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vLoading.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private Bitmap getPicFromAsset(String assetName) {
        int targetW = 300;
        int targetH = 350;

        if (targetW == 0 || targetH == 0) {
            return null;
        }

        try {
            InputStream is = getAssets().open("img/" + assetName);
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            is.reset();

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            return BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
