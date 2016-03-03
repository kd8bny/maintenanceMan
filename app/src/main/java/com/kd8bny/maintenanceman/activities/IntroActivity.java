package com.kd8bny.maintenanceman.activities;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.kd8bny.maintenanceman.fragments.intro.slide_add;
import com.kd8bny.maintenanceman.fragments.intro.slide_dbx;
import com.kd8bny.maintenanceman.fragments.intro.slide_mm;
import com.kd8bny.maintenanceman.fragments.intro.slide_spec;
import com.kd8bny.maintenanceman.fragments.intro.slide_travel;


public class IntroActivity extends AppIntro {
    private static final String TAG = "actvty_intro";

    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(new slide_mm());
        addSlide(new slide_add());
        addSlide(new slide_travel());
        addSlide(new slide_spec());
        addSlide(new slide_dbx());

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);
    }

    @Override
    public void onSkipPressed() {
        this.finish();
    }

    @Override
    public void onDonePressed() {
        this.finish();
    }
}