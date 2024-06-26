package com.stephengrice.laser.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import com.stephengrice.laser.RepeatType;

import java.io.Serializable;

public final class DbContract {
    private DbContract() {}

    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "Laser.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class Transaction implements BaseColumns, Serializable {
        public static final String TABLE_NAME = "transactions";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_CATEGORY_ID + ") REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ")" +
                ")";

        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String SQL_SELECT_ALL = "SELECT "+TABLE_NAME+".*, "+Category.TABLE_NAME+".title FROM " + TABLE_NAME
                + " LEFT JOIN categories on " + COLUMN_NAME_CATEGORY_ID + "=" + Category.TABLE_NAME + "." + Category._ID + " "
                + " ORDER BY date DESC";
        public static final String sqlSelectByCategoryId(int category_id) {
            return "SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_NAME_CATEGORY_ID+"=" + category_id;
        }

        // Hopefully this double use won't cause problems: also allow this class to hold a Transaction object from DB
        public long id;
        public float amount;
        public long date;
        public String description;
        public long category_id;
        public String category_title;

        public Transaction(Cursor cursor) {
            this.id = cursor.getLong(cursor.getColumnIndexOrThrow(Transaction._ID));
            this.amount = cursor.getFloat(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_AMOUNT));
            this.date = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DATE));
            this.description = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DESCRIPTION));
            this.category_id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_CATEGORY_ID));
            this.category_title = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Category.COLUMN_NAME_TITLE));
        }

        public Transaction() {

        }

        public ContentValues getContentValues() {
            ContentValues values = new ContentValues();
            if (this.id > 0) {
                values.put(_ID, this.id);
            }
            values.put(COLUMN_NAME_AMOUNT, amount);
            values.put(COLUMN_NAME_DATE, date);
            values.put(COLUMN_NAME_DESCRIPTION, description);
            if (this.category_id < 1) {
                values.putNull(COLUMN_NAME_CATEGORY_ID);
            } else {
                values.put(COLUMN_NAME_CATEGORY_ID, category_id);
            }
            return values;
        }
    }
    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_TITLE = "title";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_TITLE + TEXT_TYPE + " UNIQUE" +
                ")";
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY title DESC";

        public static final String sqlSelectByTitle(String title) {
            return "SELECT * FROM " + TABLE_NAME + " WHERE title=" + DatabaseUtils.sqlEscapeString(title);
        }

        public long id;
        public String title;
        public Category(Cursor cursor) {
            this.id = cursor.getLong(cursor.getColumnIndexOrThrow(Category._ID));
            this.title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE));
        }
    }

    public static class ScheduledTransaction implements BaseColumns, Serializable {
        public static final String TABLE_NAME = "scheduled_transactions";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
        public static final String COLUMN_NAME_REPEAT = "repeat";
        public static final String COLUMN_NAME_ENABLED = "enabled";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_REPEAT + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_ENABLED + INTEGER_TYPE + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_CATEGORY_ID + ") REFERENCES " + Category.TABLE_NAME + "(" + Category._ID + ")" +
                ")";
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String SQL_SELECT_ALL = "SELECT "+TABLE_NAME+".*, "+Category.TABLE_NAME+".title FROM " + TABLE_NAME
                + " LEFT JOIN categories on " + COLUMN_NAME_CATEGORY_ID + "=" + Category.TABLE_NAME + "." + Category._ID + " "
                + " ORDER BY date DESC";
        public static final String sqlSelectOne(long arg_id) {
            return "SELECT "+TABLE_NAME+".*, "+Category.TABLE_NAME+".title FROM " + TABLE_NAME
                    + " LEFT JOIN categories on " + COLUMN_NAME_CATEGORY_ID + "=" + Category.TABLE_NAME + "." + Category._ID + " "
                    + " WHERE "+TABLE_NAME+"."+_ID+"=" + arg_id;
        }

        public long id;
        public float amount;
        public long date;
        public String description;
        public long category_id;
        public String category_title;
        public RepeatType repeat;
        public boolean enabled;

        public ScheduledTransaction(Cursor cursor) {
            this.id = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
            this.amount = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT));
            this.date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE));
            this.description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DESCRIPTION));
            this.category_id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_CATEGORY_ID));
            this.category_title = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Category.COLUMN_NAME_TITLE));
            this.repeat = RepeatType.fromInt(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_REPEAT)));
            this.enabled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_ENABLED)) == 1;
        }

        public ScheduledTransaction() {

        }

        public ContentValues getContentValues() {
            ContentValues values = new ContentValues();
            if (this.id > 0) {
                values.put(_ID, this.id);
            }
            values.put(COLUMN_NAME_AMOUNT, amount);
            values.put(COLUMN_NAME_DATE, date);
            values.put(COLUMN_NAME_DESCRIPTION, description);
            if (this.category_id < 1) {
                values.putNull(COLUMN_NAME_CATEGORY_ID);
            } else {
                values.put(COLUMN_NAME_CATEGORY_ID, category_id);
            }
            values.put(COLUMN_NAME_REPEAT, repeat.getValue());
            values.put(COLUMN_NAME_ENABLED, (enabled? 1 : 0));
            return values;
        }
    }

    public static class Goal implements BaseColumns {
        public static final String TABLE_NAME = "goals";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        // Later: A "repeat" field, maybe a category

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_TITLE + TEXT_TYPE +
                COLUMN_NAME_DUE_DATE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_TYPE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_AMOUNT + REAL_TYPE +
                ")";
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
