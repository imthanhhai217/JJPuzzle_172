package com.pachia.comon.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quby.R;

public class CustomProgressDialog extends Dialog {
    TextView progressTitle;
    ProgressBar progressBar;
    public static CustomProgressDialog frag;
    String mesage;
    Context context;

    public CustomProgressDialog(@NonNull Context context, String message) {
        super(context);
        this.mesage = message;
        this.context= context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog_custom);
        progressTitle = (TextView) findViewById(R.id.progressTitle);
        progressBar= (ProgressBar) findViewById(R.id.progressBar) ;
        progressTitle.setText(mesage);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.color_light_red), PorterDuff.Mode.MULTIPLY);
    }
}
