package main;
/**
 * A simple time card system.
 * @author Bethany Corder
 */
import java.sql.*;
import java.util.ArrayList;

public class DatabaseRequestProcessor {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/timecard";
    static final String USER = "root";
    static final String PASS = "SunbaeOppa25!";
    Connection conn = null;
    Statement stmt = null;
    Calculator c = new Calculator();

    /**
     * Opens a connection to the database.
     * @return connection to the database
     */
    public Connection openConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    /**
     * Makes a query using the user's employee ID to find their name and managerial status. After
     * querying, it welcomes the employee by printing out their name.
     * @param error flag used to check for errors
     * @param employeeID user's ID
     * @return flag used to check for errors
     */
    public boolean queryEmployeeID(boolean error, String employeeID) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, manager FROM employees WHERE id = " + employeeID);
            if(!rs.next()) {
                //Updating the flag if the user's ID returned an empty ResultSet
                error = true;
            }
            while (rs.next()) {
                String name = rs.getString("name");
                String isManager = rs.getString("manager");
                if(isManager.equals("1")) {
                    System.out.println("Welcome, Manager " + name);
                }
                else if(isManager.equals("0")) {
                    System.out.println("Welcome, " + name);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    return error;
    }

    /**
     * Checks if the user is a manager.
     * @param isManager contains "1" or "0" depending on the user's managerial status
     * @param employeeID user's ID
     * @return contains "1" or "0" depending on the user's managerial status
     */
    public String checkIfManager(String isManager, String employeeID) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT manager FROM employees WHERE id = " + employeeID);
            while (rs.next()) {
                isManager = rs.getString("manager");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return isManager;
    }

    /**
     * Creates a query for all of the employees' hours worked, earnings, and position.
     */
    public void queryTimeReport() {
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
            double totalHours = 0;
            while(rs.next()) {
                ArrayList<Double> inTime = new ArrayList<>();
                ArrayList<Double> outTime = new ArrayList<>();
                String employeeID = rs.getString("id");
                String employeeName = rs.getString("name");
                String position = rs.getString("position");
                double wage = rs.getDouble("wage");
                ResultSet timeCardResults = stmt.executeQuery("SELECT * FROM timecard_entries WHERE employee_id = "
                        + employeeID);
                while(timeCardResults.next()) {
                    totalHours = 0;
                    Timestamp inTimestamp = timeCardResults.getTimestamp("in_time");
                    Timestamp outTimestamp = timeCardResults.getTimestamp("out_time");
                    c.assignTimestampToArrayList(inTime, inTimestamp);
                    c.assignTimestampToArrayList(outTime, outTimestamp);
                    //Using the Calculator class to find out how many hours the employee worked
                    totalHours = c.calculateTotalHours(inTime, outTime, totalHours);
                }
                System.out.println(employeeName + " (" + position + ") worked " + totalHours + " and earned $"
                        + totalHours*wage);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks if the user's password/ID combination exists on the database.
     * @param error flag used to check for errors
     * @param employeePassword user's password
     * @param employeeID user's ID
     * @return flag used to check for errors
     */
    public boolean checkTimeCardStatus(boolean error, String employeePassword, String employeeID) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT status, in_time FROM timecard_entries WHERE password = "
                    + employeePassword + " ORDER BY in_time DESC LIMIT 1");
            if(!rs.next()) {
                //Updating the flag if the user's password/ID combination returned an empty ResultSet
                error = true;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return error;
    }

    /**
     * Queries the database to check if the user is clocked in or out, and then updates the time card depending on the
     * status.
     * @param employeePassword user's password
     * @param employeeID user's ID
     */
    public void updateTimeCard(String employeePassword, String employeeID) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT status, in_time FROM timecard_entries WHERE password = "
                    + employeePassword + " ORDER BY in_time DESC LIMIT 1");
            while(rs.next()) {
                String isClockedIn = rs.getString("status");
                //Checking if the user is currently clocked in
                if(isClockedIn.equals("1")) {
                    System.out.println("Checking out...");
                    //Since the user is clocked in, they can only be clocked out
                    setClockOutTime(employeePassword);
                }
                //Checking if the user is currently clocked out
                else if(isClockedIn.equals("0")) {
                    System.out.println("Checking in...");
                    //Since the user is clocked out, they can only be clocked in
                    setClockInTime(employeeID, employeePassword);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the current time and inserts it into the database as the user's clock-in time.
     * @param employeeID user's ID
     * @param employeePassword user's password
     */
    public void setClockInTime(String employeeID, String employeePassword) {
        try {
            stmt = conn.createStatement();
            boolean status = true;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO timecard_entries " +
                    "(employee_id, in_time, status, password, out_time) VALUES (?,?,?,?,?)");
            ps.setString(1, employeeID);
            ps.setTimestamp(2, c.getCurrentTimeStamp());
            ps.setBoolean(3, status);
            ps.setString(4, employeePassword);
            ps.setTimestamp(5, null);
            ps.execute();
            //Printing off the clock-in time to the user
            printClockInTime(employeePassword);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Prints the clock-in time to the user.
     * @param employeePassword user's password
     */
    public void printClockInTime(String employeePassword) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(in_time) FROM timecard_entries WHERE password = "
                    + employeePassword);
            while (rs.next()) {
                String checkInTime = rs.getString("MAX(in_time)");
                System.out.println("You have clocked in at " + checkInTime);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the current time and updates the database with it as the user's clock-out time.
     * @param employeePassword user's password
     */
    public void setClockOutTime(String employeePassword) {
        try {
            boolean status = false;
            stmt = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("UPDATE timecard_entries SET out_time = ?, status = ? " +
                    "WHERE password = " + employeePassword + " ORDER BY in_time DESC LIMIT 1");
            ps.setTimestamp(1, c.getCurrentTimeStamp());
            ps.setBoolean(2, status);
            ps.execute();
            //Printing the clock-out time to the user
            printClockOutTime(employeePassword);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Prints the clock-out time to the user.
     * @param employeePassword user's password
     */
    public void printClockOutTime(String employeePassword) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(in_time) FROM timecard_entries WHERE password = " +
                    employeePassword);
            while (rs.next()) {
                String checkOutTime = rs.getString("MAX(in_time)");
                System.out.println("You have clocked out at " + checkOutTime);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
