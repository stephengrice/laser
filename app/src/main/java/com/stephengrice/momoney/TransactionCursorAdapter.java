package com.stephengrice.momoney;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stephengrice.momoney.db.MoMoneyContract;

import java.util.Date;

public class TransactionCursorAdapter extends CursorAdapter {

    float mAmount;

    public TransactionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mAmount = cursor.getFloat(cursor.getColumnIndexOrThrow(MoMoneyContract.Transaction.COLUMN_NAME_AMOUNT));
        if (mAmount > 0) {
            return LayoutInflater.from(context).inflate(R.layout.item_transaction_positive, parent, false);
        } else {
            return LayoutInflater.from(context).inflate(R.layout.item_transaction_negative, parent, false);
        }
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
        float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(MoMoneyContract.Transaction.COLUMN_NAME_AMOUNT));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(MoMoneyContract.Transaction.COLUMN_NAME_DATE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(MoMoneyContract.Transaction.COLUMN_NAME_DESCRIPTION));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(MoMoneyContract.Transaction.COLUMN_NAME_CATEGORY));
        // Fill data
        itemAmount.setText(Float.toString(Math.abs(amount)));
        itemDate.setText(new Date(date).toString());
        itemDescription.setText(description);
        itemCategory.setText(category);
    }
}
