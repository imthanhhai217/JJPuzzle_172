package com.pachia.comon.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quby.R;


public class CustomToast extends Toast {
    private Context mContext;

    public interface TOAST_TYPE {
        int INFO = 0;
        int WARNING = 1;
        int ERR = 2;
        int HELLO = 3;
        int TODO = 4;
    }

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CustomToast(Context context) {
        super(context);
        this.mContext = context;
    }


    public CustomToast(Context context, int type, String message) {
        super(context);
        this.mContext = context;
        initView(message, type);
    }

    public CustomToast(Context context, int type, String message, int gravity) {
        super(context);
        this.mContext = context;
        initView(message, type);
    }


    /**
     * @param message
     * @param type
     */
    public void initView(String message, int type) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_notify, null);

        TextView tvMessage = view.findViewById(R.id.tvDescription);
        ImageView ivToast = view.findViewById(R.id.ivToast);
        LinearLayout layoutToast = view.findViewById(R.id.layoutToast);
        ivToast.setVisibility(View.GONE);

        tvMessage.setText(message);
        switch (type) {
            case TOAST_TYPE.INFO:
//                layoutToast.setBackgroundColor(mContext.getResources().getColor(R.color.md_light_green_800));
//                ivToast.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon));
                setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
                break;
            case TOAST_TYPE.WARNING:
                layoutToast.setBackground(mContext.getResources().getDrawable(R.drawable.bg_toast_warning));
                ivToast.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_warning));
                setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
                break;
            case TOAST_TYPE.ERR:
                layoutToast.setBackground(mContext.getResources().getDrawable(R.drawable.bg_toast_err));
                ivToast.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_toast_err));
                setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
                break;
            case TOAST_TYPE.HELLO:
                ivToast.setVisibility(View.VISIBLE);
                break;

            case TOAST_TYPE.TODO:
//                layoutToast.setBackgroundColor(mContext.getResources().getColor(R.color.md_light_blue_A400));
//                ivToast.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon));
                break;
            default:
                break;
        }


        setDuration(Toast.LENGTH_SHORT);

        setView(view);
    }
}

