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
 * Created by chaemil on 17.9.14.
 */
public class NewVideosAdapter extends ArrayAdapter<NewVideosRecord> {
    //private ImageLoader mImageLoader;
    private int layout;

    public NewVideosAdapter(Context context, int layout) {
        super(context, layout);
        this.layout = layout;
        //mImageLoader = new ImageLoader(VolleyApplication.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    public void swapImageRecords(List<NewVideosRecord> objects) {
        clear();

        for(NewVideosRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        //NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.thumb);
        ImageView videoThumb = (ImageView) convertView.findViewById(R.id.thumb);
        TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
        TextView videoDate = (TextView) convertView.findViewById(R.id.videoDate);
        TextView videoURL = (TextView) convertView.findViewById(R.id.videoURL);
        TextView videoViews = (TextView) convertView.findViewById(R.id.videoViews);
        TextView albumId = (TextView) convertView.findViewById(R.id.albumId);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        NewVideosRecord archiveDataRecord = getItem(position);

        //videoThumb.setImageUrl(archiveDataRecord.getThumb(), mImageLoader);
        Picasso.with(getContext()).load(archiveDataRecord.getThumb()).into(videoThumb);
        videoName.setText(archiveDataRecord.getTitle());
        videoDate.setText(archiveDataRecord.getVideoDate());
        videoURL.setText(archiveDataRecord.getVideoUrl());
        videoViews.setText(archiveDataRecord.getVideoViews());
        albumId.setText(archiveDataRecord.getAlbumId());
        type.setText(archiveDataRecord.getType());


        return convertView;
    }
}