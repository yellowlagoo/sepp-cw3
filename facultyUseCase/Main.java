import facultyUseCase.FacultyMember;
import facultyUseCase.RegistrationUtility;
import java.util.Scanner; 

public class Main {

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
        faculty.increaseLoginAttempts();
        System.out.println("Welcome! Your faculty account has been created.");

        // Offer password change on first login
        System.out.print("Would you like to set a new password? (yes/no): ");
        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y")) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine().trim();
            faculty.setPassword(newPassword);
            System.out.println("Password updated successfully.");
        }

        System.out.println("Logged in as: " + faculty.getEmail());
    }
}