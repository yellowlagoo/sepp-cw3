package src.facultyUseCase; 

import java.util.Scanner; 

public class MainCR {
    public static void main(String[] args) {
        RegistrationUtility utility = new RegistrationUtility("mockFile.txt");

        //Taking in user input for username and password
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Try lazy migration
        FacultyMember faculty = utility.registerFacultyMember(email, password);

        //Only registers faculty if valid information provided
        if (faculty == null) {
            System.out.println("Invalid email or password.");
            return;
        }
        else{
            faculty.incrementLoginAttempts();
            System.out.println("Welcome! Your faculty account has been created.");
            System.out.println("Logged in as: " + faculty.getEmail());
        }
    }
}