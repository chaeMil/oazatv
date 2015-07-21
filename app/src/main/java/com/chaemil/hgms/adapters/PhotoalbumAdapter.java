package com.chaemil.hgms.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.model.Photo;
import com.chaemil.hgms.view.SquareImageView;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.chaemil.hgms.utils.Utils.getScreenHeight;
import static com.chaemil.hgms.utils.Utils.getScreenWidth;

/**
 * Created by chaemil on 13.11.14.
 */
public class PhotoalbumAdapter extends ArrayAdapter<Photo> {

    private int layout;
    private Activity activity;
    private ArrayList<Photo> photos;
    private int thumbWidth;

    public PhotoalbumAdapter(Context context, int layout, Activity activity, ArrayList<Photo> photos, int thumbWidth) {
        super(context, layout);
        this.layout = layout;
        this.activity = activity;
        this.photos = photos;
        this.thumbWidth = thumbWidth;
    }

    public int getCount() {
        return photos.size();
    }

    public Photo getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.photo_thumb, parent, false);
            holder = new ViewHolder();
            holder.image = (SquareImageView) convertView.findViewById(R.id.thumb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(activity.getApplicationContext())
                .load(photos.get(position).getThumb())
                .resize(thumbWidth, thumbWidth)
                .centerCrop()
                .into(holder.image);


        return convertView;
    }

    static class ViewHolder {
        SquareImageView image;
    }
}
