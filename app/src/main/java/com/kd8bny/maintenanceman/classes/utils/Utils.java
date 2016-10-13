package com.kd8bny.maintenanceman.classes.utils;

import org.joda.time.DateTime;

/**
 * Created by kd8bny on 10/12/16.
 */

public class Utils {

    public Utils(){

    }

    public String toEpoch(String date){
        DateTime fromDate = new DateTime(date);//"2011-07-19T18:23:20+0000"
        long epoch = fromDate.getMillis();
        System.out.println("Date is.." + fromDate + " epoch of date " + epochTime);

        Date is..2011-07-19T14:23:20.000-04:00 epoch of date 1311099800000

    }

    public String toDate(long epoch){
        DateTime dateTime = new DateTime(date*1000L);
        System.out.println("Datetime ..." + dateTime);

        return null;
    }
}
