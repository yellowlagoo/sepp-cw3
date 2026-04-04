package src.Model; 

public abstract class User {
    private String email;
    private String password;
    private boolean loggedIn;

    /**
     * Constructor for the user class
     * @param email - the user's email
     * @param password - the user's password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        loggedIn = false;
    }

    /**
     * Returns the user's email
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email
     * @param email - the user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's email
     * @return - the user's email
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * @param password - the user's new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's login status
     * @return - the user's login status
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the user's login status
     * @param loggedIn - the user's log status
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}