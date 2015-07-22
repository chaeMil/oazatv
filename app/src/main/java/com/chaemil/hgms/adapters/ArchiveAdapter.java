package com.chaemil.hgms.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.Photo;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.Utils;
import com.chaemil.hgms.view.SquareImageView;
import com.chaemil.hgms.view.ThumbImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaemil on 17.9.14.
 */
public class ArchiveAdapter extends ArrayAdapter<ArchiveItem> {
    private final ArrayList<ArchiveItem> archive;
    private int layout;
    private Context context;

    public ArchiveAdapter(Context context, int layout, ArrayList<ArchiveItem> archive) {
        super(context, layout);
        this.layout = layout;
        this.archive = archive;
        this.context = context;
    }

    public int getCount() {
        return archive.size();
    }

    public ArchiveItem getItem(int position) {
        return archive.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);
            holder = new ViewHolder();
            holder.image = (ThumbImageView) convertView.findViewById(R.id.thumb);
            holder.views = (TextView) convertView.findViewById(R.id.views);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getItem(position).getType().equals(ArchiveItem.TYPE_VIDEO)) {
            Picasso.with(context)
                    .load(archive.get(position).getThumb())
                    .fit()
                    .into(holder.image);
        } else {
            Picasso.with(context)
                    .load(archive.get(position).getThumb())
                    .centerCrop()
                    .resize(800, 800)
                    .into(holder.image);
        }
        holder.title.setText(archive.get(position).getTitle());
        holder.date.setText(archive.get(position).getDate());
        holder.views.setText(archive.get(position).getVideoViews());

        return convertView;
    }

    static class ViewHolder {
        ThumbImageView image;
        TextView views;
        TextView title;
        TextView date;
    }
}