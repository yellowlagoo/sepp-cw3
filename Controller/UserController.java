package controller;

import model.AdminStaff;
import model.EntertainmentProvider;
import model.Event;
import model.Model;
import model.Student;
import model.User;
import view.View;
import externalsystems.VerificationSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class UserController extends Controller {

    public static final String PREREGISTERED_USERS_FILE_PATH  = "preregistered_students.csv";
    public static final String PREREGISTERED_ADMIN_FILE_PATH  = "preregistered_admins.csv";

    private final VerificationSystem verificationSystem;

    public UserController(Model model, View view, VerificationSystem verificationSystem) {
        super(model, view);
        this.verificationSystem = verificationSystem;
    }

    public void login() {
        String email    = view.getInput("Enter email:");
        String password = view.getInput("Enter password:");

        User user = model.getUserByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            view.displayError("Invalid email or password. Please try again.");
            return;
        }

        currentUser = user;
        view.displaySuccess("Welcome back, " + getDisplayName(user) + "!");
    }

    /**
     * Clears the current session (sets currentUser to null).
     * MenuController propagates the null to all other controllers afterwards.
     */
    public void logout() {
        currentUser = null;
        view.displaySuccess("You have been logged out successfully.");
    }

    
    public void registerEntertainmentProvider() {
        System.out.println(" Register as Entertainment Provider ");

        String email          = view.getInput("Enter your email address:");
        String orgName        = view.getInput("Enter your organisation name:");
        String businessNumber = view.getInput("Enter your business registration number:");

        // Duplicate check (email, orgName, or businessNumber already in use)
        if (EPAccountAlreadyExists(email, orgName, businessNumber)) {
            view.displayError("An account with this email, organisation name, or business number already exists.");
            return;
        }

        // Verify the business registration number with the external service
        if (!verificationSystem.verifyEntertainmentProvider(businessNumber)) {
            view.displayError("Your business registration number could not be verified.");
            return;
        }

        String name        = view.getInput("Enter contact person name:");
        String password    = view.getInput("Create a password:");
        String description = view.getInput("Enter a short description of your organisation:");

        EntertainmentProvider ep = new EntertainmentProvider(
                email, password, orgName, businessNumber, name, description);
        addUser(ep);

        view.displaySuccess("Entertainment Provider account created for '" + orgName + "'!");
    }

    
    public void editPreferences() {
        if (!checkCurrentUserIsStudent()) {
            view.displayError("Only students can edit preferences.");
            return;
        }

        Student student = (Student) currentUser;
        System.out.println("Current preferences: " + student.getPreferences());
        System.out.println("Available categories: music, theatre, dance, movie, sports");

        String raw = view.getInput("Enter new preferences (comma-separated, e.g. music,dance):");

        if (student.getPreferences().updatePreferences(raw)) {
            view.displaySuccess("Preferences updated: " + student.getPreferences());
        } else {
            view.displayError("One or more preference values were not recognised. No changes made.");
        }
    }


    public void addPreregisteredUsers() {
        loadStudents();
        loadAdmins();
    }


    private void loadStudents() {
        
    }

    private void loadAdmins() {
        
    }

    /** Delegates duplicate-check to the model. */
    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        return model.epAccountAlreadyExists(email, orgName, businessNumber);
    }

    
    private void addUser(User user) {
        model.addUser(user);
    }

    
    public EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        for (User u : model.getUsers()) {
            if (u instanceof EntertainmentProvider) {
                EntertainmentProvider ep = (EntertainmentProvider) u;
                for (Event e : ep.getEvents()) {
                    if (e.getEventID() == eventNumber) return ep;
                }
            }
        }
        return null;
    }

}