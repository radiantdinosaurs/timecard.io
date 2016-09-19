package main;

/**
 *
 * @author Bethany Corder
 */
public final class TimeCardContract {

    static final String DB_URL = "jdbc:mysql://localhost:3306/timecard";
    static final String USER = "root";
    static final String PASS = "SunbaeOppa25!";

    public static final class EmployeesTable {
        public static String table_name = "employees";
        public static String column_id = "id";
        public static String manager = "manager";
        public static String position = "position";
        public static String wage = "wage";
    }

    public static final class TimeCardEntriesTable {
        public static String table_name = "timecard_entries";
        public static String column_id = "employee_id";
        public static String password = "password";
        public static String status = "status";
        public static String in_time = "in_time";
        public static String out_time = "out_time";
    }

}
