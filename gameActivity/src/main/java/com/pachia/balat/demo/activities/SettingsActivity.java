package com.pachia.balat.demo.activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.brother.balat.demo.R;
import com.pachia.balat.demo.utils.Global;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @Nullable
    @BindView(R.id.sbRows)
    SeekBar sbRows;
    @Nullable
    @BindView(R.id.sbColumns)
    SeekBar sbColumns;
    @Nullable
    @BindView(R.id.btnPurchase)
    Button btnPurchase;
    @Nullable
    @BindView(R.id.tvRowsValue)
    TextView tvRowsValue;
    @Nullable
    @BindView(R.id.tvColumnsValue)
    TextView tvColumnsValue;

    private static int currentRows;
    private static int currentColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        
        initView();
    }

    private void initView() {
        currentRows = Global.getRows(getApplicationContext());
        currentColumns = Global.getColumns(getApplicationContext());

        sbRows.setProgress(currentRows);
        sbColumns.setProgress(currentColumns);

        tvRowsValue.setText(currentRows+"");
        tvColumnsValue.setText(currentColumns+"");


        sbRows.setOnSeekBarChangeListener(this);
        sbColumns.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sbRows:
                currentRows = progress;
                tvRowsValue.setText(currentRows+"");
                break;
            case R.id.sbColumns:
                currentColumns = progress;
                tvColumnsValue.setText(currentColumns+"");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()){
            case R.id.sbRows:
                Global.saveRows(getApplicationContext(),seekBar.getProgress());
                break;
            case R.id.sbColumns:
                Global.saveColumns(getApplicationContext(),seekBar.getProgress());
                break;
        }
    }
}