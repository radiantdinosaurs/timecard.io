package main;
/**
 * ANDREW:
 * TODO: this description describes the entire project, but it's supposed to describe only this class.
 *
 * A simple time card system.
 * @author Bethany Corder
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
// ANDREW:
// TODO: the name of this class is misleading. Consider a name like TimeCardUtil.
public class Calculator {

    // ANDREW:
    // TODO: the methods in this class do not rely on member variables or leveraging the benefits of instantiation.
    // Consider making the methods static and making this an abstract class to prevent instantiation.
    // This is common known as a utility pattern.

    /**
     * Converts the TimeStamp of the employee's clock in/out times into a calculable list of hours.
     * @param list list of the hours worked
     * @param hoursWorkedTimeStamp TimeStamp for the employee's clock in/out times
     * @return ArrayList containing the hours the employee worked
     */
    public ArrayList<Double> assignTimestampToArrayList(ArrayList<Double> list, Timestamp hoursWorkedTimeStamp) {
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

        // ANDREW:
        // TODO: since this method is already making operations to an instance of ArrayList, you don't need to return
        // the modified array. It will already be modified. If you wanted to leverage returns, you would return the
        // hoursWorked instead
        return list;
    }

    /**
     * Calculates the total number of hours the employee worked.
     * @param inTime time that the employee clocked in
     * @param outTime time that the employee clocked out
     * @param totalHoursWorked total number of hours the employee worked
     * @return total number of hours the employee worked
     */
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

    // ANDREW:
    // TODO: when accessing static members, you don't instantiate the class.
    // Usage: Calculator.getCurrentTimeStamp()
    // To enforce this, you can prevent instantiation of a class by using a private constructor or making it abstract.
    /**
     * Gets the current time.
     * @return TimeStamp containing the current time
     */
    public static Timestamp getCurrentTimeStamp() {
        Date time = new Date();
        return new Timestamp(time.getTime());
    }
}
