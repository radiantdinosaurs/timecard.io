package main;

/**
 *
 * Provides Timestamps and converts Timestamps into usable numbers, like the amount of hours worked
 * @author Bethany Corder
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class TimeCardUtil {

    /**
     * Converts the TimeStamp of the employee's clock in/out times into a calculable list of hours.
     * @param list list of the hours worked
     * @param hoursWorkedTimeStamp TimeStamp for the employee's clock in/out times
     * @return ArrayList containing the hours the employee worked
     */
    public static void assignTimestampToArrayList(ArrayList<Double> list, Timestamp hoursWorkedTimeStamp) {
        double hoursWorked;
        //Converting the Timestamp into a Date object
        Date hoursWorkedDate = new Date(hoursWorkedTimeStamp.getTime());
        //Setting a format to change the Date object into a calculable number
        SimpleDateFormat format = new SimpleDateFormat("HH.mm");
        //Formatting the Date object and assigning it to a String
        String hoursWorkedString = format.format(hoursWorkedDate);
        //Changing the String into a Double so it may be added onto a calculable Double list
        hoursWorked = Double.parseDouble(hoursWorkedString);
        //Adding hoursWorked on an ArrayList
        list.add(hoursWorked);
    }

    /**
     * Calculates the total number of hours the employee worked.
     * @param inTime time that the employee clocked in
     * @param outTime time that the employee clocked out
     * @param totalHoursWorked total number of hours the employee worked
     * @return total number of hours the employee worked
     */
    public static double calculateTotalHours(ArrayList<Double> inTime, ArrayList<Double> outTime, double totalHoursWorked) {
        double z;
        for(int i = 0; i < inTime.size() && i<outTime.size(); i++) {
            double a = inTime.get(i);
            double b = outTime.get(i);
            z = b-a;
            totalHoursWorked = totalHoursWorked + z;
        }
        return totalHoursWorked;
    }

    /**
     * Gets the current time.
     * @return TimeStamp containing the current time
     */
    public static Timestamp getCurrentTimeStamp() {
        Date time = new Date();
        return new Timestamp(time.getTime());
    }
}
