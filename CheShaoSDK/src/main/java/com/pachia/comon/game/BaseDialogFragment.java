package com.pachia.comon.game;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pachia.comon.constants.Constants;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.Utils;
import com.quby.R;

public abstract class BaseDialogFragment extends DialogFragment {

    protected Activity mActivity;
    DialogFragment progressDialog;

    /**
     * Layout of activity
     *
     * @return id of resource layout
     */
    @LayoutRes
    protected abstract int getLayoutResource();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResource(), container, false);
        //Setting dialog background and animation
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0.8f);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int orientation = mActivity.getResources().getConfiguration().orientation;
        PrefManager.saveInt(mActivity, getTag() + Constants.SCREEN_ORIENTATION_CURRENT, orientation);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void showProgressDialog(boolean show, String mess) {
        Utils.showLoading(mActivity,show);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
