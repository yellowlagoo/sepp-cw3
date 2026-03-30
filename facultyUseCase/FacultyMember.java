package facultyUseCase;

public class FacultyMember extends User {
    private int loginAttempts;

    //should this be just initialized to zero? (no parameter)
    public FacultyMember(String email, String password) {
        super(email, password);
        loginAttempts = 1;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    //is this necessary? or should it just be increment any time they try to log in?
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    
}
