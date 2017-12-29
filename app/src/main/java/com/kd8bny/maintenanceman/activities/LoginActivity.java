package com.kd8bny.maintenanceman.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.fragments.fragment_main;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "activity_login";

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new fragment_main();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
