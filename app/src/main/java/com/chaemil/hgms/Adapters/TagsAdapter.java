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
 * Created by chaemil on 27.10.14.
 */
public class TagsAdapter extends ArrayAdapter<TagsRecord> {

    public TagsAdapter(Context context) {
        super(context, R.layout.tag);
    }

    public void swapImageRecords(List<TagsRecord> objects) {
        clear();

        for(TagsRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag, parent, false);
        }
        TextView tagText = (TextView) convertView.findViewById(R.id.tagText);
        TextView tag = (TextView) convertView.findViewById(R.id.tag);

        TagsRecord rec = getItem(position);

        tagText.setText(rec.getTagText());
        tag.setText(rec.getTag());


        return convertView;
    }
}