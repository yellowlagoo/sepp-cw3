package facultyUseCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap; 
import java.util.Map; 


public class RegistrationUtility {

    private String filePath;

    private Map<String, String> facultyCredentials; 

    public RegistrationUtility(String filePath){
        this.filePath = filePath; 
        this.facultyCredentials = null; //loaded on first need 
    }

    public FacultyMember registerFacultyMember(String email, String password){
        
        loadFileIfNeeded(); 

        // check if email exists in university file 
        if (!facultyCredentials.containsKey(email.toLowerCase())){
            return null; 
        }

        String storedPassword = facultyCredentials.get(email.toLowerCase()); 

        // check that password matches 
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

    // Helpers 
    private void loadFileIfNeeded(){
        // already loaded 
        if(facultyCredentials != null) return; 

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
