package com.supho.gui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.supho.gui.float18button.NotiOverlayView;
import com.supho.utils.Constants;
import com.quby.R;
import com.supho.utils.Preference;
import com.supho.utils.Res;

@SuppressLint("ValidFragment")
public class SuphoLogoutDialogWithPopup extends DialogFragment {

    private static String TAG = NotiOverlayView.class.getSimpleName();
    private String urlBanner, urlToOpen;

    Activity activity;
    EventListener eventListener;

    @SuppressLint("ValidFragment")
    public SuphoLogoutDialogWithPopup(Activity activity, String urlBanner, String urlToOpen, EventListener eventListener) {
        this.activity = activity;
        this.urlBanner = urlBanner;
        this.urlToOpen = urlToOpen;
        this.eventListener = eventListener;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        Preference.save(activity , Constants.SHARED_PREF_SHOW_DASHBOARD , true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_logout_popup, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT);

        try {
            Button btnGiftcode = (Button) view.findViewById(R.id.btn_giftcode);
            Button btnFanpage = (Button) view.findViewById(R.id.btn_fanpage);
            Button btnLogout = (Button) view.findViewById(R.id.btn_logout);
            ImageButton btnClose = (ImageButton) view.findViewById(R.id.btn_close);
            ImageView ivBanner = (ImageView) view.findViewById(R.id.iv_banner);
            RelativeLayout mainView = (RelativeLayout) view.findViewById(R.id.layout_main_logout);

            //Todo handle exception load image
            if(!urlBanner.isEmpty()){
                RequestOptions fitOptions = new RequestOptions().fitCenter();
                Glide.with(activity).load(urlBanner).apply(fitOptions).into(ivBanner);
            }
            ivBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!urlToOpen.isEmpty()){
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen));
                        startActivity(browserIntent);
                    }
                }
            });
            btnFanpage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(eventListener!=null){
                        eventListener.onFanpageClick(v);
                    }
                }
            });
            btnGiftcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(eventListener!=null){
                        eventListener.onGiftcodeClick(v);
                    }
                }
            });
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(eventListener!=null){
                        eventListener.onLogoutClick(v);
                    }
                    dismiss();
                }
            });
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(eventListener!=null){
                        eventListener.onDismiss();
                    }
                    dismiss();
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            btnLogout.setText(Res.string(activity , R.string.logout));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(eventListener!=null){
            eventListener.onDismiss();
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
        public void onLogoutClick(View v);
        public void onFanpageClick(View v);
        public void onGiftcodeClick(View v);
        public void onDismiss();
    }

}