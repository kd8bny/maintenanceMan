package com.kd8bny.maintenanceman.ui.history;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.HashSet;

public class adapter_info extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adapter_info";

    public ArrayList<ArrayList> vehicleInfo = new ArrayList<>();
    public ArrayList<String> cardInfo = new ArrayList<>();

    protected View itemViewGeneral;
    protected View itemViewTires;
    protected View itemViewEngine;

    private static final int VIEW_GENERAL = 0;
    private static final int VIEW_ENGINE = 1;
    private static final int VIEW_TIRES = 2;

    public adapter_info(ArrayList vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    @Override
    public int getItemViewType(int pos){
        return pos;
    }

    @Override
    public int getItemCount() {
        return vehicleInfo.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch(viewType){
            case VIEW_ENGINE:
                itemViewEngine = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();
                return new ViewHolderEngine(itemViewEngine, cardInfo);

            case VIEW_TIRES:
                itemViewTires = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();
                return new ViewHolderTires(itemViewTires, cardInfo);

            default: //General
                itemViewGeneral = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_info, viewGroup, false);
                cardInfo.clear();
                return new ViewHolderGeneral(itemViewGeneral, cardInfo);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        cardInfo = vehicleInfo.get(i);
        switch (getItemViewType(i)) {
            case VIEW_GENERAL:
                new ViewHolderGeneral(itemViewGeneral, cardInfo);

                break;

            case VIEW_ENGINE:
                new ViewHolderEngine(itemViewEngine, cardInfo);

                break;

            case VIEW_TIRES:
                new ViewHolderTires(itemViewTires, cardInfo);

                break;
            }
        }
    }

    class ViewHolderGeneral extends RecyclerView.ViewHolder {
        private static final String TAG = "adapter_info_ViewHolder_gen";

        private TypedArray headerColors;
        private ArrayList<String> labels = new ArrayList<>();

        public ViewHolderGeneral(View view, final ArrayList<String> cardInfo) {
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

            HashSet tempHS = new HashSet();
            tempHS.addAll(cardInfo);

            if(tempHS.size()>1) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                labels.add(view.getResources().getString(R.string.spec_year));
                labels.add(view.getResources().getString(R.string.spec_make));
                labels.add(view.getResources().getString(R.string.spec_model));
                labels.add(view.getResources().getString(R.string.spec_plate));



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

                for (int i = 0; i < cardInfo.size(); i++) {
                    if (!cardInfo.get(i).isEmpty()) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);


                        tempTextView.setText(labels.get(i) + ":     " + cardInfo.get(i));

                        ((LinearLayout) layout).addView(tempTextView);

                    }
                }
            }
        }
    }

    class ViewHolderEngine extends RecyclerView.ViewHolder {
        private static final String TAG = "adapter_info_ViewHolder_eng";

        private TypedArray headerColors;
        private ArrayList<String> labels = new ArrayList<>();

        public ViewHolderEngine(View view, final ArrayList<String> cardInfo) {
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

            HashSet tempHS = new HashSet();
            tempHS.addAll(cardInfo);

            if(tempHS.size()>1) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                labels.add(view.getResources().getString(R.string.spec_engine));
                labels.add(view.getResources().getString(R.string.spec_oil_filter));
                labels.add(view.getResources().getString(R.string.spec_oil_weight));

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

                for (int i = 0; i < cardInfo.size(); i++) {
                    if (!cardInfo.get(i).isEmpty()) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);

                        tempTextView.setText(labels.get(i) + ":     " + cardInfo.get(i));

                        ((LinearLayout) layout).addView(tempTextView);

                    }
                }
            }
        }
    }


    class ViewHolderTires extends RecyclerView.ViewHolder {
        private static final String TAG = "adapter_info_ViewHolder_tires";

        private TypedArray headerColors;
        private ArrayList<String> labels = new ArrayList<>();


        public ViewHolderTires(View view, final ArrayList<String> cardInfo) {
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

            HashSet tempHS = new HashSet();
            tempHS.addAll(cardInfo);

            if(tempHS.size()>1) {
                headerColors = view.getResources().obtainTypedArray(R.array.header_color);

                labels.add(view.getResources().getString(R.string.spec_tire_size_winter));
                labels.add(view.getResources().getString(R.string.spec_tire_size_summer));

                //Header
                TextView tempHeaderTitle = new TextView(view.getContext());

                tempHeaderTitle.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                tempHeaderTitle.setPadding(hMargin, vMargin, hMargin, hMargin);
                tempHeaderTitle.setTextColor(view.getResources().getColor(R.color.header));
                tempHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerSize);
                tempHeaderTitle.setText(R.string.header_tires);
                tempHeaderTitle.setBackgroundColor(headerColors.getColor(2, 0));

                ((LinearLayout) layout).addView(tempHeaderTitle);

                for (int i = 0; i < cardInfo.size(); i++) {
                    if (!cardInfo.get(i).isEmpty()) {
                        TextView tempTextView = new TextView(view.getContext());

                        tempTextView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fieldSize);
                        tempTextView.setTextColor(view.getResources().getColor(R.color.secondary_text));

                        tempTextView.setPadding(hcontentSpace, vcontentSpace, hcontentSpace, vcontentSpace);
                        tempTextView.setGravity(Gravity.START);

                        tempTextView.setText(labels.get(i) + ":     " + cardInfo.get(i));

                        ((LinearLayout) layout).addView(tempTextView);

                    }
                }
            }
        }
    }