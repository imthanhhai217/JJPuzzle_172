package com.supho.gui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quby.R;
import com.supho.gui.float18button.NotiOverlayView;

@SuppressLint("ValidFragment")
public class SuphoConfirmDialog extends DialogFragment {

    private static String TAG = NotiOverlayView.class.getSimpleName();
    private final String message;
    private final String strConfirm;
    private final String strCancel;

    Activity activity;
    EventListener eventListener;

    @SuppressLint("ValidFragment")
    public SuphoConfirmDialog(Activity activity, String message, String strConfirm, String strCancel, EventListener eventListener) {
        this.activity = activity;
        this.message = message;
        this.strConfirm = strConfirm;
        this.strCancel = strCancel;
        this.eventListener = eventListener;
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        try {
            TextView txtMessage = (TextView) view.findViewById(R.id.txt_message);
            final Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
            final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            btnConfirm.setText(strConfirm);
            btnCancel.setText(strCancel);
            txtMessage.setText(message);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eventListener != null) {
                        eventListener.onCancelClick(view);
                    }
                    dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eventListener != null) {
                        eventListener.onConfirmClick(view);
                    }
                    dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (eventListener != null) {
            eventListener.onCancelClick(null);
        }
    }

    public void show() {
        try {
            show(activity.getFragmentManager(), TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public interface EventListener {
        public void onCancelClick(View v);

        public void onConfirmClick(View v);
    }

}