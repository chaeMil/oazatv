package com.chaemil.hgms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chaemil.hgms.R;
import com.chaemil.hgms.Utils.VolleyApplication;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chaemil on 17.9.14.
 */
public class FirstVideoDataAdapter extends ArrayAdapter<FirstVideoDataRecord> {

    public FirstVideoDataAdapter(Context context) {
        super(context, R.layout.archive_block);

    }

    public void swapImageRecords(List<FirstVideoDataRecord> objects) {
        clear();

        for(FirstVideoDataRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_first_video, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        ImageView videoThumb = (ImageView) convertView.findViewById(R.id.thumb);
        ImageView thumbBlur = (ImageView) convertView.findViewById(R.id.thumb);
        TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
        TextView videoDate = (TextView) convertView.findViewById(R.id.videoDate);
        TextView videoURL = (TextView) convertView.findViewById(R.id.videoURL);
        TextView videoViews = (TextView) convertView.findViewById(R.id.videoViews);
        TextView albumId = (TextView) convertView.findViewById(R.id.albumId);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        FirstVideoDataRecord rec = getItem(position);

        Picasso.with(getContext()).load(rec.getThumb()).into(videoThumb);
        Picasso.with(getContext()).load(rec.getThumbBlur()).into(thumbBlur);
        videoName.setText(rec.getTitle());
        videoDate.setText(rec.getVideoDate());
        videoURL.setText(rec.getVideoUrl());
        videoViews.setText(rec.getVideoViews());
        albumId.setText(rec.getAlbumId());
        type.setText(rec.getType());


        return convertView;
    }
}