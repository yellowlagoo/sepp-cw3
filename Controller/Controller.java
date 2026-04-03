package controller;

import java.util.List;
import java.util.ArrayList;

import model.User;
import model.AdminStaff;
import model.EntertainmentProvider;
import view.View;

public abstract class Controller {
    private User currentUser;
    protected View view;

    public Controller(User currentUser, View view) {
        this.currentUser = currentUser;
        this.view = view;
    }

    public User getCurrentUser() {
        return currentUser;
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
        return currentUser.isLoggedIn() == false;
    }

    /**
     * Checks whether or not the current user is an admin staff
     * @return - whether or not the current user is an admin staff
     */
    protected boolean checkCurrentUserIsAdmin() {
        return currentUser instanceof AdminStaff;
    }

    /**
     * Checks whether or not the current user is a student
     * @return - whether or not the current user is a student
     */
    protected boolean checkCurrentUserIsStudent() {
        return currentUser instanceof Student;
    }

    /**
     * Checks whether or not the current user is an entertainment provider
     * @return - whether or not the current user is an entertainment provider
     */
    protected boolean checkCurrentUserIsEntertainmentProvider() {
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

    
    
}