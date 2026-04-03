package controller;

import java.util.List;

import model.User;

public class MenuController extends Controller {

    private UserController userController;
    private BookingController bookingController;
    private EventPerformanceController eventPerformanceController;


    public MenuController(User currentUser, UserController userController, BookingController bookingController, EventPerformanceController eventPerformanceController, View view) {
        super(currentUser, view);
        this.userController = userController;
        this.bookingController = bookingController;
        this.eventPerformanceController = eventPerformanceController;
    }

    public void mainMenu() {
        boolean running = true;

        while (running == true) {

        if (checkCurrentUserIsGuest()){
            running = handleGuestMainMenu();
        }

        else if (checkCurrentUserIsStudent()) {
            running = handleStudentMainMenu();
        }

        else if (checkCurrentUserIsEntertainmentProvider()) {
            running = handleEntertainmentProviderMainMenu();
        }

        else if (checkCurrentUserIsAdmin()) {
            running = handleAdminStaffMainMenu();
        }

    }
}

    // Implement after use cases ?
    private boolean handleGuestMainMenu() {
        List<GuestMenuOptions> options = List.of(GuestMenuOptions.values());
        int option = getMenuOption(options, "Choose an option: ");

        switch (option) {

            case 1: userController.login();
                return true;

            case 2: userController.registerEntertainmentProvider();
                return true;
            
            default: view.displayError("Invalid option. Please try again.");
                return true;
        }
        


    }

    private boolean handleStudentMainMenu() {
        List<StudentMenuOptions> options = List.of(StudentMenuOptions.values());
        int option = getMenuOption(options, "Choose an option: ");

        switch (option) {

            case 1: userController.logout();
                    return false;

            case 2: eventPerformanceController.searchForPerformance();
                    return true;

            case 3: eventPerformanceController.viewPerformance();
                    return true;

            case 4: bookingController.reviewPerformance();
                    return true;

            case 5: userController.editPreferences();
                    return true;

            case 6: bookingController.bookPerformance();
                    return true;

            case 7: bookingController.cancelBooking();
                    return true;

            default: view.displayError("Invalid option. Please try again.");
                    return true;
        }

    }

    private boolean handleEntertainmentProviderMainMenu() {
        List<EPMenuOptions> options = List.of(EPMenuOptions.values());
        int option = getMenuOption(options, "Choose an option: ");

        switch (option) {

            case 1: userController.logout();
                    return false;
                
            case 2: eventPerformanceController.searchForPerformance();
                    return true;

            case 3: eventPerformanceController.viewPerformance();
                    return true;

            case 4: eventPerformanceController.createEvent();
                    return true;

            case 5: eventPerformanceController.cancelPerformance();
                    return true;

            default: view.displayError("Invalid option. Please try again.");
                    return true;
        }
        
    }

    private boolean handleAdminStaffMainMenu() {
        List<AdminMenuOptions> options = List.of(AdminMenuOptions.values());
        int option = getMenuOption(options, "Choose an option: ");

        switch (option) {

            case 1: userController.logout();
                    return false;

            case 2: eventPerformanceController.searchForPerformance();
                    return true;

            case 3: eventPerformanceController.viewPerformance();
                    return true;

            case 4: eventPerformanceController.sponsorPerformance();
                    return true;
            
            default: view.displayError("Invalid option. Please try again.");
                    return true;

        }
        
    }

}