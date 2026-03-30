package facultyUseCase;

public class RegistrationUtility {
    private String filepath;

    public RegistrationUtility(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public FacultyMember registerFacultyMember(){
        //check if faculty member exists (I believe )
        //if yes, don't need to register --> increment login attempts, return it
        //if no, use constructor for faculty member class (includes email and password!) (login true?)
    }
}
