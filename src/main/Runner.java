package main;
/**
 * A simple time card system.
 * @author Bethany Corder
 */
public class Runner {

    /**
     * Prompts an infinite loop for updating time cards.
     */
    public static void main(String[] args) {
        boolean resume = true;
        while(resume) {
            System.out.println("Welcome to MyTimeCard!");
            runProgram();
        }
    }

    /**
     * Prompts the user to enter their employee ID. Checks if the user is a manager, then directs to the proper
     * outlet. After handling any manager-related tasks, it prompts for the user's password to update their
     * time card.
     */
    public static void runProgram() {
        InputHandler ih = new InputHandler();
        DatabaseRequestProcessor drp = new DatabaseRequestProcessor();
        String employeePassword, employeeID;
        drp.openConnection();
        boolean error = false;

        do {
            //Prompting the user to input their employee ID
            employeeID = ih.promptForID();
            if (employeeID == null || employeeID.isEmpty()) {
                //Updating flag if their input is null or empty
                error = true;
                System.out.println("That's not a valid employee ID.");
            } else {
                error = false;
                //Querying for the employee's name
                error = drp.queryEmployeeID(error, employeeID);
                if (error) {
                    //Printing if the user's ID didn't return a result from the query
                    System.out.println("We couldn't find you in the database.");
                } else {
                    //Sending off to check if user is a manager and, if so, perform managerial tasks
                    managerCheckpoint(employeeID);
                    do {
                        //Prompting for password to update time card
                        employeePassword = ih.promptForPassword();
                        if(employeePassword == null || employeePassword.isEmpty()) {
                            //Updating flag if input is null or empty
                            error = true;
                            System.out.println("Not a valid password.");
                        }
                        else {
                            error = false;
                            //Checking if password/ID combination exists in the database
                            error = drp.checkTimeCardStatus(error, employeePassword, employeeID);
                            if (error) {
                                //Printing if password/ID combination didn't return a result from the query
                                System.out.println
                                        ("We couldn't find you in the database. Try entering your password again.");
                            }
                            else {
                                //Updating the time card
                                drp.updateTimeCard(employeePassword, employeeID);
                            }
                        }
                    } while(error == true);
                }
            }
        } while(error == true);
    }

    /**
     * Checks if the user is a manager. If the user is a manager, it will prompt them to check the time report or
     * update their time card.
     * @param employeeID user's employee ID
     */
    public static void managerCheckpoint(String employeeID) {
        InputHandler ih = new InputHandler();
        DatabaseRequestProcessor drp = new DatabaseRequestProcessor();
        String managerChoice;
        String isManager = null;
        boolean error = false;
        drp.openConnection();

        isManager = drp.checkIfManager(isManager, employeeID);
        //Checking if the user is a manager
        if(isManager.equals("1")) {
            do {
                //Prompting the manager to make a choice between seeing the time report or updating their time card
                managerChoice = ih.promptManager();
                if(managerChoice.equalsIgnoreCase("time report")) {
                    error = false;
                    //Showing the manager the time report
                    drp.queryTimeReport();
                }
                else if(managerChoice.contains("update")) {
                    error = false;
                    //Continuing if the manager wants to update their time card
                    continue;
                }
                else {
                    //Updating flag if their input was null, empty, or if they typed in something other than
                    //"time report" or "update time card"
                    error = true;
                    System.out.println("I don't understand. Please type 'update' or 'time report.'");
                }
            } while(error == true);
        }
    }
}