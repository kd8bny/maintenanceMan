package com.kd8bny.maintenanceman.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Locale;


public class dialog_mileageHistory extends DialogFragment {
    private static final String TAG = "dlg_mil";

    private ArrayList<Mileage> mMileages;
    private Vehicle mVehicle;
    private Mileage mMileage;

    private Double mAvgMileage, mAvgCost;

    public dialog_mileageHistory(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TOOD This whole fragment is crap and I feel bad...but it gets the job done till I can rewrite it

        Bundle bundle = getArguments();
        mVehicle = bundle.getParcelable("vehicle");
        mMileage = (Mileage) bundle.getSerializable("event");
        mMileages = (ArrayList<Mileage>) bundle.getSerializable("eventList");

        if (!mMileages.isEmpty()) {
            Double mileage = 0.0;
            Double cost = 0.0;
            for (Mileage m : mMileages) {
                mileage += m.getMileage();
                cost += m.getPrice();
            }
            mAvgMileage = mileage / mMileages.size();
            mAvgCost = cost / mMileages.size();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_mileage_history, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        String unitVol = "gal", relation = "above your average XD";
        if (!mVehicle.getUnitDist().equals("mi")){
            unitVol = "L";
        }
        if (mMileage.getMileage() < mAvgMileage){
            relation = "below your average";
        }

        ((TextView) view.findViewById(R.id.mileage)).setText(String.format(Locale.ENGLISH,
                "%1$.2f %2$s", mMileage.getMileage(), mVehicle.getUnitMileage()));
        ((TextView) view.findViewById(R.id.avg)).setText(String.format(Locale.ENGLISH,
                "This is %1$s (%2$.2f %3$s)", relation, mAvgMileage, mVehicle.getUnitMileage()));
        ((TextView) view.findViewById(R.id.date)).setText(String.format(Locale.ENGLISH,
                "Filled up on %1$s @ $%2$s /%3$s", mMileage.getDate(), mMileage.getPrice(), unitVol));
        ((TextView) view.findViewById(R.id.fill_vol)).setText(String.format(Locale.ENGLISH,
                "Traveled %1$.1f %2$s and used %3$.2f %4$s", mMileage.getTripometer(), mVehicle.getUnitDist(), mMileage.getFillVol(), unitVol));

        return view;
    }
}