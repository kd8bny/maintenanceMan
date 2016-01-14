package com.kd8bny.maintenanceman.classes.data;

import android.content.Context;
import android.util.Log;

import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by kd8bny on 12/29/15.
 */
public class SaveLoadHelper {
    private static final String TAG = "svLdHlpr";

    private static String FILE_NAME;
    private Context context;

    public SaveLoadHelper(Context context){
        this.context = context;
        FILE_NAME = this.context.getFilesDir() + "/" + "roster.ser";
    }

    public Boolean save(ArrayList<Vehicle> l){
        /**
         * Saves objects returns true is successful
         * Auto saves to cloud source
         */
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            objectOutputStream.writeObject(l);
            objectOutputStream.close();

            backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
            mbackupRestoreHelper.startAction(context, "backup", false);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public ArrayList<Vehicle> load(){
        ArrayList<Vehicle> temp = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            temp = (ArrayList) objectInputStream.readObject();

        }catch (FileNotFoundException e){
        }catch (IOException | ClassNotFoundException e){e.printStackTrace();}

        return temp;
    }
}
