package com.stephengrice.laser;


import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stephengrice.laser.db.DbContract;

import java.util.ArrayList;

public class ScheduledTransactionArrayAdapter extends ArrayAdapter<DbContract.ScheduledTransaction> {

    private static int mLayout = R.layout.item_scheduled_transaction;

    public ScheduledTransactionArrayAdapter(Context context, ArrayList<DbContract.ScheduledTransaction> data) {
        super(context, mLayout, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DbContract.ScheduledTransaction st = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        // Find views
        TextView itemAmount = (TextView) convertView.findViewById(R.id.item_st_amount);
        TextView itemDate = (TextView) convertView.findViewById(R.id.item_st_date);
        TextView itemDescription = (TextView) convertView.findViewById(R.id.item_st_description);
        TextView itemCategory = (TextView) convertView.findViewById(R.id.item_st_category);
        TextView itemRepeat = (TextView) convertView.findViewById(R.id.item_st_repeat);

        // Format necessary data values
        String formattedAmount = MainActivity.formatCurrency(Math.abs(st.amount));
        String formattedDate = MainActivity.formatDate(getContext(), st.date);

        itemAmount.setText(formattedAmount);
        itemDate.setText(formattedDate);
        itemDescription.setText(st.description);
        // If category_id is 0, it is not set, so display the no_category string. Otherwise, display the category title
        itemCategory.setText((st.category_id < 1 ? convertView.getResources().getString(R.string.no_category) : st.category_title));
        itemRepeat.setText(MainActivity.getRepeatText(getContext(), st.repeat.getValue()));

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.item_st_img);
        // Conditional formatting - change from negative-appearing item to positive appearance
        if (st.amount > 0) {
            // Change bg color
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGreen));
            // Change icon imageview
            imgIcon.setImageResource(R.drawable.ic_up_black);
        } else {
            // Change bg color
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintRed));
            // Change icon imageview
            imgIcon.setImageResource(R.drawable.ic_down_black);
        }
        if (!st.enabled) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGray));
        }

        return convertView;
    }
}
