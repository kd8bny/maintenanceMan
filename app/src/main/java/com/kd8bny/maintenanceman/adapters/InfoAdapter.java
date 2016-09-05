package com.kd8bny.maintenanceman.adapters;

import android.content.res.TypedArray;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adptr_inf";

    private ArrayList<HashMap> mVehicleInfoArray = new ArrayList<>();
    private ArrayList<Integer> viewTypes = new ArrayList<>();
    private HashMap<String, String> cardInfo = new HashMap<>();
    private Vehicle mVehicle;
    private ArrayList<ArrayList> mCostHist;
    private ArrayList<Mileage> mMileages;

    private int stuipidI = 0;

    private static final int VIEW_SPECS_GEN = 0;
    private static final int VIEW_CHART_COST = 1;
    private static final int VIEW_CHART_MILEAGE = 2;
    private static final int VIEW_SPECS_ENG = 3;
    private static final int VIEW_SPECS_POWER = 4;
    private static final int VIEW_SPECS_OTHER = 5;

    protected View vSpecs;
    protected View vChartCost;
    protected View vChartMileage;

    public InfoAdapter(Vehicle vehicle, ArrayList<ArrayList> costHist, ArrayList<Mileage> mileages) {
        mVehicle = vehicle;
        mCostHist = costHist;
        mMileages = mileages;

        HashMap<String, String> tempSpecs;// = new HashMap<>();
        tempSpecs = mVehicle.getGeneralSpecs();
        if (!tempSpecs.isEmpty()) {
            mVehicleInfoArray.add(tempSpecs);
            viewTypes.add(VIEW_SPECS_GEN);
        }
        if (!costHist.isEmpty()) {
            HashMap<String, ArrayList> tempCost = new HashMap<>();
            tempCost.put("cost", mCostHist);
            mVehicleInfoArray.add(tempCost);
            viewTypes.add(VIEW_CHART_COST);
        }
        if (!mMileages.isEmpty()) {
            HashMap<String, ArrayList> tempMileage = new HashMap<>();
            tempMileage.put("mileage", mMileages);
            mVehicleInfoArray.add(tempMileage);
            viewTypes.add(VIEW_CHART_MILEAGE);
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
        switch (viewTypes.get(stuipidI)){
            case VIEW_CHART_COST:
                vChartCost = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info_expense, viewGroup, false);
                stuipidI++;

               return new vhChart(vChartCost, null);

           case VIEW_CHART_MILEAGE:
               vChartMileage = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info_mileage, viewGroup, false);
               stuipidI++;

               return new vhChart(vChartMileage, null);

           default :
               vSpecs = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info, viewGroup, false);
               cardInfo.clear();
               stuipidI++;

               return new vhSpecs(vSpecs, cardInfo, viewTypes.get(i), i, null);
       }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        cardInfo = mVehicleInfoArray.get(i);

        switch (viewTypes.get(i)){
            case VIEW_CHART_COST:
                new vhChart(vChartCost, mCostHist);
                break;

            case VIEW_CHART_MILEAGE:
                new vhChart(vChartMileage, new ArrayList<ArrayList>()); //FIX
                break;

            default :
                new vhSpecs(vSpecs, cardInfo, viewTypes.get(i), i, mVehicle.getTitle());
                break;
        }
    }
}

@SuppressWarnings("ResourceType")
class vhSpecs extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_specs";

    private TypedArray headerColors;

    public vhSpecs(View view, final HashMap<String, String> cardInfo, int viewType, int i, String vehicleTitle) {
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

        if(cardInfo.size() > 0) {
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
            case 3:
                return R.string.header_engine;
            case 4:
                return R.string.header_power_train;
            case 5:
                return R.string.header_other;
            default:
                return -1;
        }
    }
}

@SuppressWarnings("ResourceType")
class vhChart extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_chrt";

    private TypedArray headerColors;

    public vhChart(View view, final ArrayList<ArrayList> vehicleHist) {
        super(view);
        final String[] months = view.getResources().getStringArray(R.array.spec_month);

        View relLayout = view.findViewById(R.id.card_info_rel);
        DisplayMetrics metrics = relLayout.getContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        TreeMap<Integer, Float> costHistory = new TreeMap<>();
        List<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xLegend = new ArrayList<>();

        if (vehicleHist != null) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);
            TextView vTitle = (TextView) view.findViewById(R.id.card_info_title);
            ImageView imageView = (ImageView) view.findViewById(R.id.card_info_iv);
            imageView.setBackgroundColor(headerColors.getColor(1, 0));

            vTitle.setText(view.getResources().getString(R.string.header_chart));
            vTitle.setMinWidth(width / 3);
            vTitle.setMaxWidth(width);

            view.animate();

            HorizontalBarChart mChart = (HorizontalBarChart) view.findViewById(R.id.chart);
            mChart.setDrawValueAboveBar(true);
            //mChart.setDrawHighlightArrow(true);
            mChart.setDescription(null);
            mChart.getLegend().setEnabled(false);
            mChart.getAxisLeft().setEnabled(false);
            mChart.getAxisRight().setEnabled(false);
            mChart.setGridBackgroundColor(Color.TRANSPARENT);
            mChart.setTouchEnabled(false);
            //mChart.setFitBars(false);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setGridLineWidth(0.3f);
            xAxis.setAxisLineWidth(0.3f);
            xAxis.setTextColor(ContextCompat.getColor(view.getContext(), R.color.secondary_text));
            xAxis.setTextSize(18f);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xLegend));

            try {
                for (int i = 0; i < vehicleHist.size(); i++) {
                    ArrayList<String> tempEvent = vehicleHist.get(i);
                    String[] dateArray = tempEvent.get(0).split("/");
                    int month = Integer.parseInt(dateArray[0]);
                    int year = Integer.parseInt(dateArray[2]);
                    int yearOffset = Calendar.getInstance().get(Calendar.YEAR) - year;

                    if (yearOffset != 0) {
                        //Lets take that old boy and put it in a mod 12
                        month += (12 * yearOffset);
                    }

                    if (costHistory.containsKey(month)) {
                        costHistory.put(month, costHistory.get(month) + Float.parseFloat(tempEvent.get(1)));
                    } else {
                        costHistory.put(month, Float.parseFloat(tempEvent.get(1)));
                    }
                }

                int i = 0;
                for (int key : costHistory.keySet()) {
                    if (i > 3) {
                        break;
                    }

                    xLegend.add(months[(key - 1) % 12]);
                    barEntries.add(new BarEntry(i, costHistory.get(key)));
                    i++;
                }



                Log.d(TAG, costHistory.toString());
                Log.d(TAG, barEntries.toString());
                Log.d(TAG, xLegend.toString());

                BarDataSet barDataSet = new BarDataSet(barEntries, null);
                barDataSet.setColor(headerColors.getColor(6, 0));

                BarData data = new BarData(barDataSet);
                data.setValueTextSize(10f);
                data.setBarWidth(0.9f);

                mChart.setData(data);
                mChart.animateY(2500);

            } catch (NumberFormatException e) {
                //e.printStackTrace();
                Log.i(TAG, "No date found");
            }
        }
    }
}

class MyXAxisValueFormatter implements AxisValueFormatter {

    private ArrayList<String> mValues;

    public MyXAxisValueFormatter(ArrayList<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        if (value > 1) {
            return mValues.get((int) value) + "";
        }

        return mValues.get(0) + "";
    }

    /** this is only needed if numbers are returned, else return 0 */
    @Override
    public int getDecimalDigits() { return 0; }
}
