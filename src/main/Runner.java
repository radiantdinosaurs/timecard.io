package main;
/**
 * TODO: write unit tests for as much of your code as possible. Do research on your own to see why developers should
 * write unit tests. I've added a tests folder to your project root. You may need to mark the folder as a test folder
 * in your own project.
 * https://www.jetbrains.com/help/idea/2016.2/configuring-content-roots.html#d2022619e241
 *
 * A simple time card system.
 * @author Bethany Corder
 */
public class Runner {
    /**
     * Prompts an infinite loop for updating time cards.
     */
    public static void main(String[] args) {
        // ANDREW:
        // TODO: not mandatory, but it'd be nice to have a way to exit this program
        // As a developer, it's not important, but you should build your programs with the user in mind.
        // Suggestion - make runProgram return an int. -1 means to terminate, 0 means to resume, etc. Users can enter
        // this at any given point. It might help to let them know in the prompt text.
        boolean resume = true;
        while(resume) {
            System.out.println("Welcome to MyTimeCard! Press 'q' to quit.");
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
        boolean isValidEmployeeID = true;
        boolean doesEmployeeIDExistInDatabase = true;

        do {
            employeeID = ih.promptForID();
            if (employeeID == null || employeeID.isEmpty()) {
                isValidEmployeeID = false;
                System.out.println("That's not a valid employee ID.");
            } else {
                isValidEmployeeID = true;
                doesEmployeeIDExistInDatabase = drp.queryEmployeeID(employeeID);
                if (!doesEmployeeIDExistInDatabase) {
                    //Printing if the user's ID didn't return a result from the query
                    isValidEmployeeID = false;
                    System.out.println("We couldn't find you in the database.");
                } else {
                    //Sending off to check if user is a manager and, if so, perform managerial tasks
                    managerCheckpoint(employeeID);
                    employeePassword = ih.promptForPassword();
                    passwordCheckpoint(employeeID, employeePassword);
                }
            }
        } while(!isValidEmployeeID);
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

        //Checking if the user is a manager
        isManager = drp.checkIfManager(isManager, employeeID);
        if(isManager.equals("1")) {
            do {
                //Prompting the manager to make a choice between seeing the time report or updating their time card
                managerChoice = ih.promptManager();
                if(managerChoice.equalsIgnoreCase("time report")) {
                    error = false;
                    drp.queryTimeReport();
                    Runner.main(null);
                }
                else if(managerChoice.contains("update")) {
                    break;
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

    public static void passwordCheckpoint(String employeeID, String employeePassword) {
        boolean isValidPassword = true;
        boolean doesIDAndPasswordExistInDatabase = true;
        DatabaseRequestProcessor drp = new DatabaseRequestProcessor();

        //Getting a password from the user
        do {
            if(employeePassword == null || employeePassword.isEmpty()) {
                isValidPassword = false;
                System.out.println("Not a valid password.");
            } else {
                isValidPassword = true;
                doesIDAndPasswordExistInDatabase = drp.checkIDAndPassword(employeeID, employeePassword);
                if (!doesIDAndPasswordExistInDatabase) {
                    System.out.println
                            ("We couldn't find you in the database. Try entering your password again.");
                } else {
                    drp.updateTimeCard(employeePassword, employeeID);
                }
            }
        } while(!isValidPassword);

    }
}