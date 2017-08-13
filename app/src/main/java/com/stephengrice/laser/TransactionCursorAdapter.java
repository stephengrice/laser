package com.stephengrice.laser;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stephengrice.laser.db.DbContract;

import java.text.DecimalFormat;
import java.util.Date;

public class TransactionCursorAdapter extends CursorAdapter {

    private float mAmount;

    public TransactionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
    }

    // Bind all data, including set text
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find views
        TextView itemAmount = (TextView) view.findViewById(R.id.item_transaction_amount);
        TextView itemDate = (TextView) view.findViewById(R.id.item_transaction_date);
        TextView itemDescription = (TextView) view.findViewById(R.id.item_transaction_description);
        TextView itemCategory = (TextView) view.findViewById(R.id.item_transaction_category);
        // Extract data from cursor
        mAmount = cursor.getFloat(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_AMOUNT));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DATE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DESCRIPTION));
        long category_id = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_CATEGORY_ID));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Category.COLUMN_NAME_TITLE));

        String formattedAmount = new DecimalFormat("$#,###.00").format(Math.abs(mAmount));
        String formattedDate = DateUtils.getRelativeDateTimeString(context, date, 0, DateUtils.WEEK_IN_MILLIS, 0).toString();
        // Fill data
        itemAmount.setText(formattedAmount);
        itemDate.setText(formattedDate);
        itemDescription.setText(description);
        // If category_id is 0, it is not set, so display the no_category string. Otherwise, display the category title
        itemCategory.setText((category_id < 1 ? view.getResources().getString(R.string.no_category) : category));

        // Conditional formatting - change from negative-appearing item to positive appearance
        if (mAmount > 0) {
            // Change bg color
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTintGreen));
            // Change icon imageview
            ImageView imgIcon = (ImageView)view.findViewById(R.id.item_transaction_img);
            imgIcon.setImageResource(R.drawable.ic_up_black);
        } else {
            // Change bg color
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTintRed));
            // Change icon imageview
            ImageView imgIcon = (ImageView)view.findViewById(R.id.item_transaction_img);
            imgIcon.setImageResource(R.drawable.ic_down_black);
        }
    }
}
