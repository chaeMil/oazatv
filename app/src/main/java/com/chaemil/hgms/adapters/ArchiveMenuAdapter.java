package com.chaemil.hgms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;

import java.util.List;

/**
 * Created by chaemil on 16.10.14.
 */
public class ArchiveMenuAdapter extends ArrayAdapter<ArchiveMenuRecord> {

    public ArchiveMenuAdapter(Context context) {
        super(context, R.layout.archive_menu_item);
    }

    public void swapImageRecords(List<ArchiveMenuRecord> objects) {
        clear();

        for(ArchiveMenuRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.archive_menu_item,
                    parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        TextView menuItem = (TextView) convertView.findViewById(R.id.menuItem);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView titleToShow = (TextView) convertView.findViewById(R.id.titleToShow);

        ArchiveMenuRecord archiveMenuRecord = getItem(position);

        menuItem.setText(Utils.getStringWithRegularCustomFont(getContext(),
                archiveMenuRecord.getLabel(), Basic.FONT_REGULAR_UPRIGHT));
        type.setText(archiveMenuRecord.getType());
        content.setText(archiveMenuRecord.getContent());
        titleToShow.setText(archiveMenuRecord.getTitleToShow());

        return convertView;
    }

    @Override
    public int getPosition(ArchiveMenuRecord item) {
        return super.getPosition(item);
    }
}