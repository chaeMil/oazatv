package com.chaemil.hgms.Adapters;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by chaemil on 27.10.14.
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
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }

        ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);

        ImageView photoLarge = null;
        if(convertView.findViewById(R.id.photoLarge) != null) {
            photoLarge = (ImageView) convertView.findViewById(R.id.photoLarge);
        }

        TextView label = null;
        if(convertView.findViewById(R.id.label) != null) {
            label = (TextView) convertView.findViewById(R.id.label);
        }

        PhotoalbumRecord rec = getItem(position);

        if(convertView.findViewById(R.id.photoLarge) != null) {
            Picasso.with(getContext()).load(rec.getPhotoLarge()).into(photoLarge);
        }
        if(convertView.findViewById(R.id.label) != null) {
            label.setText(rec.getLabel());
        }
        Picasso.with(getContext()).load(rec.getThumb()).into(thumb);


        return convertView;
    }
}