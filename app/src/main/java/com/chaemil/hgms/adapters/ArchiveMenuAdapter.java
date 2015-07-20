package com.chaemil.hgms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.model.ArchiveMenu;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.Utils;

import java.util.List;

/**
 * Created by chaemil on 16.10.14.
 */
public class ArchiveMenuAdapter extends ArrayAdapter<ArchiveMenu> {

    public ArchiveMenuAdapter(Context context) {
        super(context, R.layout.archive_menu_item);
    }

    public void swapImageRecords(List<ArchiveMenu> objects) {
        clear();

        for(ArchiveMenu object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if(convertView == null) {
            if (position == 0) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.archive_menu_main,
                        parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.archive_menu_item,
                        parent, false);
            }

        //}

        //standard menu items
        TextView menuItem = (TextView) convertView.findViewById(R.id.menuItem);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView titleToShow = (TextView) convertView.findViewById(R.id.titleToShow);

        ArchiveMenu archiveMenu = getItem(position);

        if (menuItem != null) {
            menuItem.setText(Utils.getStringWithRegularCustomFont(getContext(),
                    archiveMenu.getLabel(), Constants.FONT_REGULAR_UPRIGHT));
        }
        if (type != null) {
            type.setText(archiveMenu.getType());
        }

        if (content != null) {
            content.setText(archiveMenu.getContent());
        }

        if (titleToShow != null) {
            titleToShow.setText(archiveMenu.getTitleToShow());
        }

        return convertView;
    }

    @Override
    public int getPosition(ArchiveMenu item) {
        return super.getPosition(item);
    }
}