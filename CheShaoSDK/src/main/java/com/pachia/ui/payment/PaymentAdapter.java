package com.pachia.ui.payment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pachia.comon.object.ItemPayObj;
import com.pachia.comon.utils.DeviceUtils;
import com.quby.R;

import static com.supho.CheShaoSDK.getApplicationContext;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    ItemPayObj itemPayObj;
    int widthItem, heightItem, width, height;
    Context context;
    OnClickItemPaymentListener onClickItemPaymentListener;

    public PaymentAdapter(Context context, ItemPayObj itemPayObj, OnClickItemPaymentListener onClickItemPaymentListener) {
        this.context = context;
        this.itemPayObj = itemPayObj;
        this.onClickItemPaymentListener = onClickItemPaymentListener;
        width = getScreenWidthInPixel();
        height = getScreenHeightInPixel();
        widthItem = ((int) (width / (itemPayObj.getConfig().getColumns())));
        heightItem = (int) (height / itemPayObj.getConfig().getRows());
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        ItemPayObj.Item item = itemPayObj.getItems().get(position);
        Log.d(PaymentAdapter.class.getSimpleName(), "widthItem: " + widthItem + " , heightItem: " + heightItem);

        int orientation = context.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams paramsMsg = new LinearLayout.LayoutParams((int) (widthItem * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsMsg.gravity = Gravity.CENTER;
            holder.imageView.setLayoutParams(paramsMsg);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthItem, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.imageView.setLayoutParams(params);
        }


        Glide.with(getApplicationContext()).asBitmap()
                .load(item.getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })

                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemPaymentListener.onClickItem(v, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemPayObj.getItems().isEmpty() ? 0 : itemPayObj.getItems().size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item_pay);
        }
    }


    public int getScreenWidthInPixel() {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getScreenHeightInPixel() {
        try {
            return context.getResources().getDisplayMetrics().heightPixels - (int) (50 * DeviceUtils.getDensity(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public interface OnClickItemPaymentListener {
        void onClickItem(View view, ItemPayObj.Item item);
    }
}
