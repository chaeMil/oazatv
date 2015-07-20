package com.chaemil.hgms.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.ArchiveMenu;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaemil on 16.10.14.
 */
public class ArchiveMenuAdapter extends ArrayAdapter<ArchiveMenu> {

    ArrayList<ArchiveMenu> menuItems;
    Context context;

    public ArchiveMenuAdapter(Context context, ArrayList<ArchiveMenu> menuItems) {
        super(context, R.layout.archive_menu_item);
        this.menuItems = menuItems;
        this.context = context;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public ArchiveMenu getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            holder = new Holder();

            if (this.getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.archive_menu_main, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.archive_menu_item, parent, false);
            }

            holder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (holder.title != null) {
            holder.title.setText(getItem(position).getLabel());
            holder.menuItem = getItem(position);
        }

        return convertView;
    }

    static class Holder  {
        TextView title;
        ArchiveMenu menuItem;
    }
}