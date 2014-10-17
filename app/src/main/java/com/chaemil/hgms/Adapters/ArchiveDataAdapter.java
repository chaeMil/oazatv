package com.chaemil.hgms.Adapters;


import android.app.Activity;
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
import com.chaemil.hgms.Utils.BitmapLruCache;
import com.chaemil.hgms.Utils.VolleyApplication;

import java.util.List;

/**
 * Created by chaemil on 17.9.14.
 */
public class ArchiveDataAdapter extends ArrayAdapter<ArchiveDataRecord> {
    private ImageLoader mImageLoader;

    public ArchiveDataAdapter(Context context) {
        super(context, R.layout.archive_block);

        mImageLoader = new ImageLoader(VolleyApplication.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    public void swapImageRecords(List<ArchiveDataRecord> objects) {
        clear();

        for(ArchiveDataRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.archive_block, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.thumb);
        TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
        TextView videoDate = (TextView) convertView.findViewById(R.id.videoDate);
        TextView videoURL = (TextView) convertView.findViewById(R.id.videoURL);
        TextView videoViews = (TextView) convertView.findViewById(R.id.videoViews);
        TextView albumId = (TextView) convertView.findViewById(R.id.albumId);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        ArchiveDataRecord archiveDataRecord = getItem(position);

        imageView.setImageUrl(archiveDataRecord.getThumb(), mImageLoader);
        videoName.setText(archiveDataRecord.getTitle());
        videoDate.setText(archiveDataRecord.getVideoDate());
        videoURL.setText(archiveDataRecord.getVideoUrl());
        videoViews.setText(archiveDataRecord.getVideoViews());
        albumId.setText(archiveDataRecord.getAlbumId());
        type.setText(archiveDataRecord.getType());


        return convertView;
    }
}