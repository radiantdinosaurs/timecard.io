package main;

/**
 * Handles any input
 * @author Bethany Corder
 */
import java.util.Scanner;

public class InputHandler {

    private Scanner input = new Scanner(System.in);

    public String promptForID() {
        System.out.println("Please enter your employee ID: ");
        String employeeID = input.nextLine();
        return employeeID;
    }

    /**
     * Prompts the user to input their password.
     * @return the user's password
     */
    public String promptForPassword() {
        System.out.println("Please enter your password, or 'quit' to exit: ");
        String employeePassword = input.nextLine();
        return employeePassword;
    }

    /**
     * Prompts managers to make a choice between updating their time card or seeing the time report.
     * @return String containing the manager's input
     */
    public String promptManager() {
        System.out.println("Would you like to update your time card or see the time report?");
        String managerChoice = input.nextLine();
        return managerChoice;
    }
}