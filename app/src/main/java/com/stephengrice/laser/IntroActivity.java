package com.stephengrice.laser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(AppIntroFragment.newInstance("Welcome to Laser", "Laser is the Precision Budgeting app that allows you to quickly and easily track your personal cash flow.", R.drawable.ic_add_24dp, ContextCompat.getColor(this, R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Add transactions", "Transactions are the core of Laser. Every time you spend or earn money, track it easily by adding a Transaction.", R.drawable.example_appwidget_preview, ContextCompat.getColor(this, R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Schedule transactions", "Tired of adding that paycheck every week? How about your bills? Add them all as Scheduled Transactions, and they'll be tracked automatically, whether they occur daily, weekly, bi-weekly, or even monthly.", R.drawable.ic_arrow_back_white, ContextCompat.getColor(this, R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Track income and expenses", "Spending is fun, and so is saving! See the best of both worlds with your Earned and Spent charts, found under the My Budget tab.", R.drawable.ic_money_24dp, ContextCompat.getColor(this, R.color.colorPrimary)));

//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance(title, description, image, backgroundColor));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
//
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        onDonePressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}