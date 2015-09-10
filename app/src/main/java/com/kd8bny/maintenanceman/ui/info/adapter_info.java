package com.kd8bny.maintenanceman.ui.info;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class adapter_info extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adptr_inf";

    public ArrayList<HashMap> vehicleInfoArray = new ArrayList<>();
    public ArrayList<String> keyList = new ArrayList<>();
    public Map<String, String> cardInfo = new LinkedHashMap<>();
    public ArrayList<ArrayList> vehicleHist = new ArrayList<>();

    protected View itemViewGeneral;
    protected View itemViewEngine;
    protected View itemViewPWR;
    protected View itemViewOther;
    protected View itemViewChart;

    private static final int VIEW_GENERAL = 0;
    private static final int VIEW_ENGINE = 1;
    private static final int VIEW_PWR = 2;
    private static final int VIEW_OTHER = 3;
    private static final int VIEW_CHART = 4;

    public adapter_info(HashMap<String, HashMap> vehicleInfo, ArrayList<ArrayList> vehicleHist) {
        //info
        for (String key : vehicleInfo.keySet()) {
            if (vehicleInfo.get(key) != null) {
                this.vehicleInfoArray.add(vehicleInfo.get(key));
                this.keyList.add(key); //todo reverse keylist
            }
        }
        //hist
        this.vehicleHist = vehicleHist;
        this.vehicleInfoArray.add(null);
        this.keyList.add("Chart");
    }

    @Override
    public int getItemViewType(int i){
        switch (keyList.get(i)){
            case "General":
                return VIEW_GENERAL;

            case "Engine":
                return VIEW_ENGINE;

            case "Power Train":
                return VIEW_PWR;

            case "Other":
                return VIEW_OTHER;

            case "Chart":
                return VIEW_CHART;

            default:
                Log.e(TAG, "Itemview Type");

                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return vehicleInfoArray.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       switch(i) {
           case VIEW_GENERAL:
               itemViewGeneral = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info, viewGroup, false);
               cardInfo.clear();

               return new ViewHolderGeneral(itemViewGeneral, cardInfo);

           case VIEW_ENGINE:
                itemViewEngine = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();

                return new ViewHolderEngine(itemViewEngine, cardInfo);

           case VIEW_PWR:
                itemViewPWR = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();

                return new ViewHolderPWR(itemViewPWR, cardInfo);

           case VIEW_OTHER:
                itemViewOther = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();

               return new ViewHolderOther(itemViewOther, cardInfo);

           case VIEW_CHART:
               Log.d(TAG, "create");
               itemViewChart = LayoutInflater
                       .from(viewGroup.getContext())
                       .inflate(R.layout.card_info_chart, viewGroup, false);

               return new ViewHolderChart(itemViewChart, null);

           default:
               Log.e(TAG, "No view");

               return null;
       }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        cardInfo = vehicleInfoArray.get(i);

        switch (getItemViewType(i)) {
            case VIEW_GENERAL:
                new ViewHolderGeneral(itemViewGeneral, cardInfo);

                break;

            case VIEW_ENGINE:
                new ViewHolderEngine(itemViewEngine, cardInfo);

                break;

            case VIEW_PWR:
                new ViewHolderPWR(itemViewPWR, cardInfo);

                break;

            case VIEW_OTHER:
                new ViewHolderOther(itemViewOther, cardInfo);

            case VIEW_CHART:
                Log.d(TAG,"bound");
                new ViewHolderChart(itemViewChart, vehicleHist);

                break;
            }
        }
    }

class ViewHolderGeneral extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_gen";

    private TypedArray headerColors;

    public ViewHolderGeneral(View view, final Map<String, String> cardInfo) {
        super(view);

        View layout = view.findViewById(R.id.card_info_rel);

        DisplayMetrics metrics = layout.getContext().getResources().getDisplayMetrics();
        float hMdp =  view.getResources().getDimension(R.dimen.start_end_horizontal_margin);
        float vMdp =  view.getResources().getDimension(R.dimen.heading_sub_vertical_margin);
        float vCSdp = view.getResources().getDimension(R.dimen.content_vertical_space);
        float hCSdp = view.getResources().getDimension(R.dimen.content_horizontal_margin);

        int hMargin = (int) (hMdp/metrics.density + 0.5f);
        int vMargin = (int) (vMdp/metrics.density + 0.5f);
        int vcontentSpace = (int) (vCSdp/metrics.density + 0.5f);
        int hcontentSpace = (int) (hCSdp/metrics.density + 0.5f);

        float fieldsp = view.getResources().getDimension(R.dimen.field_font);
        float headersp = view.getResources().getDimension(R.dimen.header_font);
        int fieldSize = (int) (fieldsp/metrics.density + 0.5f);
        int headerSize = (int) (headersp/metrics.density + 0.5f);

        LinearLayout.LayoutParams textViewTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textViewItemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewTitleParams.setMarginStart(50);
        textViewItemParams.setMarginStart(hMargin);

        if(cardInfo.size() > 0) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);

            //Header
            TextView tempHeaderTitle = new TextView(view.getContext());

            tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            tempHeaderTitle.setPadding(hMargin, 0, 0, 0);
            tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
            tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
            tempHeaderTitle.setText(R.string.header_general);
            tempHeaderTitle.setBackgroundColor(headerColors.getColor(0, 0));

            ((LinearLayout) layout).addView(tempHeaderTitle);

            for (String key : cardInfo.keySet()) {
                if(!cardInfo.get(key).isEmpty() && !key.equals("type")) {
                    LinearLayout tempLinLay = new LinearLayout(view.getContext());
                    TextView tempTitleTextView = new TextView(view.getContext());
                    TextView tempItemTextView = new TextView(view.getContext());
                    tempLinLay.setOrientation(LinearLayout.HORIZONTAL);

                    tempTitleTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tempItemTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    tempTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempTitleTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));
                    tempItemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempItemTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                    tempTitleTextView.setMinEms(5);
                    tempTitleTextView.setMaxEms(5);
                    tempTitleTextView.setGravity(Gravity.START);
                    tempItemTextView.setGravity(Gravity.START);

                    tempTitleTextView.setText(key + ": ");
                    tempItemTextView.setText(cardInfo.get(key));

                    tempLinLay.addView(tempTitleTextView);
                    tempLinLay.addView(tempItemTextView);
                    ((LinearLayout) layout).addView(tempLinLay, textViewItemParams);
                }
            }
        }
    }
}

class ViewHolderEngine extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_eng";

    private TypedArray headerColors;

    public ViewHolderEngine(View view, final Map<String, String> cardInfo) {
        super(view);

        View layout = view.findViewById(R.id.card_info_rel);

        DisplayMetrics metrics = layout.getContext().getResources().getDisplayMetrics();
        float hMdp =  view.getResources().getDimension(R.dimen.start_end_horizontal_margin);
        float vMdp =  view.getResources().getDimension(R.dimen.heading_sub_vertical_margin);
        float vCSdp = view.getResources().getDimension(R.dimen.content_vertical_space);
        float hCSdp = view.getResources().getDimension(R.dimen.content_horizontal_margin);

        int hMargin = (int) (hMdp/metrics.density + 0.5f);
        int vMargin = (int) (vMdp/metrics.density + 0.5f);
        int vcontentSpace = (int) (vCSdp/metrics.density + 0.5f);
        int hcontentSpace = (int) (hCSdp/metrics.density + 0.5f);

        float fieldsp = view.getResources().getDimension(R.dimen.field_font);
        float headersp = view.getResources().getDimension(R.dimen.header_font);
        int fieldSize = (int) (fieldsp/metrics.density + 0.5f);
        int headerSize = (int) (headersp/metrics.density + 0.5f);

        LinearLayout.LayoutParams textViewTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textViewItemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewTitleParams.setMarginStart(50);
        textViewItemParams.setMarginStart(hMargin);

        if(cardInfo.size() > 0) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);

            //Header
            TextView tempHeaderTitle = new TextView(view.getContext());

            tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            tempHeaderTitle.setPadding(hMargin, 0, 0, 0);
            tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
            tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
            tempHeaderTitle.setText(R.string.header_engine);
            tempHeaderTitle.setBackgroundColor(headerColors.getColor(1, 0));

            ((LinearLayout) layout).addView(tempHeaderTitle);

            for (String key : cardInfo.keySet()) {
                if(!cardInfo.get(key).isEmpty()) {
                    LinearLayout tempLinLay = new LinearLayout(view.getContext());
                    TextView tempTitleTextView = new TextView(view.getContext());
                    TextView tempItemTextView = new TextView(view.getContext());
                    tempLinLay.setOrientation(LinearLayout.HORIZONTAL);

                    tempTitleTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tempItemTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    tempTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempTitleTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));
                    tempItemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempItemTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                    tempTitleTextView.setMinEms(5);
                    tempTitleTextView.setMaxEms(5);
                    tempTitleTextView.setGravity(Gravity.START);
                    tempItemTextView.setGravity(Gravity.START);

                    tempTitleTextView.setText(key + ": ");
                    tempItemTextView.setText(cardInfo.get(key));

                    tempLinLay.addView(tempTitleTextView);
                    tempLinLay.addView(tempItemTextView);
                    ((LinearLayout) layout).addView(tempLinLay, textViewItemParams);
                }
            }
        }
    }
}

class ViewHolderPWR extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_pwr";

    private TypedArray headerColors;

    public ViewHolderPWR(View view, final Map<String, String> cardInfo) {
        super(view);

        View layout = view.findViewById(R.id.card_info_rel);

        DisplayMetrics metrics = layout.getContext().getResources().getDisplayMetrics();
        float hMdp =  view.getResources().getDimension(R.dimen.start_end_horizontal_margin);
        float vMdp =  view.getResources().getDimension(R.dimen.heading_sub_vertical_margin);
        float vCSdp = view.getResources().getDimension(R.dimen.content_vertical_space);
        float hCSdp = view.getResources().getDimension(R.dimen.content_horizontal_margin);

        int hMargin = (int) (hMdp/metrics.density + 0.5f);
        int vMargin = (int) (vMdp/metrics.density + 0.5f);
        int vcontentSpace = (int) (vCSdp/metrics.density + 0.5f);
        int hcontentSpace = (int) (hCSdp/metrics.density + 0.5f);

        float fieldsp = view.getResources().getDimension(R.dimen.field_font);
        float headersp = view.getResources().getDimension(R.dimen.header_font);
        int fieldSize = (int) (fieldsp/metrics.density + 0.5f);
        int headerSize = (int) (headersp/metrics.density + 0.5f);

        LinearLayout.LayoutParams textViewTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textViewItemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewTitleParams.setMarginStart(50);
        textViewItemParams.setMarginStart(hMargin);

        if (cardInfo.size() > 0) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);

            //Header
            TextView tempHeaderTitle = new TextView(view.getContext());

            tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            tempHeaderTitle.setPadding(hMargin, 0, 0, 0);
            tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
            tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
            tempHeaderTitle.setText(R.string.header_power_train);
            tempHeaderTitle.setBackgroundColor(headerColors.getColor(2, 0));

            ((LinearLayout) layout).addView(tempHeaderTitle);

            for (String key : cardInfo.keySet()) {
                if (!cardInfo.get(key).isEmpty()) {
                    LinearLayout tempLinLay = new LinearLayout(view.getContext());
                    TextView tempTitleTextView = new TextView(view.getContext());
                    TextView tempItemTextView = new TextView(view.getContext());
                    tempLinLay.setOrientation(LinearLayout.HORIZONTAL);

                    tempTitleTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tempItemTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    tempTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempTitleTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));
                    tempItemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempItemTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                    tempTitleTextView.setMinEms(5);
                    tempTitleTextView.setMaxEms(5);
                    tempTitleTextView.setGravity(Gravity.START);
                    tempItemTextView.setGravity(Gravity.START);

                    tempTitleTextView.setText(key + ": ");
                    tempItemTextView.setText(cardInfo.get(key));

                    tempLinLay.addView(tempTitleTextView);
                    tempLinLay.addView(tempItemTextView);
                    ((LinearLayout) layout).addView(tempLinLay, textViewItemParams);
                }
            }
        }
    }
}

class ViewHolderOther extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_othr";

    private TypedArray headerColors;

    public ViewHolderOther(View view, final Map<String, String> cardInfo) {
        super(view);

        View layout = view.findViewById(R.id.card_info_rel);

        DisplayMetrics metrics = layout.getContext().getResources().getDisplayMetrics();
        float hMdp =  view.getResources().getDimension(R.dimen.start_end_horizontal_margin);
        float vMdp =  view.getResources().getDimension(R.dimen.heading_sub_vertical_margin);
        float vCSdp = view.getResources().getDimension(R.dimen.content_vertical_space);
        float hCSdp = view.getResources().getDimension(R.dimen.content_horizontal_margin);

        int hMargin = (int) (hMdp/metrics.density + 0.5f);
        int vMargin = (int) (vMdp/metrics.density + 0.5f);
        int vcontentSpace = (int) (vCSdp/metrics.density + 0.5f);
        int hcontentSpace = (int) (hCSdp/metrics.density + 0.5f);

        float fieldsp = view.getResources().getDimension(R.dimen.field_font);
        float headersp = view.getResources().getDimension(R.dimen.header_font);
        int fieldSize = (int) (fieldsp/metrics.density + 0.5f);
        int headerSize = (int) (headersp/metrics.density + 0.5f);

        LinearLayout.LayoutParams textViewTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textViewItemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewTitleParams.setMarginStart(50);
        textViewItemParams.setMarginStart(hMargin);

        if(cardInfo.size() > 0) {
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);

            //Header
            TextView tempHeaderTitle = new TextView(view.getContext());

            tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            tempHeaderTitle.setPadding(hMargin, 0, 0, 0);
            tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
            tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
            tempHeaderTitle.setText(R.string.header_other);
            tempHeaderTitle.setBackgroundColor(headerColors.getColor(2, 0));

            ((LinearLayout) layout).addView(tempHeaderTitle);

            for (String key : cardInfo.keySet()) {
                if(!cardInfo.get(key).isEmpty()) {
                    LinearLayout tempLinLay = new LinearLayout(view.getContext());
                    TextView tempTitleTextView = new TextView(view.getContext());
                    TextView tempItemTextView = new TextView(view.getContext());
                    tempLinLay.setOrientation(LinearLayout.HORIZONTAL);

                    tempTitleTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tempItemTextView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    tempTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempTitleTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));
                    tempItemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                    tempItemTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                    tempTitleTextView.setMinEms(5);
                    tempTitleTextView.setMaxEms(5);
                    tempTitleTextView.setGravity(Gravity.START);
                    tempItemTextView.setGravity(Gravity.START);

                    tempTitleTextView.setText(key + ": ");
                    tempItemTextView.setText(cardInfo.get(key));

                    tempLinLay.addView(tempTitleTextView);
                    tempLinLay.addView(tempItemTextView);
                    ((LinearLayout) layout).addView(tempLinLay, textViewItemParams);
                }
            }
        }
    }
}

class ViewHolderChart extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_info_VwHldr_chrt";

    private TypedArray headerColors;

    public ViewHolderChart(View view, final ArrayList<ArrayList> vehicleHist) {
        super(view);
        Log.d(TAG, "made");
        View layout = view.findViewById(R.id.card_info_chart);

        if (vehicleHist != null) {
            layout.animate();

            ColumnChartView chart = (ColumnChartView) layout.findViewById(R.id.chart);


            final Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH) + 1;
            for (int i = 0; i < vehicleHist.size(); i++) {
                ArrayList<String> temp = vehicleHist.get(i);
                String[] dateArray = temp.get(1).split("/");
                int monthLog = Integer.parseInt(dateArray[0]);
                if (monthLog <= month - 3) {
                    break;
                }
                if (!temp.get(4).isEmpty()) {
                    int price = Integer.parseInt(temp.get(4));

                }

            }
            Log.d(TAG,"col");
            ArrayList<SubcolumnValue> subcol = new ArrayList<>();
            subcol.add(new SubcolumnValue(5));
            subcol.add(new SubcolumnValue(10));
            subcol.add(new SubcolumnValue(2));
            ArrayList<Column> columns = new ArrayList<>();
            Column column = new Column();
            column.setValues(subcol);
            columns.add(column);

            ColumnChartData data = new ColumnChartData(columns);
            chart.setColumnChartData(data);

            chart.startDataAnimation();
        }
    }
}
