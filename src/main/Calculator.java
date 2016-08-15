package main;
/**
 * A simple timecard system.
 * @author Bethany Corder
 */

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Calculator {

    public ArrayList<Double> assignTimestampToArrayList(ArrayList<Double> list, Timestamp hoursWorkedTimestamp) {
        double hoursWorked;
        Date hoursWorkedDate = new Date(hoursWorkedTimestamp.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH.mm");
        String hoursWorkedString = format.format(hoursWorkedDate);
        hoursWorked = Double.parseDouble(hoursWorkedString);
        list.add(hoursWorked);
        return list;
    }

    public double calculateTotalHours(ArrayList<Double> inTime, ArrayList<Double> outTime, double totalHoursWorked) {
        double z;
        for(int i = 0; i < inTime.size() && i<outTime.size(); i++) {
            double a = inTime.get(i);
            double b = outTime.get(i);
            z = b-a;
            totalHoursWorked = totalHoursWorked + z;
        }
        return totalHoursWorked;
    }

    public static Timestamp getCurrentTimeStamp() {
        Date time = new Date();
        return new Timestamp(time.getTime());
    }
}
