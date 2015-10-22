package com.kd8bny.maintenanceman.ui.intro;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;


public class activity_intro extends AppIntro {
    private static final String TAG = "actvty_intro";

    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(new slide_mm());
        addSlide(new slide_add());
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