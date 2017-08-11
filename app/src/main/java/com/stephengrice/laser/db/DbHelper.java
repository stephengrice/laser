package com.stephengrice.laser.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stephengrice.laser.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    public enum CountMode {
        ALL, POSITIVE, NEGATIVE
    }

    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.Transaction.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Category.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.Transaction.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Category.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public static float getBalance(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.Transaction.SQL_SELECT_ALL, null);
        float balance = 0;
        while (cursor.moveToNext()) {
            balance += cursor.getFloat(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_AMOUNT));
        }
        cursor.close();

        return balance;
    }

    // Requires left join in Transaction.SQL_SELECT_ALL
    public static ArrayList<DbContract.Transaction> getTransactions(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.Transaction.SQL_SELECT_ALL, null);

        ArrayList<DbContract.Transaction> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            DbContract.Transaction current = new DbContract.Transaction(
                    cursor.getFloat(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_AMOUNT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Category.COLUMN_NAME_TITLE))
            );
            result.add(current);
        }
        cursor.close();

        return result;
    }

    public static HashMap<String, Integer> countCategories(Context context, CountMode mode) {
        // Create result object (to be returned)
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        // Get arraylist of all transactions
        ArrayList<DbContract.Transaction> transactions = getTransactions(context);

        for (int i = 0; i < transactions.size(); i++) {
            DbContract.Transaction transaction = transactions.get(i);

            // Count only categories that fit CountMode criteria
            switch (mode) {
                case POSITIVE:
                    if (transaction.amount <= 0) continue;
                    break;
                case NEGATIVE:
                    if (transaction.amount >= 0) continue;
                    break;
                default:
                case ALL:
                    break;
            }

            // Initialize or increment the key for this transaction's title
            if (!result.containsKey(transaction.category_title)) {
                // Replace no_category entries with proper string resource
                if (transaction.category_title == null || transaction.category_title.length() < 1) {
                    result.put(context.getResources().getString(R.string.no_category), 1);
                } else {
                    result.put(transaction.category_title, 1);
                }
            } else {
                result.put(transaction.category_title, result.get(transaction.category_title) + 1);
            }
        }

        return result;
    }

}
