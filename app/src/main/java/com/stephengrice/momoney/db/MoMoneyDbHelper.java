package com.stephengrice.momoney.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoMoneyDbHelper extends SQLiteOpenHelper {

    public MoMoneyDbHelper(Context context) {
        super(context, MoMoneyContract.DATABASE_NAME, null, MoMoneyContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoMoneyContract.Transaction.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MoMoneyContract.Transaction.SQL_DELETE_TABLE);
        onCreate(db);
    }
}
