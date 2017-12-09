package com.kd8bny.maintenanceman.interfaces;

import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.util.ArrayList;

public interface QueryComplete {
        void fleetRosterUpdate(ArrayList<Vehicle> roster);
}
