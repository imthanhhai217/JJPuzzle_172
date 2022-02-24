package com.pachia.balat.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.brother.balat.demo.R;
import com.pachia.balat.demo.activities.InAppPurchaseActivity;
import com.pachia.balat.demo.activities.PlayActivity;
import com.pachia.balat.demo.models.ImageModel;
import com.pachia.balat.demo.utils.Global;

import java.util.ArrayList;

public class JJPuzzleViewAdapter extends RecyclerView.Adapter<JJPuzzleViewAdapter.ImageHolder> {

    private static final String TAG = "ImageViewAdapter";
    private ArrayList<ImageModel> mListData;
    private Context mContext;

    public JJPuzzleViewAdapter(ArrayList<ImageModel> mListData, Context mContext) {
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_item,parent,false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        ImageModel model = mListData.get(position);
        Bitmap bitmap = model.getBitmap();
        holder.imgData.setImageBitmap(bitmap);
        if (model.getLockState()){
            holder.imgLock.setVisibility(View.GONE);
        }else {
            holder.imgLock.setVisibility(View.VISIBLE);
        }

        holder.imgData.setOnClickListener(v -> {
            Intent intent;
            if (model.getLockState()){
                intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra(Global.ASSETS_NAME, model.getName());
            }else {
                intent = new Intent(mContext, InAppPurchaseActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        ConstraintLayout clItemMain;
        ImageView imgData;
        ImageView imgLock;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            clItemMain = itemView.findViewById(R.id.clItemMain);
            imgData = itemView.findViewById(R.id.imgData);
            imgLock = itemView.findViewById(R.id.imgLock);
        }
    }
}
