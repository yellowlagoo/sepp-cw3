package src.facultyUseCase;

import java.io.*;
import java.util.*; 

public class RegistrationUtility {
    private String filePath;
    private Map<String, String> facultyCredentials; 

    /**
     * Constructor for the RegistrationUtility class
     * @param filePath - the filepath of the file containing approved faculty usernames & passwords
     */
    public RegistrationUtility(String filePath){
        this.filePath = filePath; 
        this.facultyCredentials = null; //loaded on first need 
    }

    /**
     * Registers a faculty member upon first login attempt
     * Returns null upon invalid email or password
     * @param email - the email being logged in with
     * @param password - the password being logged in with
     * @return - the faculty member account created
     */
    public FacultyMember registerFacultyMember(String email, String password){
        loadFileIfNeeded(); 

        // Check for valid email 
        if (!facultyCredentials.containsKey(email.toLowerCase())){
            return null; 
        }

        // Check for valid password
        String storedPassword = facultyCredentials.get(email.toLowerCase()); 
        if(!storedPassword.equals(password)){
            return null; 
        }

        // Credential match; create account now 
        return new FacultyMember(email, password); 
    }
    
    public boolean isInFacultyFile(String email){
        loadFileIfNeeded();
        return facultyCredentials.containsKey(email.toLowerCase()); 
    }

    /**
     * Loads in valid faculty based on approved file if needed, does nothing if not
     */
    private void loadFileIfNeeded(){
        // If already loaded, no need
        if(facultyCredentials != null) return; 

        //Loads in faculty credentials
        facultyCredentials = new HashMap<>(); 
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line; 
            while((line = br.readLine()) != null){
                line = line.trim(); 
                if (line.isEmpty()) continue; 

                String[] parts = line.split(",", 2); // max 2 parts 
                if(parts.length == 2){
                    String email = parts[0].trim().toLowerCase(); 
                    String password = parts[1].trim(); 
                    facultyCredentials.put(email, password); 
                }
            }
            System.out.println("Faculty credentials loaded");
        }
        catch(IOException e){
            System.out.println("File not found"); 
        }

    }

}
