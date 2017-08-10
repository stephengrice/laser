package com.stephengrice.momoney;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stephengrice.momoney.db.DbContract;
import com.stephengrice.momoney.db.DbHelper;

public class SettingsActivity extends AppCompatActivity {

    private SettingsActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mActivity = this;

        Button btnClearDb = (Button)findViewById(R.id.settings_btn_clear_db);
        btnClearDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbHelper = new DbHelper(mActivity);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Drop tables
                db.delete(DbContract.Transaction.TABLE_NAME,null,null);
                db.delete(DbContract.Category.TABLE_NAME, null, null);
//                // Recreate each
//                Cursor cursor = db.rawQuery(DbContract.Transaction.SQL_CREATE_TABLE, null);
//                cursor.close();
//                cursor = db.rawQuery(DbContract.Category.SQL_CREATE_TABLE, null);
//                cursor.close();
            }
        });
    }
}
