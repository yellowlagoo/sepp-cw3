package src.Controller;

import src.Model.User;
import src.View.TextUserInterface;
import src.View.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for displaying and handling the main menu.
 * Delegates user actions to the appropriate sub-controller based on
 * the current user's role (guest, student, entertainment provider, or admin).
 */
public class MenuController extends Controller {

        private UserController userController;
        private BookingController bookingController;
        private EventPerformanceController eventPerformanceController;

        /**
         * Constructor for the MenuController class.
         * @param userController - the controller for user-related operations
         * @param bookingController - the controller for booking-related operations
         * @param eventPerformanceController - the controller for event and performance-related operations
         * @param view - the text-based user interface
         */
        public MenuController(UserController userController, BookingController bookingController,
                        EventPerformanceController eventPerformanceController, TextUserInterface view) {
                super(view);
                this.userController = userController;
                this.bookingController = bookingController;
                this.eventPerformanceController = eventPerformanceController;
        }

        /**
         * Runs the main menu loop. Continuously displays the appropriate menu
         * based on the current user's role and handles their selected option.
         * The loop runs until the application is terminated.
         */
        public void mainMenu() {
                boolean running = true;

                while (running == true) {

                        if (checkCurrentUserIsGuest()) {
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

        /**
         * Displays and handles the guest menu, which allows users to log in
         * or register as an entertainment provider.
         * On successful login, syncs the logged-in user across all controllers.
         * @return true to keep the main menu loop running
         */
        private boolean handleGuestMainMenu() {
                ArrayList<GuestMenuOptions> options = new ArrayList<>(List.of(GuestMenuOptions.values()));
                int option = getMenuOption(options, "Choose an option: ");

                switch (option) {
                        case 1:
                                userController.login();
                                try {
                                        User loggedIn = userController.getCurrentUser();
                                        this.setCurrentUser(loggedIn);
                                        eventPerformanceController.setCurrentUser(loggedIn);
                                        bookingController.setCurrentUser(loggedIn);
                                } catch (NullPointerException e) {
                                        // login failed, stay as guest
                                }
                                return true;
                        case 2:
                                userController.registerEntertainmentProvider();
                                return true;
                        default:
                                view.displayError("Invalid option. Please try again.");
                                return true;
                }
        }

        /**
         * Logs the current user out and clears the current user reference
         * across all controllers, returning the system to the guest state.
         */
        private void syncLogout() {
                userController.logout();
                this.setCurrentUser(null);
                userController.setCurrentUser(null);
                eventPerformanceController.setCurrentUser(null);
                bookingController.setCurrentUser(null);
        }

        /**
         * Displays and handles the student menu. Options include logging out,
         * searching for and viewing performances, reviewing performances,
         * editing preferences, booking performances, and cancelling bookings.
         * @return true to keep the main menu loop running
         */
        private boolean handleStudentMainMenu() {
                ArrayList<StudentMenuOptions> options = new ArrayList<>(List.of(StudentMenuOptions.values()));
                int option = getMenuOption(options, "Choose an option: ");

                switch (option) {

                        case 1:
                                syncLogout();
                                return true;

                        case 2:
                                eventPerformanceController.searchForPerformances();
                                return true;

                        case 3:
                                eventPerformanceController.viewPerformance();
                                return true;

                        case 4:
                                bookingController.reviewPerformance();
                                return true;

                        case 5:
                                userController.editPreferences();
                                return true;

                        case 6:
                                bookingController.bookPerformance();
                                return true;

                        case 7:
                                bookingController.cancelBooking();
                                return true;

                        default:
                                view.displayError("Invalid option. Please try again.");
                                return true;
                }

        }

        /**
         * Displays and handles the entertainment provider menu. Options include
         * logging out, searching for and viewing performances, creating events,
         * and cancelling performances.
         * @return true to keep the main menu loop running
         */
        private boolean handleEntertainmentProviderMainMenu() {
                ArrayList<EPMenuOptions> options = new ArrayList<>(List.of(EPMenuOptions.values()));
                int option = getMenuOption(options, "Choose an option: ");

                switch (option) {

                        case 1:
                                syncLogout();
                                return true;

                        case 2:
                                eventPerformanceController.searchForPerformances();
                                return true;

                        case 3:
                                eventPerformanceController.viewPerformance();
                                return true;

                        case 4:
                                eventPerformanceController.createEvent();
                                return true;

                        case 5:
                                eventPerformanceController.cancelPerformance();
                                return true;

                        default:
                                view.displayError("Invalid option. Please try again.");
                                return true;
                }

        }

        /**
         * Displays and handles the admin staff menu. Options include logging out,
         * searching for and viewing performances, and sponsoring performances.
         * @return true to keep the main menu loop running
         */
        private boolean handleAdminStaffMainMenu() {
                ArrayList<AdminMenuOptions> options = new ArrayList<>(List.of(AdminMenuOptions.values()));
                int option = getMenuOption(options, "Choose an option: ");

                switch (option) {

                        case 1:
                                syncLogout();
                                return true;

                        case 2:
                                eventPerformanceController.searchForPerformances();
                                return true;

                        case 3:
                                eventPerformanceController.viewPerformance();
                                return true;

                        case 4:
                                eventPerformanceController.sponsorPerformance();
                                return true;

                        default:
                                view.displayError("Invalid option. Please try again.");
                                return true;

                }

        }

}