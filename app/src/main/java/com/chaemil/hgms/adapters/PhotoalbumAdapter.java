package com.chaemil.hgms.adapters;

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
import com.koushikdutta.ion.Ion;

import java.util.List;

import static com.chaemil.hgms.utils.Utils.getScreenHeight;
import static com.chaemil.hgms.utils.Utils.getScreenWidth;

/**
 * Created by chaemil on 13.11.14.
 */
public class PhotoalbumAdapter extends ArrayAdapter<Photo> {

    private int layout;

    public PhotoalbumAdapter(Context context, int layout) {
        super(context, layout);
        this.layout = layout;
    }

    public void swapImageRecords(List<Photo> objects) {
        clear();

        for(Photo object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }

        Photo rec = getItem(position);
        Resources res = getContext().getResources();

        ImageView thumb = null;
        ImageView photoLarge = null;
        TextView label = null;
        TextView photoId = null;
        TextView photoUrl = null;

        if(convertView.findViewById(R.id.thumb) != null) {
            thumb = (ImageView) convertView.findViewById(R.id.thumb);
            //Picasso.with(getContext()).load(rec.getThumb()).into(thumb);
            Ion.with(thumb)
                    /*.placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .animateLoad(spinAnimation)
                    .animateIn(fadeInAnimation)*/
                    .load(rec.getThumb());
            thumb.getLayoutParams().width = getScreenWidth(getContext())/res.getInteger(R.integer.gallery_columns);
            thumb.getLayoutParams().height = getScreenWidth(getContext())/res.getInteger(R.integer.gallery_columns);
        }

        if(convertView.findViewById(R.id.photoLarge) != null) {
            photoLarge = (ImageView) convertView.findViewById(R.id.photoLarge);
            //Picasso.with(getContext()).load(rec.getPhotoBig()).into(photoLarge);
            Ion.with(photoLarge)
                    /*.placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .animateLoad(spinAnimation)
                    .animateIn(fadeInAnimation)*/
                    .load(rec.getPhotoBig());
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

        if(convertView.findViewById(R.id.photoUrl) != null) {
            photoUrl = (TextView) convertView.findViewById(R.id.photoUrl);
            photoUrl.setText(rec.getPhotoBig());
        }

        return convertView;
    }
}
