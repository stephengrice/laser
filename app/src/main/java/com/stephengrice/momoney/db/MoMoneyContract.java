package com.stephengrice.momoney.db;

import android.provider.BaseColumns;

public final class MoMoneyContract {
    private MoMoneyContract() {}

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoMoney.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class Transaction implements BaseColumns {
        public static final String TABLE_NAME = "transactions";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CATEGORY = "category";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
                ")";

        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    }
    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
