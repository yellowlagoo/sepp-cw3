package tests.SystemTests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.external.*;

public class LogInSystemTests {

    private TextUserInterface view;
    private UserController userController;
    private MockVerificationService mockVerificationService;

    @BeforeAll
    static void initAll() {
        System.out.println("Testing for LogIn use case started");
        System.out.println("--------------------------------");
    }

    @AfterEach
    void betweenTests() {
        System.out.println("--------------------------------");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Testing for LogIn use case completed");
    }

    @BeforeEach
    void setup() {

        view = mock(TextUserInterface.class);
        mockVerificationService = new MockVerificationService();
        userController = new UserController(view, mockVerificationService);
    }

    // Testing Student login works
    @Test
    @DisplayName("Testing correct value for login student")
    void testStudentLogin() {

        when(view.getInput("Enter email:")).thenReturn("bob@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");

        userController.login();

        verify(view).displaySuccess("User exists: login successful");
    }

    // Testing Admin login works
    @Test
    @DisplayName("Testing correct value for login student")
    void testAdminLogin() {

        when(view.getInput("Enter email:")).thenReturn("AdminStaff@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password1");

        userController.login();

        verify(view).displaySuccess("User exists: login successful");
    }

    // Testing EP login works after being registered
    @Test
    @DisplayName("Testing if registered EP's can login")
    void testEPLogin() {

        // First registering the EP
        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();

        // Now loggin them in
        when(view.getInput("Enter email:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password98980");

        userController.login();

        verify(view).displaySuccess("User exists: login successful");

    }

    // Testing null Email entry handling
    @Test
    @DisplayName("Testing correct value for null email entry")
    void testNullEmail() {

        when(view.getInput("Enter email:")).thenReturn(null);
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");

        userController.login();

        verify(view).displayError("Email can't be empty");
    }

    // Testing for empty email
    @Test
    @DisplayName("Testing correct value for empty email entry")
    void testEmptyEmail() {

        when(view.getInput("Enter email:")).thenReturn("");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");

        userController.login();

        verify(view).displayError("Email can't be empty");
    }

    // Testing for null Password
    @Test
    @DisplayName("Testing correct value for null password entry")
    void testNullPassword() {

        when(view.getInput("Enter email:")).thenReturn("bob@hidenburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn(null);

        userController.login();

        verify(view).displayError("Password can't be empty");
    }

    // Testing for empty Password
    @Test
    @DisplayName("Testing correct value for empty password entry")
    void testEmptyPassword() {

        when(view.getInput("Enter email:")).thenReturn("bob@hidenburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("");

        userController.login();

        verify(view).displayError("Password can't be empty");
    }

    // Testing student login with non-pre-registered email
    @Test
    @DisplayName("Testing correct value for login student with non-registered email")
    void testWrongStudentEmail() {

        when(view.getInput("Enter email:")).thenReturn("DoesntExist@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");

        userController.login();

        verify(view).displayError("User not found");
    }

    // Testing student incorrect password
    @Test
    @DisplayName("Testing correct value for login student with incorrect password")
    void testWrongStudentPassword() {

        when(view.getInput("Enter email:")).thenReturn("bob@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys");

        userController.login();

        verify(view).displayError("User exists: incorrect password");
    }

    // Testing Admin non-pre-registered email
    @Test
    @DisplayName("Testing correct value for login Admin with non-pre-registered email")
    void testWrongAdminEmail() {

        when(view.getInput("Enter email:")).thenReturn("DoesntExist@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");

        userController.login();

        verify(view).displayError("User not found");
    }

    // Test Admin incorrect password
    @Test
    @DisplayName("Testing correct value for login student with incorrect password")
    void testWrongAdminPassword() {

        when(view.getInput("Enter email:")).thenReturn("bobby@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys");

        userController.login();

        verify(view).displayError("User exists: incorrect password");
    }

    // Testing registered EP wrong email
    @Test
    @DisplayName("Testing for non-registered registered EP email")
    void testWrongEPEmail() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();

        when(view.getInput("Enter email:")).thenReturn("EPwrongemail@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password98980");

        userController.login();

        verify(view).displayError("User not found");
    }

    // Testing registered EP incorrect password
    @Test
    @DisplayName("Testing for incorrect registered EP email")
    void testWrongEPPassword() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();

        when(view.getInput("Enter email:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("passwordwrong");

        userController.login();

        verify(view).displayError("User exists: incorrect password");
    }

    // Testing logged in user is a student
    @Test
    @DisplayName("Testing logged in user is a student")
    void testLoggedInUserStudent() {

        when(view.getInput("Enter email:")).thenReturn("amy@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("BlueBerry56");

        userController.login();

        assertTrue(userController.getCurrentUser() instanceof Student, "Logged in user should be a student");
    }

    // testing logged in user is an admin
    @Test
    @DisplayName("Testing logged in user is an Admin")
    void testLoggedInUserAdmin() {

        when(view.getInput("Enter email:")).thenReturn("tom@edin@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password1");

        userController.login();

        assertTrue(userController.getCurrentUser() instanceof AdminStaff, "Logged in user should be a AdminStaff");
    }

    // testing logged in user is an ep
    @Test
    @DisplayName("Testing for wrong registered EP email")
    void testLoggedInUserEP() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();

        when(view.getInput("Enter email:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password98980");

        userController.login();

        assertTrue(userController.getCurrentUser() instanceof EntertainmentProvider, "Logged in user should be a EP");
    }

    // testing logged in user is logged in
    @Test
    @DisplayName("Testing logged in user is an Admin")
    void testLoggedIsLoggedIn() {

        when(view.getInput("Enter email:")).thenReturn("tom@edin@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password1");

        userController.login();

        assertTrue(userController.getCurrentUser().isLoggedIn(), "User should be logged in");
    }



}