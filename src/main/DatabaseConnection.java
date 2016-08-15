package main;
/**
 * A timecard system.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Bethany Corder
 */
public class DatabaseConnection {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/timecard";
    static final String USER = "root";
    static final String PASS = "SunbaeOppa25!";
    Connection conn = null;
    Statement stmt = null;

    public void loadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Connection openConnection() {
        System.out.println("Connecting to database...");
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Connected to the database!");
    return conn;
    }
}
