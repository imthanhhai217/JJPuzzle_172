package com.supho.gui.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pachia.comon.game.CheShaoSdk;
import com.bumptech.glide.Glide;
import com.quby.R;
import com.supho.model.ItemMenuDashboard;

import java.util.List;

public class DashboardMenuAdapter extends RecyclerView.Adapter<DashboardMenuAdapter.ViewHolder> {

    private static final String TAG = DashboardMenuAdapter.class.getSimpleName();

    private List<ItemMenuDashboard> listItemMenuDashboard;
    private Context activity;
    private boolean isLandscape, isTabletSize;

    public DashboardMenuAdapter(List<ItemMenuDashboard> listItemMenuDashboards, @NonNull Context activity, boolean isLandscape, boolean isTabletSize) {
        this.listItemMenuDashboard = listItemMenuDashboards;
        this.activity = activity;
        this.isLandscape = isLandscape;
        this.isTabletSize = isTabletSize;
        if (this.activity == null)
            this.activity = CheShaoSdk.getInstance().getApplication();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        if (isTabletSize) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_dashboard_tablet, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_dashboard, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemMenuDashboard itemMenuDashboard = listItemMenuDashboard.get(position);
        if (isLandscape || isTabletSize) {
            holder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }
        if (itemMenuDashboard.isClick()) {
            holder.tvTitle.setTextColor(activity.getResources().getColor(R.color.active_text));
            Glide.with(activity).load(itemMenuDashboard.getUrlIconActive()).into(holder.btnImageCenter);
        } else {
            holder.tvTitle.setTextColor(activity.getResources().getColor(R.color.deactive_text));
            Glide.with(activity).load(itemMenuDashboard.getUrlIcon()).into(holder.btnImageCenter);
        }
        holder.tvTitle.setText(itemMenuDashboard.getTitle_menu());
        if (itemMenuDashboard.isHasNtf()) {
            holder.hasNotiView.setVisibility(View.VISIBLE);
        } else {
            holder.hasNotiView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listItemMenuDashboard.size();
    }

    //View holder customise
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageButton btnImageCenter;
        private TextView tvTitle;
        private View hasNotiView;

        public ViewHolder(View itemView) {
            super(itemView);
            btnImageCenter = (ImageButton) itemView.findViewById(R.id.btn_image_center);
            tvTitle = itemView.findViewById(R.id.tv_title);
            hasNotiView = itemView.findViewById(R.id.ic_ntf);
        }
    }
}
