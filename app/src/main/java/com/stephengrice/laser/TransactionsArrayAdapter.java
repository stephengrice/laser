package com.stephengrice.laser;

import android.content.Context;
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


public class TransactionsArrayAdapter extends ArrayAdapter<DbContract.Transaction> {

    private static int mLayout = R.layout.item_transaction;

    public TransactionsArrayAdapter(Context context, ArrayList<DbContract.Transaction> data) {
        super(context, mLayout, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DbContract.Transaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        // Find views
        TextView itemAmount = (TextView) convertView.findViewById(R.id.item_transaction_amount);
        TextView itemDate = (TextView) convertView.findViewById(R.id.item_transaction_date);
        TextView itemDescription = (TextView) convertView.findViewById(R.id.item_transaction_description);
        TextView itemCategory = (TextView) convertView.findViewById(R.id.item_transaction_category);

        // Format necessary data values
        String formattedAmount = MainActivity.formatCurrency(Math.abs(transaction.amount));
        String formattedDate = MainActivity.formatDate(getContext(), transaction.date);

        itemAmount.setText(formattedAmount);
        itemDate.setText(formattedDate);
        itemDescription.setText(transaction.description);
        // If category_id is 0, it is not set, so display the no_category string. Otherwise, display the category title
        itemCategory.setText((transaction.category_id < 1 ? convertView.getResources().getString(R.string.no_category) : transaction.category_title));

        // Conditional formatting - change from negative-appearing item to positive appearance
        if (transaction.amount > 0) {
            // Change bg color
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGreen));
            // Change icon imageview
            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.item_transaction_img);
            imgIcon.setImageResource(R.drawable.ic_up_black);
        } else {
            // Change bg color
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintRed));
            // Change icon imageview
            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.item_transaction_img);
            imgIcon.setImageResource(R.drawable.ic_down_black);
        }

        return convertView;
    }
}
