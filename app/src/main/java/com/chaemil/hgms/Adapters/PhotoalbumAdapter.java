package com.chaemil.hgms.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.chaemil.hgms.Utils.Utils.getScreenHeight;
import static com.chaemil.hgms.Utils.Utils.getScreenWidth;

/**
 * Created by chaemil on 13.11.14.
 */
public class PhotoalbumAdapter extends ArrayAdapter<PhotoalbumRecord> {

    private int layout;

    public PhotoalbumAdapter(Context context, int layout) {
        super(context, layout);
        this.layout = layout;
    }

    public void swapImageRecords(List<PhotoalbumRecord> objects) {
        clear();

        for(PhotoalbumRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }

        PhotoalbumRecord rec = getItem(position);
        Resources res = getContext().getResources();

        ImageView thumb = null;
        ImageView photoLarge = null;
        TextView label = null;
        TextView photoId = null;

        if(convertView.findViewById(R.id.thumb) != null) {
            thumb = (ImageView) convertView.findViewById(R.id.thumb);
            Picasso.with(getContext()).load(rec.getThumb()).into(thumb);
            thumb.getLayoutParams().width = getScreenWidth(getContext())/res.getInteger(R.integer.gallery_columns);
            thumb.getLayoutParams().height = getScreenWidth(getContext())/res.getInteger(R.integer.gallery_columns);
        }

        if(convertView.findViewById(R.id.photoLarge) != null) {
            photoLarge = (ImageView) convertView.findViewById(R.id.photoLarge);
            Picasso.with(getContext()).load(rec.getPhotoBig()).into(photoLarge);
            photoLarge.getLayoutParams().width = getScreenWidth(getContext());
            photoLarge.getLayoutParams().height = getScreenHeight(getContext());
        }

        if(convertView.findViewById(R.id.label) != null) {
            label = (TextView) convertView.findViewById(R.id.label);
            label.setText(rec.getLabel());
        }

        if(convertView.findViewById(R.id.photoId) != null) {
            photoId = (TextView) convertView.findViewById(R.id.photoId);
            photoId.setText(rec.getPhotoId());
        }

        return convertView;
    }
}
