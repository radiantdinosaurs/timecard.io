package main;
/**
 * A simple timecard system.
 * @author Bethany Corder
 */

public class Runner {
    public static void main(String[] args) {
        InputHandler ih = new InputHandler();
        DatabaseRequestProcessor drp = new DatabaseRequestProcessor();
        String employeeID, employeePassword, managerChoice;
        String isManager = null;
        boolean resume = true;
        drp.openConnection();

        while (resume) {
            employeeID = drp.queryEmployeeID(ih.promptForID());
            isManager = drp.checkIfManager(isManager, employeeID);
            if (isManager.equals("1")) {
                managerChoice = (ih.promptManager());
                if (managerChoice.equalsIgnoreCase("time report")) {
                    drp.queryTimeReport();
                    continue;
                }
            }
            employeePassword = ih.promptForPassword();
            drp.checkTimeCardStatus(employeePassword, employeeID);
        }
    }
}