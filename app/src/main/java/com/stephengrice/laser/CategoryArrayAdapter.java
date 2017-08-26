package com.stephengrice.laser;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stephengrice.laser.db.DbContract;

import java.util.ArrayList;

public class CategoryArrayAdapter extends ArrayAdapter<DbContract.Category> {
    private static int mLayout = android.R.layout.select_dialog_item;

    public CategoryArrayAdapter(Context context, ArrayList<DbContract.Category> data) {
        super(context, mLayout, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DbContract.Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        TextView itemTitle = (TextView) convertView.findViewById(R.id.item_title);

        itemTitle.setText(category.title);

        return convertView;
    }
}
