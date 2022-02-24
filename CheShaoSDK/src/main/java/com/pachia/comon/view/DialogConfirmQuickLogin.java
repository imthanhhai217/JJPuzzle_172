package com.pachia.comon.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.pachia.comon.game.BaseConfirmDialogFragment;
import com.pachia.comon.listener.IDialogListener;
import com.quby.R;


public class DialogConfirmQuickLogin extends BaseConfirmDialogFragment {
    IDialogListener listener;

    public static DialogConfirmQuickLogin newInstance() {
        DialogConfirmQuickLogin frag = new DialogConfirmQuickLogin();
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

    public void initView(View v) {
        TextView btnNo = (TextView) v.findViewById(R.id.btnNo);
        TextView btnYes = (TextView) v.findViewById(R.id.btnYes);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCancel();
                dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onOk();
                dismiss();
            }
        });
    }

    public void setListener(IDialogListener listener) {
        this.listener = listener;
    }
}
