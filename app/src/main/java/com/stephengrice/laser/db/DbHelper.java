package com.stephengrice.laser.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stephengrice.laser.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    public enum CountMode {
        ALL, POSITIVE, NEGATIVE
    }
    public enum TimeFrame {
        DAY, WEEK, MONTH, YEAR, ALL
    }

    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.Transaction.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Category.SQL_CREATE_TABLE);
        db.execSQL(DbContract.ScheduledTransaction.SQL_CREATE_TABLE);
        db.execSQL(DbContract.Goal.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.Transaction.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Category.SQL_DELETE_TABLE);
        db.execSQL(DbContract.ScheduledTransaction.SQL_DELETE_TABLE);
        db.execSQL(DbContract.Goal.SQL_DELETE_TABLE);
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
        db.close();

        return balance;
    }

    // Requires left join in Transaction.SQL_SELECT_ALL
    public static ArrayList<DbContract.Transaction> getTransactions(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.Transaction.SQL_SELECT_ALL, null);

        ArrayList<DbContract.Transaction> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            DbContract.Transaction current = new DbContract.Transaction(cursor);
            result.add(current);
        }
        cursor.close();
        db.close();

        return result;
    }

    public static long insertTransaction(Context context, DbContract.Transaction transaction) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(DbContract.Transaction.TABLE_NAME, null, transaction.getContentValues());
        db.close();
        return newRowId;
    }

    public static ArrayList<DbContract.Category> getCategories(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.Category.SQL_SELECT_ALL, null);

        ArrayList<DbContract.Category> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            DbContract.Category current = new DbContract.Category(cursor);
            result.add(current);
        }
        cursor.close();
        db.close();

        return result;
    }

    public static ArrayList<DbContract.ScheduledTransaction> getScheduledTransactions(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.ScheduledTransaction.SQL_SELECT_ALL, null);

        ArrayList<DbContract.ScheduledTransaction> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            DbContract.ScheduledTransaction current = new DbContract.ScheduledTransaction(cursor);
            result.add(current);
        }
        cursor.close();
        db.close();

        return result;
    }

    public static long insertScheduledTransaction(Context context, DbContract.ScheduledTransaction st) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(DbContract.ScheduledTransaction.TABLE_NAME, null, st.getContentValues());
        db.close();
        return newRowId;
    }

    public static long updateScheduledTransaction(Context context, DbContract.ScheduledTransaction st) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.update(
                DbContract.ScheduledTransaction.TABLE_NAME,
                st.getContentValues(),
                DbContract.Transaction._ID + " = ?",
                new String[] { Long.toString(st.id) }
        );
        db.close();
        return result;
    }


    public static HashMap<String, Float> countByCategory(Context context, CountMode mode, TimeFrame timeFrame) {
        // Create result object (to be returned)
        HashMap<String, Float> result = new HashMap<String, Float>();
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

            // TODO make this a lot more efficient (I am ashamed
            // Discard elements outside of given timeframe
            Calendar calendar = Calendar.getInstance();
//            Log.d("mytag", "Comparing times: " + transaction.date + " to " + calendar.getTimeInMillis());
            switch (timeFrame) {
                case DAY:
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    if (transaction.date < calendar.getTimeInMillis()) {
                        Log.d("mytag", "Skipping because not within last day: " + transaction.description);
                        continue;
                    }
                    break;
                case WEEK:
                    calendar.add(Calendar.DAY_OF_YEAR, -7);
                    if (transaction.date < calendar.getTimeInMillis()) {
                        Log.d("mytag", "Skipping because not within last week: " + transaction.description);
                        continue;
                    }
                    break;
                case MONTH:
                    calendar.add(Calendar.MONTH, -1);
                    if (transaction.date < calendar.getTimeInMillis()){
                        Log.d("mytag", "Skipping because not within last month: " + transaction.description);
                        continue;
                    }
                    break;
                case YEAR:
                    calendar.add(Calendar.YEAR, -1);
                    if (transaction.date < calendar.getTimeInMillis()){
                        Log.d("mytag", "Skipping because not within last year: " + transaction.description);
                        continue;
                    }
                    break;
                default:
                case ALL:
                    break;
            }

            // Replace no_category entries with proper string resource
            if (transaction.category_title == null || transaction.category_title.length() < 1) {
                transaction.category_title = context.getResources().getString(R.string.no_category);
            }

            // Initialize or increment the key for this transaction's title
            if (!result.containsKey(transaction.category_title)) {
                result.put(transaction.category_title, transaction.amount);
            } else {
                result.put(transaction.category_title, result.get(transaction.category_title) + transaction.amount);
            }
        }

        Log.d("mytag", "Returning " + result.size() + " rows");
        return result;
    }

    /**
     * Pass in a category title
     * IF category exists, get the id for it
     * ELSE create a new category and return its id
     * @param title Category title (case insensitive)
     * @return id of category row with given title
     */
    public static long getCategoryId(Context context, String title) {
        // Don't store a blank category
        if (title.length() < 1) {
            return 0;
        }

        // Determine whether this category exists
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.Category.sqlSelectByTitle(title), null);

        if (cursor.moveToFirst()) {
            long rowId = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Category._ID));
            cursor.close();
            return rowId;
        } else {
            // Create new row
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(DbContract.Category.COLUMN_NAME_TITLE, title);
            long newRow = db.insert(DbContract.Category.TABLE_NAME, null, values);
            db.close();
            return newRow;
        }
    }

    /**
     *
     * @param context
     * @param transaction
     * @return number of rows affected
     */
    public static int updateTransaction(Context context, DbContract.Transaction transaction) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.update(
                DbContract.Transaction.TABLE_NAME,
                transaction.getContentValues(),
                DbContract.Transaction._ID + " = ?",
                new String[] { Long.toString(transaction.id) }
        );
        db.close();
        return result;
    }

    public static DbContract.ScheduledTransaction getScheduledTransaction(Context context, long search_id) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(DbContract.ScheduledTransaction.sqlSelectOne(search_id), null);
        cursor.moveToFirst();
        DbContract.ScheduledTransaction result = new DbContract.ScheduledTransaction(cursor);

        cursor.close();
        db.close();

        return result;
    }

    public static void deleteScheduledTransaction(Context context, DbContract.ScheduledTransaction scheduledTransaction) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(
                DbContract.ScheduledTransaction.TABLE_NAME,
                DbContract.ScheduledTransaction._ID + "=?",
                new String[]{ Long.toString(scheduledTransaction.id) }
        );
        db.close();
    }

    public static void deleteTransaction(Context context, DbContract.Transaction transaction) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(
                DbContract.Transaction.TABLE_NAME,
                DbContract.Transaction._ID + "=?",
                new String[]{ Long.toString(transaction.id) }
        );
        db.close();
    }

    public static long countTransactions(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = DatabaseUtils.queryNumEntries(
                db,
                DbContract.Transaction.TABLE_NAME
        );
        db.close();
        return result;
    }
    public static long countScheduledTransactions(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = DatabaseUtils.queryNumEntries(
                db,
                DbContract.ScheduledTransaction.TABLE_NAME
        );
        db.close();
        return result;
    }
}
