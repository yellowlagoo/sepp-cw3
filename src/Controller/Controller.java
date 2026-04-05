package src.Controller;

import src.Model.*;
import src.View.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Controller {
    private User currentUser;
    protected TextUserInterface view;
    private String errMsg = "Please set the current user before accessing it's attributes.";
    
    public Controller(TextUserInterface view) {
        this.view = view;
        currentUser = null;
    }

    public User getCurrentUser() {
        try {
            return this.currentUser;
        } catch (NullPointerException e) {
            view.displayError(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Checks whether or not the current user is a guest, meaning they are not
     * logged in yet
     * @return - whether or not the current user is a guest
     */
    protected boolean checkCurrentUserIsGuest() {
        try {
            return currentUser.isLoggedIn() == false;
        } catch (NullPointerException e) {
            view.displayError(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * Checks whether or not the current user is an admin staff
     * @return - whether or not the current user is an admin staff
     */
    protected boolean checkCurrentUserIsAdmin() {
        try {
            return currentUser instanceof AdminStaff;
        } catch (NullPointerException e) {
            view.displayError(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * Checks whether or not the current user is a student
     * @return - whether or not the current user is a student
     */
    protected boolean checkCurrentUserIsStudent() {
        try {
            return currentUser instanceof Student;
        } catch (NullPointerException e) {
            view.displayError(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * Checks whether or not the current user is an entertainment provider
     * @return - whether or not the current user is an entertainment provider
     */
    protected boolean checkCurrentUserIsEntertainmentProvider() {
        try {
            return currentUser instanceof EntertainmentProvider;
        } catch (NullPointerException e) {
            view.displayError(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
    }

    protected <T> int getMenuOption(List<T> options, String prompt) {
        List<T> menuOptions = new ArrayList<>(options);

        System.out.println(prompt);

        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println((i + 1) + ". " + menuOptions.get(i).toString());

        }

        String option = view.getInput("Enter option number: ");
        return Integer.parseInt(option);
    
    }

    
    
}