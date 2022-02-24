package com.supho.gui.view.adapter;


import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supho.model.NtfModel;
import com.quby.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khaitran on 9/18/17.
 */

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<NtfModel> listNtf = new ArrayList<>();

    public CustomPagerAdapter(Context context , List<NtfModel> listModel) {
        mContext = context;
        this.listNtf = listModel;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_notification, collection, false);
        TextView textView = (TextView) layout.findViewById(R.id.txt);
        textView.setText(listNtf.get(position).getTitle());
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return listNtf.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return listNtf.get(position).getTitle();
    }
}
