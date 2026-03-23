public class AdminStaff extends User{

    private String name; 

    public AdminStaff(String email, String password){
        super(email, password); 
        this.name = name; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
