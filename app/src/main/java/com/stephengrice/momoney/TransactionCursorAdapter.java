package com.stephengrice.momoney;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stephengrice.momoney.db.DbContract;

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
        // Fill data
        String formattedAmount = new DecimalFormat("$0.00").format(Math.abs(mAmount));
        itemAmount.setText(formattedAmount);
        itemDate.setText(new Date(date).toString());
        itemDescription.setText(description);
        itemCategory.setText(Long.toString(category_id) + " category: " + category);

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
