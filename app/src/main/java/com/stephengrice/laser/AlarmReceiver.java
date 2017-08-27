package com.stephengrice.laser;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static int tempNumExec = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received: " + tempNumExec, Toast.LENGTH_SHORT).show();
        tempNumExec++;
        Log.d("mytag", "alarm triggered");
        setAlarm(context, intent.getIntExtra("test", 0));
    }

    public void setAlarm(Context context, long date){
        Log.d("mytag","alarm set");

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.SECOND, 5);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("test", date);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }
}
