package com.stephengrice.laser;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

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

                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent);
                mActivity.finish();
            }
        });
    }
}
