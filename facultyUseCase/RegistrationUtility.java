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
}
