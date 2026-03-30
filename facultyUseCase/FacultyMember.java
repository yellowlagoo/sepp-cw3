package facultyUseCase;

public class FacultyMember {
    private int loginAttempts;

    //should this be just initialized to zero? (no parameter)
    public FacultyMember(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    //necessary
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    
}
