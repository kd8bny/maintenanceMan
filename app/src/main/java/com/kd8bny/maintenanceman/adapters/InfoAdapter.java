package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adptr_inf";

    private Context mContext;
    private ArrayList<HashMap> mVehicleInfoArray = new ArrayList<>();
    private ArrayList<Integer> viewTypes = new ArrayList<>();
    private HashMap<String, String> cardInfo = new HashMap<>();
    private Vehicle mVehicle;
    private ArrayList<Maintenance> mCostHist;
    private ArrayList<Mileage> mMileages;
    private String UNIT_MILEAGE;

    private int recycler_i = 0;

    private static final int VIEW_SPECS_GEN = 0;
    private static final int VIEW_CURRENT = 1;
    private static final int VIEW_SPECS_ENG = 2;
    private static final int VIEW_SPECS_POWER = 3;
    private static final int VIEW_SPECS_OTHER = 4;

    protected View vSpecs;
    protected View vCurrent;

    public InfoAdapter(Context context, Vehicle vehicle, ArrayList<Maintenance> costHist, ArrayList<Mileage> mileages) {
        mContext = context;
        mVehicle = vehicle;
        mCostHist = costHist;
        mMileages = mileages;
        UNIT_MILEAGE = vehicle.getUnitMileage();

        HashMap<String, String> tempSpecs = mVehicle.getGeneralSpecs();
        if (!tempSpecs.isEmpty()) {
            mVehicleInfoArray.add(tempSpecs);
            viewTypes.add(VIEW_SPECS_GEN);
        }
        if (!mCostHist.isEmpty() || !mMileages.isEmpty()) {
            HashMap<String, String> tempCurrent = new HashMap<>();
            tempCurrent.put("current", "current");
            mVehicleInfoArray.add(tempCurrent);
            viewTypes.add(VIEW_CURRENT);
        }
        tempSpecs = mVehicle.getEngineSpecs();
        if (!tempSpecs.isEmpty()) {
            mVehicleInfoArray.add(tempSpecs);
            viewTypes.add(VIEW_SPECS_ENG);
        }
        tempSpecs = mVehicle.getPowerTrainSpecs();
        if (!tempSpecs.isEmpty()) {
            mVehicleInfoArray.add(tempSpecs);
            viewTypes.add(VIEW_SPECS_POWER);
        }
        tempSpecs = mVehicle.getOtherSpecs();
        if (!tempSpecs.isEmpty()) {
            mVehicleInfoArray.add(tempSpecs);
            viewTypes.add(VIEW_SPECS_OTHER);
        }
    }

    @Override
    public int getItemCount() {
        return mVehicleInfoArray.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (viewTypes.get(recycler_i)){
            case VIEW_CURRENT:
                vCurrent = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info_current, viewGroup, false);
                recycler_i++;

               return new vhCurrent(vCurrent, -1, null, null, "");

            default:
                vSpecs = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();
                recycler_i++;

                   return new vhSpecs(vSpecs, -1, null, -1, null);
           }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        cardInfo = mVehicleInfoArray.get(i);
        switch (viewTypes.get(i)){
            case VIEW_CURRENT:
                new vhCurrent(vCurrent, i, mCostHist, mMileages, UNIT_MILEAGE);
                break;

            default :
                new vhSpecs(vSpecs, i, cardInfo, viewTypes.get(i), mVehicle.getTitle());
                break;
        }
    }
}

@SuppressWarnings("ResourceType")
class vhSpecs extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_specs";

    private TypedArray headerColors;

    public vhSpecs(View view, int i, final HashMap<String, String> cardInfo, int viewType, String vehicleTitle) {
        super(view);
        View relLayout = view.findViewById(R.id.card_info_rel);
        View linLayout = relLayout.findViewById(R.id.card_info_lin);

        DisplayMetrics metrics = linLayout.getContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        float fieldsp = view.getResources().getDimension(R.dimen.field_font);
        int fieldSize = (int) (fieldsp/metrics.density + 0.5f);

        LinearLayout.LayoutParams textViewItemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        if(i > -1) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);
            TextView vHeader = (TextView) view.findViewById(R.id.card_info_title);
            if (viewType == 0) {
                vHeader.setText(vehicleTitle);
            }else {
                vHeader.setText(getTitle(viewType));
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.card_info_iv);
            imageView.setBackgroundColor(headerColors.getColor(i, 0));

            for (String key : cardInfo.keySet()) {
                if(!cardInfo.get(key).isEmpty() && !key.equals("type")) {
                    LinearLayout tempLinLay = new LinearLayout(view.getContext());
                    TextView vTitle = new TextView(view.getContext());
                    TextView vItem = new TextView(view.getContext());
                    tempLinLay.setOrientation(LinearLayout.HORIZONTAL);

                    vTitle.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    vItem.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    vTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    vTitle.setTextColor(ContextCompat.getColor(view.getContext(), R.color.secondary_text));
                    vTitle.setMinWidth(width/3);
                    vTitle.setMaxWidth(width/2);
                    vItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    vItem.setTextColor(ContextCompat.getColor(view.getContext(), R.color.secondary_text));

                    vTitle.setText(String.format("%s: ", key));
                    vItem.setText(cardInfo.get(key));

                    tempLinLay.addView(vTitle);
                    tempLinLay.addView(vItem);
                    ((LinearLayout) linLayout).addView(tempLinLay, textViewItemParams);
                }
            }
        }
    }

    private int getTitle(int viewType){
        switch (viewType){
            case 2:
                return R.string.header_engine;
            case 3:
                return R.string.header_power_train;
            case 4:
                return R.string.header_other;
            default:
                return -1;
        }
    }
}

@SuppressWarnings("ResourceType")
class vhCurrent extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_crnt";

    private TypedArray headerColors;

    public vhCurrent(View view, int i, ArrayList<Maintenance> maintenanceList, ArrayList<Mileage> mileageList, String unitMileage) {
        super(view);

        if(i > -1) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);
            TextView vHeader = (TextView) view.findViewById(R.id.card_info_title);
            vHeader.setText(view.getResources().getString(R.string.header_current));
            ImageView imageView = (ImageView) view.findViewById(R.id.card_info_iv);
            imageView.setBackgroundColor(headerColors.getColor(i, 0));

            if (!maintenanceList.isEmpty()){
                Double total = 0.0;
                for (Maintenance m: maintenanceList) {
                    String price = m.getPrice();
                    if (price != null) {
                        if (!price.isEmpty()) {
                            total += Double.parseDouble(price);
                        }
                    }
                }

                TextView vVal = (TextView) view.findViewById(R.id.value_cost);
                TextView vInfo = (TextView) view.findViewById(R.id.info_cost);
                vVal.setText(String.format(Locale.ENGLISH, "%1$,.2f", total));
                vInfo.setText(view.getResources().getString(R.string.hint_info_cost));
            }

            if (!mileageList.isEmpty()){
                Double mileage = 0.0;
                Double cost = 0.0;
                for (Mileage m: mileageList) {
                    mileage += m.getMileage();
                    cost += m.getPrice();
                }
                mileage = mileage/mileageList.size();
                cost = cost/mileageList.size();

                TextView vMileageVal = (TextView) view.findViewById(R.id.value_mileage);
                TextView vMileageInfo = (TextView) view.findViewById(R.id.info_mileage);
                vMileageVal.setText(String.format(Locale.ENGLISH, "%1$,.2f", mileage));
                vMileageInfo.setText(unitMileage);

                TextView vCostVal = (TextView) view.findViewById(R.id.value_mileage_cost);
                TextView vCostInfo = (TextView) view.findViewById(R.id.info_mileage_cost);
                vCostVal.setText(String.format(Locale.ENGLISH, "%1$,.2f", cost));
                vCostInfo.setText(view.getResources().getString(R.string.hint_info_mileage_cost));
            }
        }
    }
}
