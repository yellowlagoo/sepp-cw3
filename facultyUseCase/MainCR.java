package facultyUseCase; 

import java.util.Scanner; 

public class MainCR {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        RegistrationUtility utility = new RegistrationUtility("mockFile.txt");

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Try lazy migration
        FacultyMember faculty = utility.registerFacultyMember(email, password);

        if (faculty == null) {
            System.out.println("Invalid email or password.");
            return;
        }

        // Account created — first login
        faculty.incrementLoginAttempts();
        System.out.println("Welcome! Your faculty account has been created.");

        System.out.println("Logged in as: " + faculty.getEmail());
    }
}