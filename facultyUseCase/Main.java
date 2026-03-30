package facultyUseCase;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String username = scan.next();
        username = username.trim();

        System.out.println("Enter your password: ");
        String password = scan.next();
        password = password.trim();

        String filepath = "mockFile.txt";
        RegistrationUtility registration = new RegistrationUtility(filepath);
        registration.registerFacultyMember(username, password);
    }
}
