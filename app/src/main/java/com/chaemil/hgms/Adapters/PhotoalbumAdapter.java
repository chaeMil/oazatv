package com.chaemil.hgms.Adapters;

import android.app.ActionBar;
import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.chaemil.hgms.Utils.Utils.getDisplayWidth;

import java.util.List;

/**
 * Created by chaemil on 27.10.14.
 */
public class PhotoalbumAdapter extends ArrayAdapter<PhotoalbumRecord> {
    private int layout;

    private int thumbWidth = getDisplayWidth(getContext())/3;
    private int thumbHeight = getDisplayWidth(getContext())/3;

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
        ImageView thumb;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
            thumb = new ImageView(getContext());
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(thumbWidth,thumbHeight);
            //thumb.setLayoutParams(new ViewGroup.LayoutParams(thumbWidth, thumbHeight));
            thumb.setLayoutParams(parms);
            thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //thumb.setPadding(8, 8, 8, 8);
        } else {
            thumb = (ImageView) convertView;
        }

        Log.d("thumbWidth",Integer.toString(thumbWidth));




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