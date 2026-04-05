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

    protected User getCurrentUser() {
        if (currentUser == null) {
            throw new NullPointerException(errMsg);
        }
        return this.currentUser;
    }

    protected void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Checks whether or not the current user is a guest, meaning they are not
     * logged in yet
     * @return - whether or not the current user is a guest
     */
    protected boolean checkCurrentUserIsGuest() {
        return currentUser == null || currentUser.isLoggedIn() == false;
    }

    /**
     * Checks whether or not the current user is an admin staff
     * @return - whether or not the current user is an admin staff
     */
    protected boolean checkCurrentUserIsAdmin() {
        if (currentUser == null) {
            throw new NullPointerException(errMsg);
        }
        return currentUser instanceof AdminStaff;
    }

    /**
     * Checks whether or not the current user is a student
     * @return - whether or not the current user is a student
     */
    protected boolean checkCurrentUserIsStudent() {
        if (currentUser == null) {
            throw new NullPointerException(errMsg);
        }
        return currentUser instanceof Student;
    }

    /**
     * Checks whether or not the current user is an entertainment provider
     * @return - whether or not the current user is an entertainment provider
     */
    protected boolean checkCurrentUserIsEntertainmentProvider() {
        if (currentUser == null) {
            throw new NullPointerException(errMsg);
        }
        return currentUser instanceof EntertainmentProvider;
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

    public String getErrMsg() {
        return this.errMsg;
    }
    
}