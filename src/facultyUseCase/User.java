package src.facultyUseCase;

public class User {
    private String email;
    private String password;
    
    /**
     * Constructor for the User class
     * @param email - the user's email
     * @param password - the user's password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
