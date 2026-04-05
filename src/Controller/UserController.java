package src.Controller;

import src.Model.*;
import src.View.*;
import src.ExternalSystems.MockVerificationSystem;

import java.io.*;
import java.util.*;



public class UserController extends Controller {

    public static final String PREREGISTERED_USERS_FILE_PATH = "preregistered_students.csv";
    public static final String PREREGISTERED_ADMIN_FILE_PATH = "preregistered_admins.csv";

    private final MockVerificationSystem verificationSystem;
    private Collection<User> users;
    private TextUserInterface view;
    // we dont have a general model
    public UserController(User currentUser, TextUserInterface view, MockVerificationSystem verificationSystem) {
        super(currentUser, view);
        super.getCurrentUser().setLoggedIn(false);
        this.verificationSystem = verificationSystem;
        this.users = new ArrayList<>();
        this.view = view;
    }

    public void login() {
        if (super.getCurrentUser().isLoggedIn()) return;

        String email = view.getInput("Enter email:");
        if (email == null || email.equals("")) {
            view.displayError("Email can't be empty");
            throw new IllegalArgumentException("Email can't be empty");
        }

        String password = view.getInput("Enter password:");
        if (password == null || password.equals("")) {
            view.displayError("Password can't be empty");
            throw new IllegalArgumentException("Password can't be empty");
        }
        
        String fileName = super.checkCurrentUserIsAdmin()? PREREGISTERED_ADMIN_FILE_PATH : PREREGISTERED_USERS_FILE_PATH;
        // check if users credentials is in the file 
        // assumptions for file structure:
        // email, password 
        // password contains no commas 
        // each email only appears once 
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            List<String> elements = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                elements = Arrays.asList((line.trim()).split(","));
                this.validate(elements, fileName);
                // check if email equals first and password equals second 
                String parsedEmail = elements.get(0);
                String parsedPassword = elements.get(1);
                //error handling for empty vals 
                if (parsedEmail.equals(email)) {
                    if (parsedPassword.equals(password)) {
                        super.getCurrentUser().setLoggedIn(true);
                        view.displaySuccess("Login successful.");
                    } else {
                        view.displayError("Password is incorrect.");
                    }
                    break;
                    // if the email is found whether or not the password is correct we are done 
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file " + fileName + " could not be found.");
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file " + fileName + ".");
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing the file due to a malformed line. Make sure it follows <email>, <password>");
        }

    }

    public void logout() {
        super.getCurrentUser().setLoggedIn(false);
        view.displaySuccess("You have been logged out successfully.");
    }

    public void registerEntertainmentProvider() {
        System.out.println(" Register as Entertainment Provider ");

        String email = view.getInput("Enter your email address:");
        String orgName = view.getInput("Enter your organisation name:");
        String businessNumber = view.getInput("Enter your business registration number:");

        // Duplicate check (email, orgName, or businessNumber already in use)
        try {
            if (this.EPAccountAlreadyExists(email, orgName, businessNumber)) {
                view.displayError("An account with this email, organisation name, or business number already exists.");
                return;
            }
        } catch (IllegalArgumentException e) {
            view.displayError("One of the fields is empty: email, organisation name, and business registration number. Please try again.");
            return;
        }
        

        // Verify the business registration number with the external service
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

    private void addPreregisteredUsers() {
        // add all users to an array 
        // read the files and create a user for each one, then add them to the collection of users 
        // if in admin create admin 
        // filename, user type
        // assume preregistered students only need email and password to be created 
        boolean loadSuccess = this.load(PREREGISTERED_USERS_FILE_PATH, 0) && this.load(PREREGISTERED_ADMIN_FILE_PATH, 1);
        if (!loadSuccess) {
            view.displayError("There was an error parsing the preregistered user files, check the error logs");
        }
    }

    private boolean load(String fileName, int type) {
        //List<AdminStaff> admin = new ArrayList<AdminStaff>();
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

    private void validate(List<String> elements, String fileName) {
        if (elements == null || elements.isEmpty()) {
            throw new NullPointerException("This line is empty in the file + " + fileName + ".");
        }

        if (elements.size() != 2) {
            throw new IllegalArgumentException("The line is malformed. Ensure the line follows the structure : <email>, <password>. File: " + fileName);
        }
    }


    /** Delegates duplicate-check to the model. */
    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        if (email == null || orgName == null || businessNumber == null) {
            throw new IllegalArgumentException("When validating EP account, all fields must be non-empty.");
        }

        for (User user : this.getUsers()) {
            super.setCurrentUser(user);
            if (super.checkCurrentUserIsEntertainmentProvider()) {
                EntertainmentProvider checkEP = (EntertainmentProvider) super.getCurrentUser();
                String checkEmail = checkEP.getEmail();
                String checkOrg = checkEP.getOrgName();
                String checkBusiness = checkEP.getBusinessNumber();
                if (checkEmail.equals(email) && checkOrg.equals(orgName) && checkBusiness.equals(businessNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addUser(User user) {
        this.users.add(user);
    }

    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        for (User user : this.getUsers()) {
            super.setCurrentUser(user);
            if (super.checkCurrentUserIsEntertainmentProvider()) {
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

    public Collection<User> getUsers() {
        return users;
    }
}