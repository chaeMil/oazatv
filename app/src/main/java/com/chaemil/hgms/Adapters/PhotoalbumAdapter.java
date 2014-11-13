package com.chaemil.hgms.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.chaemil.hgms.PhotoalbumGallery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chaemil on 13.11.14.
 */
public class PhotoalbumAdapter extends BaseAdapter {

    private final Context context;
    private final int imageWidth;
    private Activity activity;
    private ArrayList<PhotoalbumRecord> photos = new ArrayList<PhotoalbumRecord>();

    public PhotoalbumAdapter(Context context, Activity activity, ArrayList<PhotoalbumRecord> photos, int imageWidth) {
        this.activity = activity;
        this.photos = photos;
        this.context = context;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return this.photos.size();
    }

    @Override
    public Object getItem(int i) {
        return this.photos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(activity);
        }
        else {
            imageView = (ImageView) convertView;
        }

        PhotoalbumRecord rec = (PhotoalbumRecord) getItem(position);

        Picasso.with(context).load(rec.getThumb()).into(imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));

        // image view click listener
        imageView.setOnClickListener(new OnImageClickListener(position));

        return imageView;
    }

    class OnImageClickListener implements View.OnClickListener {

        int _postion;

        // constructor
        public OnImageClickListener(int position) {
            this._postion = position;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            Intent i = new Intent(activity, PhotoalbumGallery.class);
            i.putExtra("position", _postion);
            activity.startActivity(i);
        }

    }
}
