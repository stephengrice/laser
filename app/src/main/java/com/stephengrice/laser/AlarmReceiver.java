package com.stephengrice.laser;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String ARG_ST_ID = "scheduledTransactionId";

    @Override
    public void onReceive(Context context, Intent intent) {
        long st_id = intent.getExtras().getLong(ARG_ST_ID);
        DbContract.ScheduledTransaction scheduledTransaction = DbHelper.getScheduledTransaction(context, st_id);

        long currentTime = System.currentTimeMillis();
        // Add a transaction instance to match this scheduled transaction
        DbContract.Transaction transaction = new DbContract.Transaction();
        transaction.amount = scheduledTransaction.amount;
        transaction.description = scheduledTransaction.description;
        transaction.date = currentTime;
        transaction.category_id = scheduledTransaction.category_id;
        DbHelper.insertTransaction(context, transaction);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));

        // Update ScheduledTransaction object in DB
        scheduledTransaction.date = calendar.getTimeInMillis();
        DbHelper.updateScheduledTransaction(context, scheduledTransaction);

        switch(scheduledTransaction.repeat) {
            default:
            case NO_REPEAT:
                return;
            case DAILY:
                calendar.add(Calendar.DATE, 1);
                break;
            case WEEKLY:
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                break;
            case BI_WEEKLY:
                calendar.add(Calendar.DAY_OF_YEAR, 14);
                break;
            case MONTHLY:
                calendar.add(Calendar.MONTH, 1);
                break;
            case YEARLY:
                calendar.add(Calendar.YEAR, 1);
                break;
        }

        // Set next alarm for this scheduled transaction based on RepeatType
        setAlarm(context, scheduledTransaction);
        Toast.makeText(context, "Transaction added: " + scheduledTransaction.description + "; Next alarm: " + MainActivity.formatDate(context, calendar.getTimeInMillis()), Toast.LENGTH_LONG).show();
    }

    public static void setAlarm(Context context, DbContract.ScheduledTransaction scheduledTransaction){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ARG_ST_ID, scheduledTransaction.id);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, scheduledTransaction.date, sender);
    }
}
