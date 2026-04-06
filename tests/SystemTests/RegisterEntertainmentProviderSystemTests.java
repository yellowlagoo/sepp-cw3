package tests.SystemTests;

import src.Controller.*;
import src.Model.*;
import src.View.*;
import src.external.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.Mockito.*;



public class RegisterEntertainmentProviderSystemTests {

    private UserController userController;
    private TextUserInterface view;
    private MockVerificationService verificationService;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        verificationService = new MockVerificationService();
        userController = new UserController(view, verificationService);
    }

    // testing registration works
    @Test
    @DisplayName("Checking succesfull registration works")
    void testRegistractionSuccessful() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();

        verify(view).displaySuccess("Entertainment Provider account created for 'testOrganisationName' !");
    }

    // testing null email is handled
    @Test
    @DisplayName("Checking null email error works")
    void testNullEmailError() {

        when(view.getInput("Enter your email address:")).thenReturn(null);
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");

        userController.registerEntertainmentProvider();

        verify(view).displayError(
                "One of the fields is empty: email, organisation name, and business registration number. Please try again.");

    }

    // testing null organistion name is handled
    @Test
    @DisplayName("Checking null organisation name error works")
    void testNullOrganisationNameError() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn(null);
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");

        userController.registerEntertainmentProvider();

        verify(view).displayError(
                "One of the fields is empty: email, organisation name, and business registration number. Please try again.");
    }

    // testing null registration number is handled
    @Test
    @DisplayName("Checking null organisation name error works")
    void testNullRegistrationNumberError() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn(null);

        userController.registerEntertainmentProvider();

        verify(view).displayError(
                "One of the fields is empty: email, organisation name, and business registration number. Please try again.");
    }

    // Duplicate registration attempts test

    @Test
    @DisplayName("Checking values are correct for registering an ep duplicate times")
    void testDuplicateRegistrations() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123455769");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();

        userController.registerEntertainmentProvider();

        verify(view).displayError(
                "An account with this email, organisation name, or business number already exists.");
    }

    // testing business number which is too long
    @Test
    @DisplayName("Checking values are correct for business number being too long in registration")
    void testBusinessNumberTooLong() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A12345576934567934752420942314");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();


        verify(view).displayError(
                "Your business registration number could not be verified.");
    }

    // testing business number which is too short
    @Test
    @DisplayName("Checking values are correct for business number too short in registration")
    void testBusinessNumberTooShort() {

        when(view.getInput("Enter your email address:")).thenReturn("EPtest@ed.ac.uk");
        when(view.getInput("Enter your organisation name:")).thenReturn("testOrganisationName");
        when(view.getInput("Enter your business registration number:")).thenReturn("A123");
        when(view.getInput("Enter contact person name:")).thenReturn("Michael");
        when(view.getInput("Create a password:")).thenReturn("password98980");
        when(view.getInput("Enter a short description of your organisation:"))
                .thenReturn("This is a test organisation for our testing task");

        userController.registerEntertainmentProvider();


        verify(view).displayError(
                "Your business registration number could not be verified.");
    }


}
