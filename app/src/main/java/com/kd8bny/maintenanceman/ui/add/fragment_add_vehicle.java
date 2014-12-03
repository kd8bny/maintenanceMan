package com.kd8bny.maintenanceman.ui.add;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.GpsStatus;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.content.Context;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.dialogs.prompt_text;
import com.kd8bny.maintenanceman.ui.year;
import com.kd8bny.maintenanceman.data.vehicleLog;

import java.util.ArrayList;


public class fragment_add_vehicle extends ListFragment {

    private static final String TAG = "fragment_add_vehicle";

    private vehicleLog mvehicleLog;
    private int value;
    private ArrayList<String> mvehicleData;
    private static final String dialog_year = "Vehicle Year"; //TODO:String??

    public void add_vehicle(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvehicleLog = new vehicleLog();
        setHasOptionsMenu(true);
        ArrayList<vehicleLog> mvehicleData = mvehicleLog.getData(this.getResources());

        ArrayAdapter <vehicleLog> adapter =
                new ArrayAdapter<vehicleLog>(getActivity(),android.R.layout.simple_list_item_1, mvehicleData);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        String item = (String)(getListAdapter()).getItem(pos);

        //Intent i = new Intent(getActivity(), vehicleLog.class);
        //startActivity(i);

        //FragmentManager fm = getActivity()
        //        .getFragmentManager();
        //prompt_text dialog = new prompt_text();
        //dialog.show(fm, null);

        //Dialog dialog = prompt_text.newInstance(this);

        //dialog.setTargetFragment(this, 0);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, data.toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_vehicle, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;

            case R.id.menu_save:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

