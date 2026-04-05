package src.Controller;

import src.Model.*;
import src.View.*;
import src.ExternalSystems.MockVerificationSystem;

import java.io.*;
import java.util.*;

/**
 * Controller responsible for user-related operations including login, logout,
 * registration of entertainment providers, and editing student preferences.
 */
public class UserController extends Controller {

    public static final String PREREGISTERED_USERS_FILE_PATH = "preregistered_students.csv";
    public static final String PREREGISTERED_ADMIN_FILE_PATH = "preregistered_admins.csv";

    private final MockVerificationSystem verificationSystem;
    private Collection<User> users;

    /**
     * Constructor for the UserController class.
     * @param view - the text-based user interface
     * @param verificationSystem - the external system used to verify entertainment providers
     */
    public UserController(TextUserInterface view, MockVerificationSystem verificationSystem) {
        super(view);
        this.verificationSystem = verificationSystem;
        this.users = new ArrayList<>();
    }

    /**
     * Prompts the user for email and password, then attempts to log them in
     * by checking against preregistered student and admin CSV files.
     */
    public void login() {
        String email = view.getInput("Enter email:");
        if (email == null || email.trim().isEmpty()) {
            view.displayError("Email can't be empty");
            return;
        }

        String password = view.getInput("Enter password:");
        if (password == null || password.trim().isEmpty()) {
            view.displayError("Password can't be empty");
            return;
        }

        String readForStudent = this.readFileForUser(PREREGISTERED_USERS_FILE_PATH, email, password);
        String notFound = "User not found";
        if (readForStudent.equals(notFound)) {
            String readForAdmin = this.readFileForUser(PREREGISTERED_ADMIN_FILE_PATH, email, password);
            if (readForAdmin.equals(notFound)) {
                readForAdmin = "This user is not preregistered.";
            }
            displayMessaging(readForAdmin);
        } else {
            displayMessaging(readForStudent);
        }
    }

    /**
     * Displays the result of a login attempt as either a success or error message.
     * @param readResult - the result string from the file reading process
     */
    private void displayMessaging(String readResult) {
        if (readResult.contains("successful")) {
            view.displaySuccess(readResult);
        } else {
            view.displayError(readResult);
        }
    }

    /**
     * Reads a CSV file to find a user with the given email and password.
     * On success, sets the current user and marks them as logged in.
     * @param fileName - the path to the CSV file to read
     * @param email - the email to search for
     * @param password - the password to verify
     * @return a result string indicating success or the type of failure
     */
    private String readFileForUser(String fileName, String email, String password) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            List<String> elements = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                elements = Arrays.asList((line.trim()).split(","));
                this.validate(elements, fileName);
                String parsedEmail = elements.get(0);
                String parsedPassword = elements.get(1);
                if (parsedEmail.equals(email)) {
                    if (parsedPassword.equals(password)) {
                        if (fileName.equals(PREREGISTERED_USERS_FILE_PATH)) {
                            this.setCurrentUser(new Student(email, password));
                        } else if (fileName.equals(PREREGISTERED_ADMIN_FILE_PATH)) {
                            this.setCurrentUser(new AdminStaff(email, password));
                        } else {
                            return "Error parsing the file: unrecognized file " + fileName + ".";
                        }
                        this.getCurrentUser().setLoggedIn(true);
                        return "User exists: login successful";
                    } else {
                        view.displayError("Password is incorrect.");
                        return "User exists: incorrect password";
                    }
                }
            }
            br.close();
            return "User not found";
        } catch (FileNotFoundException e) {
            return "Error parsing file: the file " + fileName + " was not found.";
        } catch (IOException e) {
            return "Error parsing file: problem reading a line in the file " + fileName + ".";
        } catch (IllegalArgumentException e) {
            return "Error parsing file: malformed line in the file " + fileName + ", ensure all lines follow the format <email>, <password>.";
        } catch (NullPointerException e) {
            return "Error parsing file: there is an empty line in the file " + fileName + ".";
        }
    }

    /**
     * Logs the current user out by setting their logged-in status to false
     * and clearing the current user reference.
     */
    public void logout() {
        try {
            super.getCurrentUser().setLoggedIn(false);
            super.setCurrentUser(null);
            view.displaySuccess("You have been logged out successfully.");
        } catch (NullPointerException e) {
            view.displayError("No user is logged in.");
        }
    }

    /**
     * Guides the user through registering as an entertainment provider.
     * Checks for duplicate accounts and verifies the business registration
     * number with the external verification system before creating the account.
     */
    public void registerEntertainmentProvider() {
        System.out.println(" Register as Entertainment Provider ");

        String email = view.getInput("Enter your email address:");
        String orgName = view.getInput("Enter your organisation name:");
        String businessNumber = view.getInput("Enter your business registration number:");

        try {
            if (this.EPAccountAlreadyExists(email, orgName, businessNumber)) {
                view.displayError("An account with this email, organisation name, or business number already exists.");
                return;
            }
        } catch (IllegalArgumentException e) {
            view.displayError("One of the fields is empty: email, organisation name, and business registration number. Please try again.");
            return;
        }

        if (!verificationSystem.verifyEntertainmentProvider(businessNumber)) {
            view.displayError("Your business registration number could not be verified.");
            return;
        }

        String name = view.getInput("Enter contact person name:");
        String password = view.getInput("Create a password:");
        String description = view.getInput("Enter a short description of your organisation:");

        EntertainmentProvider ep = new EntertainmentProvider(
                email, password, orgName, businessNumber, name, description);
        this.addUser(ep);

        view.displaySuccess("Entertainment Provider account created for '" + orgName + "!");
    }

    /**
     * Allows a student to edit their event preferences.
     * Only accessible to users who are students.
     */
    public void editPreferences() {
        if (!super.checkCurrentUserIsStudent()) {
            view.displayError("Only students can edit preferences.");
            return;
        }

        Student student = (Student) super.getCurrentUser();
        StudentPreferences preferences = student.getPreferences();

        System.out.println("Current preferences: " + preferences.toString());
        System.out.println("Available categories: music, theatre, dance, movie, sports.");

        String raw = view.getInput("Enter new preferences (comma-separated, e.g. music, dance):");

        if (preferences.updatePreferences(raw)) {
            view.displaySuccess("Preferences updated: " + preferences.toString());
        } else {
            view.displayError("An error occurred while updating preferences.");
        }
    }

    /**
     * Loads all preregistered students and admins from their respective
     * CSV files into the users collection.
     */
    private void addPreregisteredUsers() {
        boolean loadSuccess = this.load(PREREGISTERED_USERS_FILE_PATH, 0) && this.load(PREREGISTERED_ADMIN_FILE_PATH, 1);
        if (!loadSuccess) {
            view.displayError("There was an error parsing the preregistered user files, check the error logs");
        }
    }

    /**
     * Reads a CSV file and creates User objects for each line.
     * @param fileName - the path to the CSV file
     * @param type - 0 for students, 1 or above for admin staff
     * @return true if the file was loaded successfully, false otherwise
     */
    private boolean load(String fileName, int type) {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                List<String> elements = Arrays.asList((line.trim()).split(","));
                this.validate(elements, fileName);

                User parsed;
                if (type > 0) parsed = new AdminStaff(elements.get(0), elements.get(1));
                else parsed = new Student(elements.get(0), elements.get(1));
                this.addUser(parsed);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Validates that a parsed CSV line has exactly two elements.
     * @param elements - the parsed elements from the CSV line
     * @param fileName - the file being parsed, used in error messages
     * @throws NullPointerException if elements is null or empty
     * @throws IllegalArgumentException if the line does not have exactly two elements
     */
    private void validate(List<String> elements, String fileName) {
        if (elements == null || elements.isEmpty()) {
            throw new NullPointerException("This line is empty in the file + " + fileName + ".");
        }
        if (elements.size() != 2) {
            throw new IllegalArgumentException("The line is malformed. Ensure the line follows the structure : <email>, <password>. File: " + fileName);
        }
    }

    /**
     * Checks whether an entertainment provider account already exists
     * with the given email, organisation name, and business number.
     * @param email - the email to check
     * @param orgName - the organisation name to check
     * @param businessNumber - the business registration number to check
     * @return true if a matching account exists, false otherwise
     * @throws IllegalArgumentException if any parameter is null
     */
    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        if (email == null || orgName == null || businessNumber == null) {
            throw new IllegalArgumentException("All fields must be non-empty.");
        }
        for (User user : this.getUsers()) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider checkEP = (EntertainmentProvider) user;
                if (checkEP.getEmail().equals(email)
                        && checkEP.getOrgName().equals(orgName)
                        && checkEP.getBusinessNumber().equals(businessNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a user to the collection of users.
     * @param user - the user to add
     */
    private void addUser(User user) {
        this.users.add(user);
    }

    /**
     * Finds the entertainment provider who owns the event with the given event number.
     * @param eventNumber - the ID of the event to search for
     * @return the EntertainmentProvider who owns the event, or null if not found
     */
    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        for (User user : this.getUsers()) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider ep = (EntertainmentProvider) user;
                for (Event e : ep.getEvents()) {
                    if (e.getEventID() == eventNumber) {
                        return ep;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the collection of all users in the system.
     * @return the collection of users
     */
    public Collection<User> getUsers() {
        return users;
    }
}