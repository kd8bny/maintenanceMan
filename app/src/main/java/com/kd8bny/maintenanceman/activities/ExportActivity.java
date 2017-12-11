package com.kd8bny.maintenanceman.activities;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ExportActivity extends AppCompatActivity {
    private static final String TAG = "exportAll";

    private static final String DIR = "maintenanceman";
    private File dir;

    private ProgressBar progressBar;

    private ArrayList<Vehicle> mRoster = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        mRoster = bundle.getParcelableArrayList("roster");

        Button exportButton = (Button) this.findViewById(R.id.button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportAll();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void exportAll() {
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(getApplicationContext());
        Calendar cal = Calendar.getInstance();
        Gson gson = new Gson();

        String date = String.format("%s_%s_%s",
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
        String fileName = String.format("%s_%s_%s.json", "maintenanceman", "data", date);

        File sdcard = Environment.getExternalStorageDirectory();
        dir = new File(String.format("%s/%s/", sdcard.getAbsoluteFile(), DIR));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dataFile = new File(dir, fileName);

        try {
            FileWriter fileWriter = new FileWriter(dataFile);
            String arrayJSON = gson.toJson(mRoster);
            fileWriter.write(arrayJSON);

            ArrayList<Maintenance> maintenances = new ArrayList<>();
            ArrayList<Mileage> mileages = new ArrayList<>();
            ArrayList<Travel> travels = new ArrayList<>();
            for (Vehicle v : mRoster) {
                maintenances.addAll(vehicleLogDBHelper.getMaintenanceEntries(v.getRefID(), false));
                mileages.addAll(vehicleLogDBHelper.getMileageEntries(v.getRefID(), false));
                travels.addAll(vehicleLogDBHelper.getFullTravelEntries(v.getRefID(), false));
            }

            arrayJSON = gson.toJson(maintenances);
            fileWriter.write(arrayJSON);

            arrayJSON = gson.toJson(mileages);
            fileWriter.write(arrayJSON);

            arrayJSON = gson.toJson(travels);
            fileWriter.write(arrayJSON);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}