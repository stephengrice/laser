package com.stephengrice.laser;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stephengrice.laser.db.DbContract;

public class CategoryCursorAdapter extends CursorAdapter {

    public CategoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.select_dialog_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView v = (TextView) view;
        v.setText(getTitle(cursor));
    }

    @Override
    public String convertToString(Cursor cursor) {
        return getTitle(cursor);
    }

    private String getTitle(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Category.COLUMN_NAME_TITLE));
    }


}
