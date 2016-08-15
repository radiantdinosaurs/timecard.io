package main;
/**
 * A simple timecard system.
 * @author Bethany Corder
 */

import java.util.Scanner;

public class InputHandler {

    public String promptForID() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your employee ID: ");
        String employeeID = input.nextLine();
        return employeeID;
    }

    public String promptForPassword() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your password: ");
        String employeePassword = input.nextLine();
        return employeePassword;
    }

    public String promptManager() {
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to clock or see the time report?");
        String managerChoice = input.nextLine();
        return managerChoice;
    }
}
