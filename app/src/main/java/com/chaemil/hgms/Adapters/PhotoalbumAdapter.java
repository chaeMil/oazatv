package com.chaemil.hgms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chaemil on 27.10.14.
 */
public class PhotoalbumAdapter extends ArrayAdapter<PhotoalbumRecord> {

    public PhotoalbumAdapter(Context context) {
        super(context, R.layout.photo_thumb);
    }

    public void swapImageRecords(List<PhotoalbumRecord> objects) {
        clear();

        for(PhotoalbumRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.photo_thumb, parent, false);
        }
        ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);

        PhotoalbumRecord rec = getItem(position);

        Picasso.with(getContext()).load(rec.getThumb()).into(thumb);


        return convertView;
    }
}