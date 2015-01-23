package com.kd8bny.maintenanceman.ui.history;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class adapter_info extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<ArrayList> vehicleInfo = new ArrayList<>();
    public ArrayList<String> cardInfo = new ArrayList<>();

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
                View itemViewEngine = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.info_card_eng, viewGroup, false);
                return new ViewHolderEngine(itemViewEngine, cardInfo);

            case VIEW_TIRES:
                View itemViewTires = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.info_card_tires, viewGroup, false);
                return new ViewHolderTires(itemViewTires, cardInfo);

            default: //General
                View itemViewGeneral = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.info_card_gen, viewGroup, false);
                return new ViewHolderGeneral(itemViewGeneral, cardInfo);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        cardInfo = vehicleInfo.get(i);
        Log.i(TAG,cardInfo+"");
        switch (getItemViewType(i)) {
            case VIEW_GENERAL:
                ViewHolderGeneral viewHolderGeneral = (ViewHolderGeneral) viewHolder;
                viewHolderGeneral.vyear.setText(cardInfo.get(0));
                viewHolderGeneral.vmake.setText(cardInfo.get(1));
                viewHolderGeneral.vmodel.setText(cardInfo.get(2));
                viewHolderGeneral.vplate.setText(cardInfo.get(3)); //TODO dict

                break;

            case VIEW_ENGINE:
                ViewHolderEngine viewHolderEngine = (ViewHolderEngine) viewHolder;
                viewHolderEngine.vsize.setText(cardInfo.get(0));
                viewHolderEngine.vfilter.setText(cardInfo.get(1));
                viewHolderEngine.vweight.setText(cardInfo.get(2));//TODO dict

                break;

            case VIEW_TIRES:
                ViewHolderTires viewHolderTires = (ViewHolderTires) viewHolder;
                viewHolderTires.vsummerSize.setText(cardInfo.get(0));
                viewHolderTires.vwinterSize.setText(cardInfo.get(1));//TODO dict

                break;
            }
        }
    }

    class ViewHolderGeneral extends RecyclerView.ViewHolder {
        protected TextView vyear;
        protected TextView vmake;
        protected TextView vmodel;
        protected TextView vplate;

        public ViewHolderGeneral(View view, final ArrayList<String> vehicleList) {
            super(view);

            //view.setTag(vehicleList);

            //TODO card views these are null
            vyear = (TextView) view.findViewById(R.id.year);
            vmake = (TextView) view.findViewById(R.id.make);
            vmodel = (TextView) view.findViewById(R.id.model);
            vplate = (TextView) view.findViewById(R.id.plate);
        }
    }

    class ViewHolderEngine extends RecyclerView.ViewHolder {
        protected TextView vsize;
        protected TextView vfilter;
        protected TextView vweight;

        public ViewHolderEngine(View view, final ArrayList<String> vehicleList) {
            super(view);

            //view.setTag(vehicleList);

            //TODO card views these are null
            vsize = (TextView) view.findViewById(R.id.eng);
            vfilter = (TextView) view.findViewById(R.id.val_spec_oil_filter);
            vweight = (TextView) view.findViewById(R.id.val_spec_oil_weight);
        }
    }

        class ViewHolderTires extends RecyclerView.ViewHolder {
            protected TextView vsummerSize;
            protected TextView vwinterSize;

            public ViewHolderTires(View view, final ArrayList<String> vehicleList) {
                super(view);

                //view.setTag(vehicleList);

                //TODO card views these are null
                vsummerSize = (TextView) view.findViewById(R.id.summer_size);
                vwinterSize = (TextView) view.findViewById(R.id.winter_size);
            }

        }