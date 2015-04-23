package com.kd8bny.maintenanceman.ui.history;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class adapter_info extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adptr_inf";

    public ArrayList<HashMap> vehicleInfoArray = new ArrayList<>();
    public ArrayList<String> keyList = new ArrayList<>();
    public Map<String, String> cardInfo = new LinkedHashMap<>();

    protected View itemViewGeneral;
    protected View itemViewEngine;
    protected View itemViewPWR;
    protected View itemViewOther;

    private static final int VIEW_GENERAL = 0;
    private static final int VIEW_ENGINE = 1;
    private static final int VIEW_PWR = 2;
    private static final int VIEW_OTHER = 3;

    public adapter_info(HashMap<String, HashMap> vehicleInfo) {
        for (String key : vehicleInfo.keySet()) {
            if (vehicleInfo.get(key) != null) {
                this.vehicleInfoArray.add(vehicleInfo.get(key));
                this.keyList.add(key);
            }
        }
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
        }
        
        Log.e(TAG, "Itemview Type");
        return -1;
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

                break;
            }
        }
    }

    class ViewHolderGeneral extends RecyclerView.ViewHolder {
        private static final String TAG = "adptr_info_VwHldr_gen";

        private TypedArray headerColors;

        public ViewHolderGeneral(View view, final Map<String, String> cardInfo) {
            super(view);

            View layout = view.findViewById(R.id.card_info_lin);

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

            if(cardInfo.size() > 0) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                //Header
                TextView tempHeaderTitle = new TextView(view.getContext());

                tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                tempHeaderTitle.setPadding(hMargin, vMargin, hMargin, hMargin);
                tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
                tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
                tempHeaderTitle.setText(R.string.header_general);
                tempHeaderTitle.setBackgroundColor(headerColors.getColor(0, 0));

                ((LinearLayout) layout).addView(tempHeaderTitle);

                for (String key : cardInfo.keySet()) {
                    if(!cardInfo.get(key).isEmpty() && !key.equals("type")) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);

                        tempTextView.setText(key + ":     " + cardInfo.get(key));

                        ((LinearLayout) layout).addView(tempTextView);
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

            View layout = view.findViewById(R.id.card_info_lin);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

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

            if(cardInfo.size() > 0) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                //Header
                TextView tempHeaderTitle = new TextView(view.getContext());

                tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                tempHeaderTitle.setPadding(hMargin, vMargin, hMargin, hMargin);
                tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
                tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
                tempHeaderTitle.setText(R.string.header_engine);
                tempHeaderTitle.setBackgroundColor(headerColors.getColor(1, 0));

                ((LinearLayout) layout).addView(tempHeaderTitle);

                for (String key : cardInfo.keySet()) {
                    if(!cardInfo.get(key).isEmpty()) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);

                        tempTextView.setText(key + ":     " + cardInfo.get(key));

                        ((LinearLayout) layout).addView(tempTextView);
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

            View layout = view.findViewById(R.id.card_info_lin);

            DisplayMetrics metrics = layout.getContext().getResources().getDisplayMetrics();
            float hMdp = view.getResources().getDimension(R.dimen.start_end_horizontal_margin);
            float vMdp = view.getResources().getDimension(R.dimen.heading_sub_vertical_margin);
            float vCSdp = view.getResources().getDimension(R.dimen.content_vertical_space);
            float hCSdp = view.getResources().getDimension(R.dimen.content_horizontal_margin);

            int hMargin = (int) (hMdp / metrics.density + 0.5f);
            int vMargin = (int) (vMdp / metrics.density + 0.5f);
            int vcontentSpace = (int) (vCSdp / metrics.density + 0.5f);
            int hcontentSpace = (int) (hCSdp / metrics.density + 0.5f);

            float fieldsp = view.getResources().getDimension(R.dimen.field_font);
            float headersp = view.getResources().getDimension(R.dimen.header_font);
            int fieldSize = (int) (fieldsp / metrics.density + 0.5f);
            int headerSize = (int) (headersp / metrics.density + 0.5f);

            if (cardInfo.size() > 0) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                //Header
                TextView tempHeaderTitle = new TextView(view.getContext());

                tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                tempHeaderTitle.setPadding(hMargin, vMargin, hMargin, hMargin);
                tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
                tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
                tempHeaderTitle.setText(R.string.header_power_train);
                tempHeaderTitle.setBackgroundColor(headerColors.getColor(2, 0));

                ((LinearLayout) layout).addView(tempHeaderTitle);

                for (String key : cardInfo.keySet()) {
                    if (!cardInfo.get(key).isEmpty()) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);

                        tempTextView.setText(key + ":     " + cardInfo.get(key));

                        ((LinearLayout) layout).addView(tempTextView);
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

            View layout = view.findViewById(R.id.card_info_lin);

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

            if(cardInfo.size() > 0) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                //Header
                TextView tempHeaderTitle = new TextView(view.getContext());

                tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                tempHeaderTitle.setPadding(hMargin, vMargin, hMargin, hMargin);
                tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
                tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
                tempHeaderTitle.setText(R.string.header_other);
                tempHeaderTitle.setBackgroundColor(headerColors.getColor(2, 0));

                ((LinearLayout) layout).addView(tempHeaderTitle);

                for (String key : cardInfo.keySet()) {
                    if(!cardInfo.get(key).isEmpty()) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);

                        tempTextView.setText(key + ":     " + cardInfo.get(key));

                        ((LinearLayout) layout).addView(tempTextView);
                    }
                }
            }
        }
    }