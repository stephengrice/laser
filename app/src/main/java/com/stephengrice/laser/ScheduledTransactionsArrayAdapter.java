package com.stephengrice.laser;


import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.stephengrice.laser.db.DbContract;

import java.util.ArrayList;

public class ScheduledTransactionsArrayAdapter extends ArrayAdapter<DbContract.ScheduledTransaction> {

    private static int mLayout = R.layout.item_scheduled_transaction;

    public ScheduledTransactionsArrayAdapter(Context context, ArrayList<DbContract.ScheduledTransaction> data) {
        super(context, mLayout, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
