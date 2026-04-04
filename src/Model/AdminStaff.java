package src.Model; 

public class AdminStaff extends User{
    private String name; 

    /**
     * Constructor for AdminStaff
     * @param email - the staff member's email
     * @param password - the staff member's password
     */
    public AdminStaff(String email, String password){
        super(email, password); 
        this.name = ""; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}