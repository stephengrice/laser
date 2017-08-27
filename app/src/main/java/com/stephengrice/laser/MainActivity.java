package com.stephengrice.laser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;
import com.stephengrice.laser.fragment.ScheduledTransactionAddFragment;
import com.stephengrice.laser.fragment.ScheduledTransactionDetailFragment;
import com.stephengrice.laser.fragment.ScheduledTransactionEditFragment;
import com.stephengrice.laser.fragment.ScheduledTransactionsFragment;
import com.stephengrice.laser.fragment.SettingsFragment;
import com.stephengrice.laser.fragment.TransactionAddFragment;
import com.stephengrice.laser.fragment.BudgetChartFragment;
import com.stephengrice.laser.fragment.BudgetFragment;
import com.stephengrice.laser.fragment.DashFragment;
import com.stephengrice.laser.fragment.GoalsFragment;
import com.stephengrice.laser.fragment.TransactionDetailFragment;
import com.stephengrice.laser.fragment.TransactionEditFragment;
import com.stephengrice.laser.fragment.TransactionsFragment;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DashFragment.OnFragmentInteractionListener,
        BudgetFragment.OnFragmentInteractionListener,
        TransactionsFragment.OnFragmentInteractionListener,
        GoalsFragment.OnFragmentInteractionListener,
        TransactionAddFragment.OnFragmentInteractionListener,
        BudgetChartFragment.OnFragmentInteractionListener,
        TransactionDetailFragment.OnFragmentInteractionListener,
        TransactionEditFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        ScheduledTransactionsFragment.OnFragmentInteractionListener,
        ScheduledTransactionAddFragment.OnFragmentInteractionListener,
        ScheduledTransactionDetailFragment.OnFragmentInteractionListener,
        ScheduledTransactionEditFragment.OnFragmentInteractionListener {

    public static final int SNACKBAR_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showIntro();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                updateNavBalance();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Replace content_main with dash fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, new DashFragment())
                .commit();
        // Select the dashboard menu item to start
        navigationView.getMenu().getItem(0).setChecked(true);

        // Set up back press for fragments
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        // Update UI
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
                        if (currentFragment instanceof DashFragment) {
                            navigationView.getMenu().getItem(0).setChecked(true);
                        } else if (currentFragment instanceof BudgetFragment) {
                            navigationView.getMenu().getItem(1).setChecked(true);
                        } else if (currentFragment instanceof TransactionsFragment) {
                            navigationView.getMenu().getItem(2).setChecked(true);
                        } else if (currentFragment instanceof ScheduledTransactionsFragment) {
                            navigationView.getMenu().getItem(3).setChecked(true);
                        } else if (currentFragment instanceof GoalsFragment) {
                            navigationView.getMenu().getItem(4).setChecked(true);
                        } else if (currentFragment instanceof SettingsFragment) {
                            navigationView.getMenu().getItem(5).setChecked(true);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent i = new Intent(this, SettingsActivity.class);
//            startActivity(i);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_dash) {
            fragment = new DashFragment();
        } else if (id == R.id.nav_budget) {
            fragment = new BudgetFragment();
        } else if (id == R.id.nav_transactions) {
            fragment = new TransactionsFragment();
        } else if (id == R.id.nav_goals) {
            fragment = new GoalsFragment();
//        } else if (id == R.id.nav_add) {
//            fragment = new TransactionAddFragment();
        } else if (id == R.id.nav_scheduled_transactions) {
            fragment = new ScheduledTransactionsFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showIntro() {
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }

    public static String formatDate(Context context, long date) {
        return DateUtils.getRelativeDateTimeString(context, date, 0, DateUtils.WEEK_IN_MILLIS, 0).toString();
    }

    public static String formatCurrency(float currency) {
        return new DecimalFormat("$#,##0.00").format(currency);
    }

    public static String formatPercent(float percent) {
        return new DecimalFormat("0.0'%'").format(percent);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }

    public void updateNavBalance() {
        // Update balance in nav tray
        TextView txtNavBalance = (TextView) findViewById(R.id.txt_nav_balance);
        txtNavBalance.setText(formatCurrency(DbHelper.getBalance(MainActivity.this)));
    }

    public static String getRepeatText(Context context, int repeat) {
        return context.getResources().getStringArray(R.array.spinner_repeat)[repeat];
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void setAlarm(Context context, DbContract.ScheduledTransaction scheduledTransaction) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        // TODO detect and change repeat time
//        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
//        Log.d("mytag", "alarm set");
//        Log.d("mytag", new Date(alarmManager.getNextAlarmClock().getTriggerTime()).toString());
//    }
}
