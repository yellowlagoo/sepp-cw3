package facultyUseCase;

public class FacultyMember extends User {

    private int loginAttempts;



    //should this be just initialized to zero? (no parameter)
    public FacultyMember(String email, String password) {
        super(email, password);
        this.loginAttempts = 0;
    }

    // call every time a log in is attempted for the account 
    public void increaseLoginAttempts(){
        this.loginAttempts++; 
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public boolean isFirstLogin(){
        return loginAttempts == 1; 
    }
    

    
}
