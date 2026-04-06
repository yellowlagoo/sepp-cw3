package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.ExternalSystems.MockVerificationSystem;

public class LogOutSystemTests {

    private TextUserInterface view;
    private UserController userController;
    private MockVerificationSystem mockVerificationSystem;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        mockVerificationSystem = new MockVerificationSystem();
        userController = new UserController(view, mockVerificationSystem);
    }

    // logs in a student so there is an active session  

    private void loginAsStudent() {
        when(view.getInput("Enter email:")).thenReturn("bob@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");
        userController.login();
    }

    private void loginAsAdmin() {
        when(view.getInput("Enter email:")).thenReturn("AdminStaff@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password1");
        userController.login();
    }

    private void loginAsEP() {
        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A12345");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");
        userController.registerEntertainmentProvider();

        when(view.getInput("Enter email:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("password98980");
        userController.login();
    }

    //  Logout displays success message                                   

    @Test
    @DisplayName("Testing logout success message for student")
    void testStudentLogoutDisplaysSuccess() {
        loginAsStudent();

        userController.logout();

        verify(view).displaySuccess("You have been logged out successfully.");
    }

    @Test
    @DisplayName("Testing logout success message for admin")
    void testAdminLogoutDisplaysSuccess() {
        loginAsAdmin();

        userController.logout();

        verify(view).displaySuccess("You have been logged out successfully.");
    }

    @Test
    @DisplayName("Testing logout success message for EP")
    void testEPLogoutDisplaysSuccess() {
        loginAsEP();

        userController.logout();

        verify(view).displaySuccess("You have been logged out successfully.");
    }

    //  Logout clears the current user 

    @Test
    @DisplayName("Testing current user is null after student logout")
    void testStudentLogoutClearsCurrentUser() {
        loginAsStudent();

        userController.logout();

        assertNull(userController.getCurrentUser(), "Current user should be null after logout");
    }

    @Test
    @DisplayName("Testing current user is null after admin logout")
    void testAdminLogoutClearsCurrentUser() {
        loginAsAdmin();

        userController.logout();

        assertNull(userController.getCurrentUser(), "Current user should be null after logout");
    }

    @Test
    @DisplayName("Testing current user is null after EP logout")
    void testEPLogoutClearsCurrentUser() {
        loginAsEP();

        userController.logout();

        assertNull(userController.getCurrentUser(), "Current user should be null after logout");
    }

    //  Logout sets user as no longer logged in    

    @Test
    @DisplayName("Testing user isLoggedIn is false after student logout")
    void testStudentIsLoggedInFalseAfterLogout() {
        loginAsStudent();
        User user = userController.getCurrentUser();

        userController.logout();

        assertFalse(user.isLoggedIn(), "User's isLoggedIn should be false after logout");
    }

    @Test
    @DisplayName("Testing user isLoggedIn is false after admin logout")
    void testAdminIsLoggedInFalseAfterLogout() {
        loginAsAdmin();
        User user = userController.getCurrentUser();

        userController.logout();

        assertFalse(user.isLoggedIn(), "Admin's isLoggedIn should be false after logout");
    }

    //  Login after logout works correctly   

    @Test
    @DisplayName("Testing student can log back in after logging out")
    void testStudentCanLoginAfterLogout() {
        loginAsStudent();
        userController.logout();

        when(view.getInput("Enter email:")).thenReturn("bob@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");
        userController.login();

        assertNotNull(userController.getCurrentUser(), "User should be logged in again after re-login");
    }

    @Test
    @DisplayName("Testing logged in user is a student after re-login")
    void testCurrentUserIsStudentAfterReLogin() {
        loginAsStudent();
        userController.logout();

        when(view.getInput("Enter email:")).thenReturn("bob@hindeburgh.ed.ac.uk");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");
        userController.login();

        assertTrue(userController.getCurrentUser() instanceof Student,
            "Re-logged in user should still be a Student");
    }

    //  Logout when not logged in (guest)    

    @Test
    @DisplayName("Testing logout when no user is logged in")
    void testLogoutWhenNotLoggedIn() {
        // No login — currentUser is null (guest state)
        userController.logout();

        assertNull(userController.getCurrentUser(),
            "Current user should remain null after logging out as guest");
    }
}