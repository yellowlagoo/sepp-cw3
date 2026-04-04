package src.Controller;

import src.Model.*;
import src.View.*;
import src.facultyUseCase.RegistrationUtility;
import src.ExternalSystems.VerificationSystem;

import java.io.*;
import java.util.*;

import org.omg.CORBA.SystemException;


public class UserController extends Controller {

    public static final String PREREGISTERED_USERS_FILE_PATH = "preregistered_students.csv";
    public static final String PREREGISTERED_ADMIN_FILE_PATH = "preregistered_admins.csv";

    private final VerificationSystem verificationSystem;
    private Collection<User> users;

    // we dont have a general model
    public UserController(User currentUser, View view, VerificationSystem verificationSystem) {
        super(currentUser, view);
        super.getCurrentUser().setLoggedIn(false);
        this.verificationSystem = verificationSystem;
        this.users = new ArrayList<>();
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
            Boolean found = false;

            while ((line = br.readLine()) != null) {
                elements = Arrays.asList(line.split(","));
                if (elements.size() != 2) {
                    //malformed input on this line  
                    throw new Exception("Malformed line in the file " + fileName + ".");
                }

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

        } catch (Exception e) {
            if (e.equals(new FileNotFoundException())) {
                System.err.println("The file " + fileName + " could not be found.");
            } else if (e.equals(new IOException())) {
                System.err.println("An error occurred while reading the file " + fileName + ".");
            } else {
                throw new Error("An error occurred while logging in");
            }
            
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
        if (EPAccountAlreadyExists(email, orgName, businessNumber)) {
            view.displayError("An account with this email, organisation name, or business number already exists.");
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
        addUser(ep);

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
        // assume preregistered students only need email to be created 
       List<Student> students = this.loadStudents(PREREGISTERED_USERS_FILE_PATH);
       List<AdminStaff> admin = this.loadAdmin(PREREGISTERED_ADMIN_FILE_PATH);

    }

    private List<Student> loadStudents(String fileName) {
        List<Student> students = new ArrayList<Student>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> elements = Arrays.asList((line.trim()).split(","));
                this.validate(elements, fileName); // do i need this in a try catch or will it be caught by the outer try catch 
                Student parsed = new Student(elements.get(0), elements.get(1));
                this.addUser(parsed);
            }
        } catch (Exception f) {
            //errorHandling(f);
            //file not found - fileReader 
            //io exception - readline 
            //illegal argument exception when parsing lines 
            if (f.equals(new IllegalArgumentException())) {
                return students;
            }
            throw new Error("file not found");
        }
        return students;
    }

    private List<AdminStaff> loadAdmin(String fileName) {
        List<AdminStaff> admin = new ArrayList<AdminStaff>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> elements = Arrays.asList((line.trim()).split(","));
                this.validate(elements, fileName); // do i need this in a try catch or will it be caught by the outer try catch 
                AdminStaff parsed = new AdminStaff(elements.get(0), elements.get(1));
                this.addUser(parsed);
            }
        } catch (Exception f) {
            //file not found - fileReader 
            //io exception - readline 
            //illegal argument exception when parsing lines 
            if (f.equals(new IllegalArgumentException())) {
                return admin;
            }
            throw new Error("file not found");
        }
        return admin;
    }

    private void validate(List<String> elements, String fileName) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("This line is empty in the file + " + fileName + ".");
        }

        if (elements.size() != 2) {
            throw new IllegalArgumentException("The line is malformed. Ensure the line follows the structure : <email>, <password>. File: " + fileName);
        }
    }


    /** Delegates duplicate-check to the model. */
    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        return model.epAccountAlreadyExists(email, orgName, businessNumber);
    }

    private void addUser(User user) {
        this.addUser(user);
    }

    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        for (User u : this.getUsers()) {
            if (u instanceof EntertainmentProvider) {
                EntertainmentProvider ep = (EntertainmentProvider) u;
                for (Event e : ep.getEvents()) {
                    if (e.getEventID() == eventNumber)
                        return ep;
                }
            }
        }
        return null;
    }

}