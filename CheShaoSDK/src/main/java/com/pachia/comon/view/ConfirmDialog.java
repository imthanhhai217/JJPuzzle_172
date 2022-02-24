package com.pachia.comon.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.pachia.comon.game.BaseConfirmDialogFragment;
import com.quby.R;


public class ConfirmDialog extends BaseConfirmDialogFragment {
    TextView btnNo, btnYes, btnRetry;
    String lableOK, lableCancel, lableRetry;
    View.OnClickListener onClickOk, onClickCancel, onClickRetry;

    String title, message;
    TextView dlgTitle, dlgContent;

    public static ConfirmDialog newInstance() {
        ConfirmDialog frag = new ConfirmDialog();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_term_of_use;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void setOkButton(String lable, View.OnClickListener onClickListener) {
        this.lableOK = lable;
        this.onClickOk = onClickListener;
    }

    public void setCancelButton(String lable, View.OnClickListener onClickListener) {
        this.lableCancel = lable;
        this.onClickCancel = onClickListener;
    }

    public void setRetryButton(String lable, View.OnClickListener onClickListener) {
        this.lableRetry = lable;
        this.onClickRetry = onClickListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void initView(View v) {
        btnNo = (TextView) v.findViewById(R.id.btnNo);
        btnYes = (TextView) v.findViewById(R.id.btnYes);
        btnRetry = (TextView) v.findViewById(R.id.btnRetry);
        dlgContent = (TextView) v.findViewById(R.id.dlgContent);
        dlgTitle = (TextView) v.findViewById(R.id.dlgTitle);
        dlgContent.setVisibility(View.GONE);
        dlgTitle.setVisibility(View.GONE);
        btnNo.setVisibility(View.GONE);

        if (lableOK != null) {
            btnYes.setVisibility(View.VISIBLE);
            btnYes.setText(lableOK);
            btnYes.setOnClickListener(onClickOk);
        }
        if (lableCancel != null) {
            btnNo.setVisibility(View.VISIBLE);
            btnNo.setText(lableCancel);
            btnNo.setOnClickListener(onClickCancel);
        }

        if (lableRetry != null) {
            btnRetry.setVisibility(View.VISIBLE);
            btnRetry.setText(lableRetry);
            btnRetry.setOnClickListener(onClickRetry);
        }
        if (title != null) {
            dlgTitle.setVisibility(View.VISIBLE);
            dlgTitle.setText(title);
        }
        if (message != null) {
            dlgContent.setText(message);
            dlgContent.setVisibility(View.VISIBLE);
        }

    }

}
