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
    
    public UserController(TextUserInterface view, MockVerificationSystem verificationSystem) {
        super(view);
        this.verificationSystem = verificationSystem;
        this.users = new ArrayList<>();
        this.view = view;
    }

    public void login() {
        String email = view.getInput("Enter email:");
        if (email == null || email.equals(" ")) {
            view.displayError("Email can't be empty");
            //throw new IllegalArgumentException("Email can't be empty");
            return;
        }

        String password = view.getInput("Enter password:");
        if (password == null || password.equals(" ")) {
            view.displayError("Password can't be empty");
            //throw new NullPointerException("Password can't be empty");
            return;
        }
        // assumptions for file structure:
            // email, password 
            // password contains no commas 
            // each email only appears once 
            // check if users credentials is in the file 
        String readForStudent = this.readFileForUser(PREREGISTERED_USERS_FILE_PATH, email, password);
        // if current user's email and password match then we have successfully 
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

    private void displayMessaging(String readResult) {
        if (readResult.contains("successful")) {
            view.displaySuccess(readResult);
        } else {
            view.displayError(readResult);
        }
    }


    // true log in successful
    // false: password incorrect, user not found, error in parsing files  
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
                //error handling for empty vals 
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
            return "Error parsing file: problem reading a line in the file "  + fileName + ".";
        } catch (IllegalArgumentException e) {
            return "Error parsing file: malformed line in the file " + fileName + ", ensure all lines follow the format <email>, <password>.";
        } catch (NullPointerException e) {
            return "Error parsing file: there is an empty line in the file " + fileName + ".";
        }
    }

    public void logout() {
        try {
            (super.getCurrentUser()).setLoggedIn(false);
            view.displaySuccess("You have been logged out successfully.");
        } catch (NullPointerException e) {
            view.displayError("No user is logged in, log out failed.");
        }
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