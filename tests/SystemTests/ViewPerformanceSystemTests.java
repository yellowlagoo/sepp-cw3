package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.external.MockPaymentSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ViewPerformanceSystemTests {

    private TextUserInterface view;
    private EventPerformanceController eventPerformanceController;
    private MockPaymentSystem mockPaymentSystem;
    private Collection<Performance> allPerformances;
    private EntertainmentProvider ep;
    private Event event;
    private Performance performance;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        mockPaymentSystem = new MockPaymentSystem();
        allPerformances = new ArrayList<>();

        eventPerformanceController = new EventPerformanceController(
                1, 1, mockPaymentSystem, view, allPerformances);

        ep = new EntertainmentProvider("ep@test.com", "password123", "Test Org", "B9999", "Alice", "Test org desc");
        event = new Event(ep, 1, "Jazz Night", EventType.MUSIC, true);

        performance = new Performance(1,
                LocalDateTime.of(2030, 6, 15, 19, 30),
                LocalDateTime.of(2030, 6, 15, 22, 0),
                Arrays.asList("John"), "Edinburgh Venue, EH1 1AA",
                100, false, false, 50, 20.00, event);

        allPerformances.add(performance);
    }

    //  View performance — success

    @Test
    @DisplayName("Testing valid ID calls displaySpecificPerformance")
    void testValidIdCallsDisplaySpecificPerformance() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing displayed performance contains event title")
    void testDisplayedPerformanceContainsEventTitle() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(contains("Jazz Night"));
    }

    @Test
    @DisplayName("Testing displayed performance contains venue address")
    void testDisplayedPerformanceContainsVenueAddress() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(contains("Edinburgh Venue, EH1 1AA"));
    }

    @Test
    @DisplayName("Testing displayed performance contains performer name")
    void testDisplayedPerformanceContainsPerformerName() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(contains("John"));
    }

    //  View performance — non-existent ID

    @Test
    @DisplayName("Testing error shown for non-existent performance ID")
    void testNonExistentIdShowsError() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("999");

        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance with given number does not exist");
    }

    @Test
    @DisplayName("Testing displaySpecificPerformance not called when ID does not exist")
    void testDisplaySpecificPerformanceNotCalledForNonExistentId() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("999");

        eventPerformanceController.viewPerformance();

        verify(view, never()).displaySpecificPerformance(anyString());
    }

    //  View performance — non-numeric ID

    @Test
    @DisplayName("Testing error shown for non-numeric performance ID")
    void testNonNumericIdShowsError() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("abc");

        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance ID must be a number");
    }

    @Test
    @DisplayName("Testing displaySpecificPerformance not called for non-numeric ID")
    void testDisplaySpecificPerformanceNotCalledForNonNumericId() {
        when(view.getInput("Enter ID of performance to view")).thenReturn("abc");

        eventPerformanceController.viewPerformance();

        verify(view, never()).displaySpecificPerformance(anyString());
    }

    //  View performance — any user can view (no access control)

    @Test
    @DisplayName("Testing guest user can view a performance")
    void testGuestCanViewPerformance() {
        eventPerformanceController.setCurrentUser(null);
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing student can view a performance")
    void testStudentCanViewPerformance() {
        Student student = new Student("bob@hindeburgh.ed.ac.uk", "monkeys99$");
        student.setLoggedIn(true);
        eventPerformanceController.setCurrentUser(student);
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing EP can view a performance")
    void testEPCanViewPerformance() {
        ep.setLoggedIn(true);
        eventPerformanceController.setCurrentUser(ep);
        when(view.getInput("Enter ID of performance to view")).thenReturn("1");

        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }
}
