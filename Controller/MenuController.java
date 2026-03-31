package Controller;

import Model.User;

public class MenuController extends Controller {

    public MenuController(User currentUser) {
        super(currentUser);
    }

    public void mainMenu() {
        if (checkCurrentUserIsAdmin()) {
            handleAdminStaffMainMenu();
        } else if (checkCurrentUserIsEntertainmentProvider()) {
            handleEntertainmentProviderMainMenu();
        } else if (checkCurrentUserIsGuest()) {
            handleGuestMainMenu();
        } else if (checkCurrentUserIsStudent()) {
            handleStudentMainMenu();
        }

    }

    // Implement after use cases ?
    private boolean handleGuestMainMenu() {
        return false;
    }

    private boolean handleStudentMainMenu() {
        return false;
    }

    private boolean handleEntertainmentProviderMainMenu() {
        return false;
    }

    private boolean handleAdminStaffMainMenu() {
        return false;
    }

}