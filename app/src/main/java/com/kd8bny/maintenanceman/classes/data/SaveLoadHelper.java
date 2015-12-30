package com.kd8bny.maintenanceman.classes.data;

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

    private static final String FILE_NAME = "roster.ser";

    public SaveLoadHelper(){
    }

    public Boolean save(ArrayList<Vehicle> l){
        /**
         * Saves objects returns true is successful
         */
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            for (Vehicle v : l) {
                objectOutputStream.writeObject(v);
            }
            objectOutputStream.close();
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

            while (true){
                temp.add((Vehicle) objectInputStream.readObject());
            }
        }catch (EOFException | FileNotFoundException e){
        }catch (IOException | ClassNotFoundException e){e.printStackTrace();}

        return temp;
    }
}
