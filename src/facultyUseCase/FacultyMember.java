package src.facultyUseCase;

public class FacultyMember extends User {
    private int loginAttempts;

    /**
     * Constructor for the Faculty Member class
     * Initializes the login attempts to 0 as a faculty member object will only be created upon first login attempt
     * @param email - the faculty member's email
     * @param password - the faculty member's password
     */
    public FacultyMember(String email, String password) {
        super(email, password);
        this.loginAttempts = 0;
    }

    // Called every time a log in is attempted for the account 
    public void incrementLoginAttempts(){
        this.loginAttempts++; 
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public boolean isFirstLogin(){
        return loginAttempts == 1; 
    }
}
