public abstract class User {
    private String email;
    private String password;

    /**
     * Constructor for the user class
     * @param email: the user's email
     * @param password: the user's password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //@TODO: add getters & setters