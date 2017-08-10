package com.stephengrice.momoney.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

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

}
