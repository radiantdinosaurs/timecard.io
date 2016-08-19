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
        // ANDREW:
        // TODO: when working with streaming classes like Database classes, make sure to close the
        // connection when you're done with it. This frees up memory and is important on systems that service large
        // numbers of users. Since you're using the Java 8 JDK, you can look into having that handled for you using
        // "try-using" statements.
        drp.openConnection();
        boolean error = false;

        // ANDREW:
        // TODO: there's a lot of control flow nesting here, split it up so it's easier to read
        // you may want to separate out nested if-else statements into their own methods with the required params
        // as the method arguments. Additionally, you can re-architect the entire flow of the app so the logic is
        // flatter (less nesting). In many programs, this is attributed to how the errors are handled.

        do {
            //Prompting the user to input their employee ID
            employeeID = ih.promptForID();
            if (employeeID == null || employeeID.isEmpty()) {
                // ANDREW:
                // TODO: consider changing the name of the boolean
                // for some, it's advised to make boolean variables named after what they represent
                // e.g. isValidEmployeeId. So, you'll have something like if(isValidEmployeeId) { ... }
                // There's a lot of debate about good naming vs. comments. The usual conclusion is follow the middle path.

                //Updating flag if their input is null or empty
                error = true;
                System.out.println("That's not a valid employee ID.");
            } else {
                // ANDREW
                // TODO: consider not recycling the error variable
                // if you want to have a variable to control the loop, it's more prudent to keep a separate variable.
                // this is a minor example of what's called "tight-coupling" which makes it more difficult to
                // refactor and re-architect existing code
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
                            // ANDREW:
                            // TODO: the manager may not necessarily want to check in or check out
                            // they may just want to view the time cards alone
                            // allow them to choose an action again or start from the user ID step again

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

                // ANDREW:
                // TODO: instead of relying on an error flag and the continue keyword, consider just using break
                // in this case, the naming of the variable as error is very intuitive. It makes one think of
                // an error instead of just "resuming the loop" or "repeating"
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