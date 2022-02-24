package com.supho.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pachia.comon.utils.ToastUtils;
import com.quby.R;
import com.supho.utils.Res;

public class SuphoImageGaleryActivity extends AppCompatActivity {
    private GridView grdImages;
    private ImageButton btnSelect, btnCancel;

    private ImageAdapter imageAdapter;
    private TextView tvNumberSelected, tvHeader;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private int count;
    private int countSelected = 0;
    private int limitSelect = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_game_image_galery);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            limitSelect = bundle.getInt("limit");
        }
        grdImages = (GridView) findViewById(R.id.grdImages);
        btnSelect = (ImageButton) findViewById(R.id.btnSelect);
        btnCancel = (ImageButton) findViewById(R.id.btn_cancel_galery);
        tvNumberSelected = (TextView) findViewById(R.id.tvNumberSelected);
        tvHeader = (TextView) findViewById(R.id.tvHeader);

        try {
            tvHeader.setText(Res.string(this, R.string.images_selected));
            final String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
            @SuppressWarnings("deprecation")
            String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.DATE_TAKEN};
            Cursor imagecursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    orderBy);
            count = imagecursor.getCount();
            arrPath = new String[count];
            ids = new int[count];
            thumbnailsselection = new boolean[count];
            final int dataColumn = imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            final int idColumn = imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            for (int i = 0; i < count; i++) {
                imagecursor.moveToPosition(i);
                arrPath[i] = imagecursor.getString(dataColumn);
                ids[i] = imagecursor.getInt(idColumn);

            }

            tvNumberSelected.setText("" + (countSelected + 3 - limitSelect));

            imageAdapter = new ImageAdapter(this, new OnSelectImageGalleryListener() {
                @Override
                public void onSelectImageGalleryListener(CheckBox cb, int id) {
                    try {
                        countSelected++;
                        if (countSelected > limitSelect) {
                            ToastUtils.showShortToast(cb.getContext(), getString(R.string.alert_image_validate));
                            countSelected--;
                            cb.setChecked(false);
                            thumbnailsselection[id] = false;
                        } else {
                            tvNumberSelected.setText("" + (countSelected + 3 - limitSelect));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onUnselectImageGalleryListener(CheckBox cb, int id) {
                    try {
                        countSelected--;
                        tvNumberSelected.setText("" + (countSelected + 3 - limitSelect));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            grdImages.setAdapter(imageAdapter);
            imagecursor.close();


            btnSelect.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    try {
                        final int len = thumbnailsselection.length;
                        int cnt = 0;
                        String selectImages = "";
                        for (int i = 0; i < len; i++) {
                            if (thumbnailsselection[i]) {
                                cnt++;
                                selectImages = selectImages + arrPath[i] + "|";
                            }
                        }
                        if (cnt == 0) {
                            Toast.makeText(SuphoImageGaleryActivity.this, "Please select at least one image", Toast.LENGTH_LONG).show();
                        } else {

                            Log.d("SelectedImages", selectImages);
                            Intent i = new Intent();
                            i.putExtra("data", selectImages);
                            setResult(Activity.RESULT_OK, i);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater mInflater;
        private OnSelectImageGalleryListener listener;

        public ImageAdapter(Context context, OnSelectImageGalleryListener listener) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.listener = listener;
        }

        public int getCount() {
            return count;
        }

        public ViewHolder getItem(int position) {
            return getItem(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                final ViewHolder holder;
                GridView grid = (GridView) parent;
                int itemWidth = grid.getRequestedColumnWidth();
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    itemWidth = itemWidth * 2;
                }
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.image_galery_item, null);
                    holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
                    holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);
                    holder.imgThumb.getLayoutParams().width = itemWidth;
                    holder.imgThumb.getLayoutParams().height = itemWidth;

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.chkImage.setId(position);
                holder.imgThumb.setId(position);
                holder.chkImage.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        int id = cb.getId();
                        if (thumbnailsselection[id]) {
                            cb.setChecked(false);
                            thumbnailsselection[id] = false;
                            if (listener != null) {
                                listener.onUnselectImageGalleryListener(cb, id);
                            }
                        } else {
                            cb.setChecked(true);
                            thumbnailsselection[id] = true;
                            if (listener != null) {
                                listener.onSelectImageGalleryListener(cb, id);
                            }
                        }
                    }
                });
                holder.imgThumb.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        int id = holder.chkImage.getId();
                        if (thumbnailsselection[id]) {
                            holder.chkImage.setChecked(false);
                            thumbnailsselection[id] = false;
                            if (listener != null) {
                                listener.onUnselectImageGalleryListener(holder.chkImage, id);
                            }
                        } else {
                            holder.chkImage.setChecked(true);
                            thumbnailsselection[id] = true;
                            if (listener != null) {
                                listener.onSelectImageGalleryListener(holder.chkImage, id);
                            }
                        }
                    }
                });
                RequestOptions cropOptions = new RequestOptions().centerCrop();
                Glide.with(context).load(arrPath[position])
                        .apply(cropOptions)
                        .into(holder.imgThumb);

                holder.chkImage.setChecked(thumbnailsselection[position]);
                holder.id = position;
                return convertView;
            } catch (Exception e) {
                e.printStackTrace();
                return mInflater.inflate(R.layout.image_galery_item, null);
            }
        }
    }

    interface OnSelectImageGalleryListener {
        void onSelectImageGalleryListener(CheckBox cb, int id);

        void onUnselectImageGalleryListener(CheckBox cb, int id);
    }

    class ViewHolder {
        ImageView imgThumb;
        CheckBox chkImage;
        int id;
    }
}