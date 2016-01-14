package com.kd8bny.maintenanceman.ui.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kd8bny.maintenanceman.R;


public class activity_info extends AppCompatActivity {
    private static final String TAG = "act_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //setContentView(R.layout.activity_main);
        /*FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer_main);

        if (fragment == null){
            fragment = new fragment_info();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer_main, fragment)
                    .commit();
        }

        /*Bundle bundle = getIntent().getBundleExtra("bundle");
        ArrayList<Vehicle> roster = bundle.getParcelableArrayList("roster");
        int vehiclePos = bundle.getInt("pos", -1);

        Vehicle vehicle = roster.get(vehiclePos);
        Log.v(TAG, vehicle.toString());

        Log.d(TAG, vehicle.getReservedSpecs().toString());
        Log.d(TAG, "create" + vehicle.getGeneralSpecs().toString() + vehicle.getEngineSpecs().toString() + vehicle.getPowerTrainSpecs().toString() + vehicle.getOtherSpecs() + "");

*/


        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ViewPager vpPager = (ViewPager) findViewById(R.id.view_pager);
        adapter_viewPager_info adapter = new adapter_viewPager_info(getSupportFragmentManager(), getApplicationContext());
        vpPager.setAdapter(adapter);
    }
}
