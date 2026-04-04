package facultyUseCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class TestFacultyUseCase {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) throws IOException {

        // Create a temporary mock file for tests that need it
        Path mockFile = Files.createTempFile("mockFaculty", ".txt");
        Files.writeString(mockFile,
            "j.smith@university.ac.uk, faculty123\n" +
            "a.jones@university.ac.uk, letmein456\n" +
            "p.brown@university.ac.uk, secure789\n"
        );
        String filePath = mockFile.toString();

        System.out.println("FacultyMember Tests ");

        // isFirstLogin()
        testIsFirstLogin_noLoginYet_returnsFalse();
        testIsFirstLogin_afterOneIncrement_returnsTrue();
        testIsFirstLogin_afterTwoIncrements_returnsFalse();
        testIsFirstLogin_afterManyIncrements_returnsFalse();

        // incrementLoginAttempts()
        testIncrementLoginAttempts_startsAtZero();
        testIncrementLoginAttempts_onceGivesOne();
        testIncrementLoginAttempts_multipleTimes_countsCorrectly();

        // setPassword() (non-trivial — changes state)
        testSetPassword_updatesCorrectly();
        testSetPassword_emptyString();

        // Inheritance
        testFacultyMember_isInstanceOfUser();

        System.out.println("RegistrationUtility Tests");

        // Valid credentials
        testRegister_validCredentials_returnsNotNull(filePath);
        testRegister_validCredentials_correctEmail(filePath);
        testRegister_secondValidEntry_returnsNotNull(filePath);
        testRegister_emailCaseInsensitive_returnsNotNull(filePath);

        // Invalid credentials
        testRegister_wrongPassword_returnsNull(filePath);
        testRegister_emailNotInFile_returnsNull(filePath);
        testRegister_emptyEmail_returnsNull(filePath);
        testRegister_emptyPassword_returnsNull(filePath);

        // Missing file
        testRegister_fileMissing_returnsNull();

        // isInFacultyFile()
        testIsInFacultyFile_knownEmail_returnsTrue(filePath);
        testIsInFacultyFile_unknownEmail_returnsFalse(filePath);
        testIsInFacultyFile_emptyEmail_returnsFalse(filePath);

        // Summary
        System.out.println("Results ");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));
    }

    //  FacultyMember tests                                                

    static void testIsFirstLogin_noLoginYet_returnsFalse() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        check("isFirstLogin() with no logins yet returns false",
              fm.isFirstLogin() == false);
    }

    static void testIsFirstLogin_afterOneIncrement_returnsTrue() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        fm.incrementLoginAttempts();
        check("isFirstLogin() after 1 increment returns true",
              fm.isFirstLogin() == true);
    }

    static void testIsFirstLogin_afterTwoIncrements_returnsFalse() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        fm.incrementLoginAttempts();
        fm.incrementLoginAttempts();
        check("isFirstLogin() after 2 increments returns false",
              fm.isFirstLogin() == false);
    }

    static void testIsFirstLogin_afterManyIncrements_returnsFalse() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        for (int i = 0; i < 10; i++) fm.incrementLoginAttempts();
        check("isFirstLogin() after 10 increments returns false",
              fm.isFirstLogin() == false);
    }

    static void testIncrementLoginAttempts_startsAtZero() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        check("loginAttempts starts at 0",
              fm.getLoginAttempts() == 0);
    }

    static void testIncrementLoginAttempts_onceGivesOne() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        fm.incrementLoginAttempts();
        check("loginAttempts is 1 after one increment",
              fm.getLoginAttempts() == 1);
    }

    static void testIncrementLoginAttempts_multipleTimes_countsCorrectly() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        fm.incrementLoginAttempts();
        fm.incrementLoginAttempts();
        fm.incrementLoginAttempts();
        check("loginAttempts is 3 after three increments",
              fm.getLoginAttempts() == 3);
    }

    static void testSetPassword_updatesCorrectly() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        fm.setPassword("newPassword");
        check("setPassword() updates the password correctly",
              fm.getPassword().equals("newPassword"));
    }

    static void testSetPassword_emptyString() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        fm.setPassword("");
        check("setPassword() with empty string stores empty string",
              fm.getPassword().equals(""));
    }

    static void testFacultyMember_isInstanceOfUser() {
        FacultyMember fm = new FacultyMember("j.smith@university.ac.uk", "pass123");
        check("FacultyMember is an instance of User",
              fm instanceof User);
    }

    //  RegistrationUtility tests   

    static void testRegister_validCredentials_returnsNotNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "j.smith@university.ac.uk", "faculty123");
        check("registerFacultyMember() with valid credentials returns not null",
              fm != null);
    }

    static void testRegister_validCredentials_correctEmail(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "j.smith@university.ac.uk", "faculty123");
        check("registerFacultyMember() returns object with correct email",
              fm != null && fm.getEmail().equals("j.smith@university.ac.uk"));
    }

    static void testRegister_secondValidEntry_returnsNotNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "a.jones@university.ac.uk", "letmein456");
        check("registerFacultyMember() works for second entry in file",
              fm != null);
    }

    static void testRegister_emailCaseInsensitive_returnsNotNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "J.SMITH@UNIVERSITY.AC.UK", "faculty123");
        check("registerFacultyMember() is case insensitive for email",
              fm != null);
    }

    static void testRegister_wrongPassword_returnsNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "j.smith@university.ac.uk", "wrongpassword");
        check("registerFacultyMember() with wrong password returns null",
              fm == null);
    }

    static void testRegister_emailNotInFile_returnsNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "nobody@university.ac.uk", "faculty123");
        check("registerFacultyMember() with unknown email returns null",
              fm == null);
    }

    static void testRegister_emptyEmail_returnsNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember("", "faculty123");
        check("registerFacultyMember() with empty email returns null",
              fm == null);
    }

    static void testRegister_emptyPassword_returnsNull(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        FacultyMember fm = utility.registerFacultyMember(
            "j.smith@university.ac.uk", "");
        check("registerFacultyMember() with empty password returns null",
              fm == null);
    }

    static void testRegister_fileMissing_returnsNull() {
        RegistrationUtility utility = new RegistrationUtility("nonexistent.txt");
        FacultyMember fm = utility.registerFacultyMember(
            "j.smith@university.ac.uk", "faculty123");
        check("registerFacultyMember() with missing file returns null",
              fm == null);
    }

    static void testIsInFacultyFile_knownEmail_returnsTrue(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        check("isInFacultyFile() with known email returns true",
              utility.isInFacultyFile("j.smith@university.ac.uk"));
    }

    static void testIsInFacultyFile_unknownEmail_returnsFalse(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        check("isInFacultyFile() with unknown email returns false",
              !utility.isInFacultyFile("nobody@university.ac.uk"));
    }

    static void testIsInFacultyFile_emptyEmail_returnsFalse(String filePath) {
        RegistrationUtility utility = new RegistrationUtility(filePath);
        check("isInFacultyFile() with empty email returns false",
              !utility.isInFacultyFile(""));
    }

    //  Helper — prints PASS/FAIL and updates counters               
    static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }
}