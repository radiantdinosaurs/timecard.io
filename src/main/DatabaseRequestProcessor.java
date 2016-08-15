package main;
/**
 * A simple timecard system.
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

    public Connection openConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Welcome to MyTimeCard");
        return conn;
    }

    public String queryEmployeeID(String employeeID) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, manager FROM employees WHERE id = " + employeeID);
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
    return employeeID;
    }

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
                ResultSet timeCardResults = stmt.executeQuery("SELECT * FROM timecard_entries WHERE employee_id = " + employeeID);
                while(timeCardResults.next()) {
                    totalHours = 0;
                    Timestamp inTimestamp = timeCardResults.getTimestamp("in_time");
                    Timestamp outTimestamp = timeCardResults.getTimestamp("out_time");
                    c.assignTimestampToArrayList(inTime, inTimestamp);
                    c.assignTimestampToArrayList(outTime, outTimestamp);
                    totalHours = c.calculateTotalHours(inTime, outTime, totalHours);
                }
                System.out.println(employeeName + " (" + position + ") worked " + totalHours + " and earned $" + totalHours*wage);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void checkTimeCardStatus(String employeePassword, String employeeID) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT status, in_time FROM timecard_entries WHERE password = " + employeePassword + " ORDER BY in_time DESC LIMIT 1");
            while(rs.next()) {
                String isCheckedIn = rs.getString("status");
                if(isCheckedIn.equals("1")) {
                    System.out.println("Checking out...");
                    setCheckOutTime(employeePassword);
                }
                else if(isCheckedIn.equals("0")) {
                    System.out.println("Checking in...");
                    setCheckInTime(employeeID, employeePassword);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCheckInTime(String employeeID, String employeePassword) {
        try {
            stmt = conn.createStatement();
            boolean status = true;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO timecard_entries (employee_id, in_time, status, password, out_time) VALUES (?,?,?,?,?)");
            ps.setString(1, employeeID);
            ps.setTimestamp(2, c.getCurrentTimeStamp());
            ps.setBoolean(3, status);
            ps.setString(4, employeePassword);
            ps.setTimestamp(5, null);
            ps.execute();
            printCheckInTime(employeePassword);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printCheckInTime(String employeePassword) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(in_time) FROM timecard_entries WHERE password = " + employeePassword);
            while (rs.next()) {
                String checkInTime = rs.getString("MAX(in_time)");
                System.out.println("You have clocked in at " + checkInTime);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCheckOutTime(String employeePassword) {
        try {
            boolean status = false;
            stmt = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("UPDATE timecard_entries SET out_time = ?, status = ? WHERE password = " + employeePassword + " ORDER BY in_time DESC LIMIT 1");
            ps.setTimestamp(1, c.getCurrentTimeStamp());
            ps.setBoolean(2, status);
            ps.execute();
            printCheckOutTime(employeePassword);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printCheckOutTime(String employeePassword) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(in_time) FROM timecard_entries WHERE password = " + employeePassword);
            while (rs.next()) {
                String checkOutTime = rs.getString("MAX(in_time)");
                System.out.println("You have clocked out at " + checkOutTime);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
