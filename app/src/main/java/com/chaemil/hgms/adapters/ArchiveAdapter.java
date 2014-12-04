package com.chaemil.hgms.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.utils.Utils;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chaemil on 17.9.14.
 */
public class ArchiveAdapter extends ArrayAdapter<ArchiveRecord> {
    private int layout;

    public ArchiveAdapter(Context context, int layout) {
        super(context, layout);
        this.layout = layout;
    }

    public void swapImageRecords(List<ArchiveRecord> objects) {
        clear();

        for(ArchiveRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }
        ImageView background = null;
        if(convertView.findViewById(R.id.background) != null) {
            background = (ImageView) convertView.findViewById(R.id.background);
        }
        ImageView videoThumb = (ImageView) convertView.findViewById(R.id.thumb);
        TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
        TextView videoDate = (TextView) convertView.findViewById(R.id.videoDate);
        TextView videoURL = (TextView) convertView.findViewById(R.id.videoURL);
        TextView videoViews = (TextView) convertView.findViewById(R.id.videoViews);
        TextView albumId = (TextView) convertView.findViewById(R.id.albumId);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        ArchiveRecord rec = getItem(position);

        if(convertView.findViewById(R.id.background) != null) {
            Picasso.with(getContext()).load(rec.getThumbBlur()).into(background);
            /*Ion.with(background)
                    /*.placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .animateLoad(spinAnimation)
                    .animateIn(fadeInAnimation)
                    .load(rec.getThumbBlur());*/
        }
        Picasso.with(getContext()).load(rec.getThumb()).into(videoThumb);
        /*Ion.with(videoThumb)
                /*.placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .animateLoad(spinAnimation)
                .animateIn(fadeInAnimation)
                .load(rec.getThumb());*/
        videoName.setText(Utils.getStringWithRegularCustomFont(getContext(),rec.getTitle(),"Titillium-RegularUpright.otf"));
        videoDate.setText(rec.getVideoDate());
        videoURL.setText(rec.getVideoUrl());
        videoViews.setText(rec.getVideoViews());
        albumId.setText(rec.getAlbumId());
        type.setText(rec.getType());


        return convertView;
    }
}