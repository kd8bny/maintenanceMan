package com.kd8bny.maintenanceman.ui.info;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kd8bny.maintenanceman.R;

public class activity_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ViewPager vpPager = (ViewPager) findViewById(R.id.view_pager);
        adapter_viewPager_info adapter = new adapter_viewPager_info(getSupportFragmentManager(), getApplicationContext());
        vpPager.setAdapter(adapter);
    }
}
