package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.contains;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.ExternalSystems.MockVerificationSystem;

public class EditPreferencesSystemTests {

    private TextUserInterface view;
    private UserController userController;
    private MockVerificationSystem mockVerificationSystem;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        mockVerificationSystem = new MockVerificationSystem();
        userController = new UserController(view, mockVerificationSystem);
    }

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

    //  Edit preferences — success cases

    @Test
    @DisplayName("Testing student can set music preference")
    void testStudentCanSetMusicPreference() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music");
        userController.editPreferences();

        verify(view).displaySuccess(contains("Preferences updated"));
    }

    @Test
    @DisplayName("Testing student can set multiple preferences")
    void testStudentCanSetMultiplePreferences() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music,dance");
        userController.editPreferences();

        verify(view).displaySuccess(contains("Preferences updated"));
    }

    @Test
    @DisplayName("Testing student can set all preferences")
    void testStudentCanSetAllPreferences() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):"))
                .thenReturn("music,dance,movie,sports,theatre");
        userController.editPreferences();

        verify(view).displaySuccess(contains("Preferences updated"));
    }

    @Test
    @DisplayName("Testing success message contains updated preference")
    void testSuccessMessageContainsUpdatedPreference() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music");
        userController.editPreferences();

        verify(view).displaySuccess(contains("true"));
    }

    //  Edit preferences — preferences are actually updated

    @Test
    @DisplayName("Testing music preference is set to true after update")
    void testMusicPreferenceIsUpdated() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music");
        userController.editPreferences();

        Student student = (Student) userController.getCurrentUser();
        assertTrue(student.getPreferences().prefersMusicEvents());
    }

    @Test
    @DisplayName("Testing dance preference is set to true after update")
    void testDancePreferenceIsUpdated() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("dance");
        userController.editPreferences();

        Student student = (Student) userController.getCurrentUser();
        assertTrue(student.getPreferences().prefersDanceEvents());
    }

    @Test
    @DisplayName("Testing unselected preferences remain false after partial update")
    void testUnselectedPreferencesRemainFalse() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music");
        userController.editPreferences();

        Student student = (Student) userController.getCurrentUser();
        assertFalse(student.getPreferences().prefersDanceEvents());
        assertFalse(student.getPreferences().prefersMovieEvents());
        assertFalse(student.getPreferences().prefersSportsEvents());
        assertFalse(student.getPreferences().prefersTheaterEvents());
    }

    @Test
    @DisplayName("Testing preferences are replaced on second update")
    void testPreferencesAreReplacedOnSecondUpdate() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music");
        userController.editPreferences();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("dance");
        userController.editPreferences();

        Student student = (Student) userController.getCurrentUser();
        assertFalse(student.getPreferences().prefersMusicEvents(), "Music should be cleared after second update");
        assertTrue(student.getPreferences().prefersDanceEvents());
    }

    //  Edit preferences — invalid input

    @Test
    @DisplayName("Testing invalid preference category shows error")
    void testInvalidPreferenceCategoryShowsError() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("jazz");
        userController.editPreferences();

        verify(view).displayError("An error occurred while updating preferences.");
    }

    @Test
    @DisplayName("Testing empty preference input shows error")
    void testEmptyPreferenceInputShowsError() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("");
        userController.editPreferences();

        verify(view).displayError("An error occurred while updating preferences.");
    }

    @Test
    @DisplayName("Testing null preference input shows error")
    void testNullPreferenceInputShowsError() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn(null);
        userController.editPreferences();

        verify(view).displayError("An error occurred while updating preferences.");
    }

    @Test
    @DisplayName("Testing mixed valid and invalid preferences shows error")
    void testMixedValidAndInvalidPreferencesShowsError() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("music,jazz");
        userController.editPreferences();

        verify(view).displayError("An error occurred while updating preferences.");
    }

    @Test
    @DisplayName("Testing displaySuccess is not called on invalid input")
    void testSuccessNotCalledOnInvalidInput() {
        loginAsStudent();

        when(view.getInput("Enter new preferences (comma-separated, e.g. music, dance):")).thenReturn("jazz");
        userController.editPreferences();

        verify(view, never()).displaySuccess(contains("Preferences updated"));
    }

    //  Edit preferences — access control

    @Test
    @DisplayName("Testing non-student (admin) cannot edit preferences")
    void testAdminCannotEditPreferences() {
        loginAsAdmin();

        userController.editPreferences();

        verify(view).displayError("Only students can edit preferences.");
    }

    @Test
    @DisplayName("Testing non-student (EP) cannot edit preferences")
    void testEPCannotEditPreferences() {
        loginAsEP();

        userController.editPreferences();

        verify(view).displayError("Only students can edit preferences.");
    }

    @Test
    @DisplayName("Testing guest cannot edit preferences")
    void testGuestCannotEditPreferences() {
        // No login — guest (currentUser is null, checkCurrentUserIsStudent throws NPE)
        assertThrows(NullPointerException.class, () -> userController.editPreferences());
    }

    @Test
    @DisplayName("Testing displaySuccess is not called when non-student tries to edit preferences")
    void testSuccessNotCalledForNonStudent() {
        loginAsAdmin();

        userController.editPreferences();

        verify(view, never()).displaySuccess(contains("Preferences updated"));
    }
}
