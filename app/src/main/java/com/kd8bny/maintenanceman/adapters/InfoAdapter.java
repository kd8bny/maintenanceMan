package com.kd8bny.maintenanceman.adapters;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.StyleRes;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adptr_inf";

    private ArrayList<HashMap> vehicleInfoArray = new ArrayList<>();
    private ArrayList<Integer> itemPos = new ArrayList<>();
    private HashMap<String, String> cardInfo = new HashMap<>();
    private ArrayList<ArrayList> mCostHist;
    private Vehicle mVehicle;

    private static final int VIEW_SPECS = 0;
    private static final int VIEW_CHART = 1;
    private int chartPos = 1;

    protected View vSpecs;
    protected View vChart;

    public InfoAdapter(Vehicle vehicle, ArrayList<ArrayList> costHist) {
        mVehicle = vehicle;
        mCostHist = costHist;
        ArrayList<HashMap> temp = new ArrayList<>();
        temp.add(mVehicle.getGeneralSpecs());
        temp.add(mVehicle.getEngineSpecs());
        temp.add(mVehicle.getPowerTrainSpecs());
        temp.add(mVehicle.getOtherSpecs());
        if (!costHist.isEmpty()){
            HashMap<String, ArrayList> tempCost = new HashMap<>();
            tempCost.put("cost", costHist);
            temp.add(1, tempCost);
        }else{
            temp.add(1, new HashMap());
        }

        vehicleInfoArray = (ArrayList<HashMap>) temp.clone();
        for (int i = temp.size()-1; i >= 0 ; i--) { //Remove Empty HashMaps
            if (temp.get(i).isEmpty()) {
                vehicleInfoArray.remove(i);
                if (i == 0 && chartPos != -1) {
                    chartPos = 0;
                } else if (i == 1) {
                    chartPos = -1;
                }
            } else {
                itemPos.add(0, i);
            }
        }
    }

    public int getItemViewType(int i){
        if(i == chartPos) {
            return VIEW_CHART;
        }
        return VIEW_SPECS;
    }

    @Override
    public int getItemCount() {
        return vehicleInfoArray.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       switch(i) {
           case VIEW_CHART:
               vChart = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info_chart, viewGroup, false);

               return new vhChart(vChart, null);

           case VIEW_SPECS:
               vSpecs = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info, viewGroup, false);
               cardInfo.clear();

               return new vhSpecs(vSpecs, cardInfo, null, i, null);

           default:
               Log.e(TAG, "No view");
               return null;
       }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        cardInfo = vehicleInfoArray.get(i);

        switch (getItemViewType(i)) {
            case VIEW_CHART:
                new vhChart(vChart, mCostHist);
                break;

            case VIEW_SPECS:
                new vhSpecs(vSpecs, cardInfo, itemPos, i, mVehicle.getTitle());
                break;
        }
    }
}

@SuppressWarnings("ResourceType")
class vhSpecs extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_gen";

    private TypedArray headerColors;

    public vhSpecs(View view, final HashMap<String, String> cardInfo,
                   ArrayList<Integer> itemPos, int i, String vehicleTitle) {
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
            if (itemPos.get(i) == 0) {
                vHeader.setText(vehicleTitle);
            }else {
                vHeader.setText(getTitle(itemPos, i));
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

                    vTitle.setText(key + ": ");
                    vItem.setText(cardInfo.get(key));

                    tempLinLay.addView(vTitle);
                    tempLinLay.addView(vItem);
                    ((LinearLayout) linLayout).addView(tempLinLay, textViewItemParams);
                }
            }
        }
    }

    private int getTitle(ArrayList<Integer> itemPos, int i){
        switch (itemPos.get(i)){
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
class vhChart extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_chrt";

    private TypedArray headerColors;

    public vhChart(View view, final ArrayList<ArrayList> vehicleHist) {
        super(view);
        final String[] months = view.getResources().getStringArray(R.array.spec_month);

        View relLayout = view.findViewById(R.id.card_info_rel);
        DisplayMetrics metrics = relLayout.getContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        ArrayList<BarEntry> yvals = new ArrayList<>();
        ArrayList<String> xvals = new ArrayList<>();
        ArrayList<Integer> xvalsNum = new ArrayList<>();
        ArrayList<Float> yvalsNum = new ArrayList<>();

        if (vehicleHist != null) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);
            TextView vTitle = (TextView) view.findViewById(R.id.card_info_title);
            ImageView imageView = (ImageView) view.findViewById(R.id.card_info_iv);
            imageView.setBackgroundColor(headerColors.getColor(1, 0));

            vTitle.setText(view.getResources().getString(R.string.header_chart));
            vTitle.setMinWidth(width/3);
            vTitle.setMaxWidth(width);

            view.animate();

            HorizontalBarChart mchart = (HorizontalBarChart) view.findViewById(R.id.chart);
            mchart.setDrawValueAboveBar(true);
            mchart.setDrawHighlightArrow(true);
            mchart.setDescription(null);
            mchart.getLegend().setEnabled(false);
            mchart.getAxisLeft().setEnabled(false);
            mchart.getAxisRight().setEnabled(false);
            mchart.setGridBackgroundColor(Color.TRANSPARENT);
            mchart.setTouchEnabled(false);

            XAxis xAxis = mchart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setGridLineWidth(0.3f);
            xAxis.setTextColor(view.getResources().getColor(R.color.secondary_text));
            xAxis.setTextSize(view.getResources().getDimension(R.dimen.field_font));

            try {
                for (int i = 0; i < vehicleHist.size(); i++) {
                    ArrayList<String> tempEvent = vehicleHist.get(i);
                    String[] dateArray = tempEvent.get(0).split("/");
                    int monthLog = Integer.parseInt(dateArray[0]);

                    if (xvalsNum.size() > 0){
                        if (xvalsNum.get(xvalsNum.size()-1) == monthLog) {
                            yvalsNum.add(
                                    yvalsNum.size()-1,
                                    yvalsNum.get(yvalsNum.size()-1) +
                                            Float.parseFloat(tempEvent.get(1)));
                        }else {
                            xvalsNum.add(monthLog);
                            yvalsNum.add(Float.parseFloat(tempEvent.get(1)));
                        }
                    }else {
                        xvalsNum.add(monthLog);
                        yvalsNum.add(Float.parseFloat(tempEvent.get(1)));
                    }
                }

                if(xvalsNum.size() > 0) {
                    int i = 0;
                    while (i < xvalsNum.size() && i < 4) {
                        xvals.add(months[xvalsNum.get(i) - 1]);
                        yvals.add(new BarEntry(yvalsNum.get(i), i));
                        i++;
                    }

                    BarDataSet barDataSet = new BarDataSet(yvals, null);
                    barDataSet.setColor(headerColors.getColor(6, 0));
                    ArrayList<BarDataSet> dataSets = new ArrayList<>();
                    dataSets.add(barDataSet);

                    BarData data = new BarData(xvals, dataSets);
                    data.setValueTextSize(10f);

                    mchart.setData(data);
                    mchart.animateY(2500);
                }

            }catch (NumberFormatException e){
                //e.printStackTrace();
                Log.i(TAG, "No date found");
            }
        }
    }
}
