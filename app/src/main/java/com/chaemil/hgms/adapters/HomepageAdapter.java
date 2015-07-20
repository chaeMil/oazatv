package com.chaemil.hgms.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.utils.SmartLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chaemil on 20.7.15.
 */
public class HomepageAdapter extends ArrayAdapter<ArchiveItem> {

    Context context;
    ArrayList<ArchiveItem> homePage;

    public HomepageAdapter(Context context, ArrayList<ArchiveItem> homePage, int resource) {
        super(context, resource);
        this.context = context;
        this.homePage = homePage;
    }

    @Override
    public int getCount() {
        return homePage.size();
    }

    @Override
    public ArchiveItem getItem(int position) {
        return homePage.get(position);
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if (homePage.get(position).isBigLayout()) {
            return 0;
        }
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        //SmartLog.log("position", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            if (this.getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.home_first_video, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.home_block, parent, false);
            }

            holder = new Holder();

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
            holder.thumbBlur = (ImageView) convertView.findViewById(R.id.thumbBlur);
            holder.playCount = (TextView) convertView.findViewById(R.id.views);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.title.setText(getItem(position).getTitle());
        holder.playCount.setText(getItem(position).getVideoViews());
        holder.date.setText(getItem(position).getVideoDate());
        Picasso.with(context).load(getItem(position).getThumb()).resize(320, 180).into(holder.thumb);
        if (position == 0) {
            Picasso.with(context).load(getItem(position).getThumb()).into(holder.thumb);
            if (holder.thumbBlur != null) {
                Picasso.with(context).load(getItem(position).getThumbBlur()).resize(320, 180).into(holder.thumbBlur);
            }
        }

        return convertView;
    }

    static class Holder {
        TextView title;
        TextView date;
        TextView playCount;
        ImageView thumb;
        ImageView thumbBlur;
    }
}
