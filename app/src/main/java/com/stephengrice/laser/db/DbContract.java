package com.stephengrice.laser.db;

import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import java.io.Serializable;

public final class DbContract {
    private DbContract() {}

    public static final int DATABASE_VERSION = 3;
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
        public static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME
                + " LEFT JOIN categories on " + COLUMN_NAME_CATEGORY_ID + "=" + Category.TABLE_NAME + "." + Category._ID + " "
                + " ORDER BY date DESC";
        public static final String sqlSelectByCategoryId(int category_id) {
            return "SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_NAME_CATEGORY_ID+"=" + category_id;
        }

        // Hopefully this double use won't cause problems: also allow this class to hold a Transaction object from DB
        public float amount;
        public long date;
        public String description;
        public int category_id;
        public String category_title;
        public Transaction(float amount, long date, String description, int category_id, String category_title) {
            this.amount = amount;
            this.date = date;
            this.description = description;
            this.category_id = category_id;
            this.category_title = category_title;
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

    }
}
