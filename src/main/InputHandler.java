package main;
/**
 * A simple time card system.
 * @author Bethany Corder
 */
import java.util.Scanner;

public class InputHandler {
    // TODO: remember to close the Scanner when you're done with it. In these cases, it's before the return statement.
    /**
     * Prompts the user to input their employee ID.
     * @return the user's employee ID
     */
    public String promptForID() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your employee ID: ");
        String employeeID = input.nextLine();
        return employeeID;
    }

    /**
     * Prompts the user to input their password.
     * @return the user's password
     */
    public String promptForPassword() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your password: ");
        String employeePassword = input.nextLine();
        return employeePassword;
    }

    /**
     * Prompts managers to make a choice between updating their time card or seeing the time report.
     * @return String containing the manager's input
     */
    public String promptManager() {
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to update your time card or see the time report?");
        String managerChoice = input.nextLine();
        return managerChoice;
    }
}