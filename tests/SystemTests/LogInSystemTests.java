package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.console.shadow.picocli.CommandLine.Help.Ansi.Text;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.ExternalSystems.MockVerificationSystem;

public class LogInSystemTests {

    private TextUserInterface view;
    private UserController userController;
    private MockVerificationSystem mockVerificationSystem;

    @BeforeEach
    void setup() {

        view = mock(TextUserInterface.class);
        mockVerificationSystem = new MockVerificationSystem();
        userController = new UserController(view, mockVerificationSystem);
    }

    @Test
    @DisplayName("Testing correct value for login student email")
    void testStudentLogin() {

        when(view.getInput("Enter email:")).thenReturn("bob@hindeburgh.ed.ac.u");
        when(view.getInput("Enter password:")).thenReturn("monkeys99$");

        userController.login();

        verify(view).displaySuccess("User exists: login successful");
    }

}
