package com.chaemil.hgms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.chaemil.hgms.R;
import com.squareup.picasso.Picasso;

import java.util.List;

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

        ImageView thumb = null;
        if(convertView.findViewById(R.id.thumb) != null) {
             thumb = (ImageView) convertView.findViewById(R.id.thumb);
        }

        PhotoalbumRecord rec = getItem(position);

        if(convertView.findViewById(R.id.thumb) != null) {
            Picasso.with(getContext()).load(rec.getThumb()).into(thumb);
            thumb.getLayoutParams().width = getScreenWidth(getContext())/3;
            thumb.getLayoutParams().height = getScreenWidth(getContext())/3;
        }

        Picasso.with(getContext()).load(rec.getThumb()).into(thumb);


        return convertView;
    }
}
