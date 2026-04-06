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
import src.ExternalSystems.MockPaymentSystem;

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

        ep = new EntertainmentProvider("EPtest@ed.ac.uk", "password98980",
            "testOrganisationName", "A12345", "Michael",
            "This is a test organisation for our testing task");
        ep.setLoggedIn(true);

        event = new Event(ep, 1, "Jazz Night", EventType.MUSIC, true);
        performance = new Performance(1,
            LocalDateTime.of(2027, 6, 15, 19, 30),
            LocalDateTime.of(2027, 6, 15, 22, 0),
            Arrays.asList("John", "Sarah"),
            "Edinburgh Venue, EH1 1AA", 100, false, false, 50, 20.00, event);

        allPerformances.add(performance);
        eventPerformanceController.setCurrentUser(ep);
    }


    //  Helpers

    private void loginAsEP() {
        ep.setLoggedIn(true);
        eventPerformanceController.setCurrentUser(ep);
    }

    private void loginAsStudent() {
        Student student = new Student("bob@hindeburgh.ed.ac.uk", "monkeys99$");
        student.setLoggedIn(true);
        eventPerformanceController.setCurrentUser(student);
    }

    private void loginAsAdmin() {
        AdminStaff admin = new AdminStaff("AdminStaff@ed.ac.uk", "password1");
        admin.setLoggedIn(true);
        eventPerformanceController.setCurrentUser(admin);
    }

    //  View performance — success cases

    @Test
    @DisplayName("Testing EP can view an existing performance")
    void testEPCanViewPerformance() {
        loginAsEP();

        when(view.getInput("Enter ID of performance to view")).thenReturn(String.valueOf(performance.getPerformanceID()));
        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing student can view an existing performance")
    void testStudentCanViewPerformance() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn(String.valueOf(performance.getPerformanceID()));
        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing admin can view an existing performance")
    void testAdminCanViewPerformance() {
        loginAsAdmin();

        when(view.getInput("Enter ID of performance to view")).thenReturn(String.valueOf(performance.getPerformanceID()));
        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing guest can view an existing performance")
    void testGuestCanViewPerformance() {
        // No login — guest
        eventPerformanceController.setCurrentUser(null);

        when(view.getInput("Enter ID of performance to view")).thenReturn(String.valueOf(performance.getPerformanceID()));
        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing displayed performance info contains event title")
    void testViewedPerformanceContainsEventTitle() {
        when(view.getInput("Enter ID of performance to view")).thenReturn(String.valueOf(performance.getPerformanceID()));
        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(contains("Jazz Night"));
    }

    @Test
    @DisplayName("Testing displayed performance info contains venue address")
    void testViewedPerformanceContainsVenueAddress() {
        when(view.getInput("Enter ID of performance to view")).thenReturn(String.valueOf(performance.getPerformanceID()));
        eventPerformanceController.viewPerformance();

        verify(view).displaySpecificPerformance(contains("Edinburgh Venue, EH1 1AA"));
    }

    //  View performance — performance does not exist

    @Test
    @DisplayName("Testing viewing a non-existent performance shows error")
    void testViewNonExistentPerformanceShowsError() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn("999");
        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance with given number does not exist");
    }

    @Test
    @DisplayName("Testing viewing with ID 0 shows error")
    void testViewPerformanceWithIdZeroShowsError() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn("0");
        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance with given number does not exist");
    }

    @Test
    @DisplayName("Testing viewing with negative ID shows error")
    void testViewPerformanceWithNegativeIdShowsError() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn("-1");
        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance with given number does not exist");
    }

    @Test
    @DisplayName("Testing viewing with invalid (non-numeric) ID shows error")
    void testViewPerformanceWithInvalidIdShowsError() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn("abc");
        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance ID must be a number");
    }

    //  View performance — empty performances list

    @Test
    @DisplayName("Testing viewing a performance when no performances exist shows error")
    void testViewPerformanceWhenNoneExistShowsError() {
        loginAsStudent();
        allPerformances.clear();

        when(view.getInput("Enter ID of performance to view")).thenReturn("1");
        eventPerformanceController.viewPerformance();

        verify(view).displayError("Performance with given number does not exist");
    }

    //  displaySpecificPerformance NOT called on failure

    @Test
    @DisplayName("Testing displaySpecificPerformance not called for non-existent performance")
    void testDisplayNotCalledForNonExistentPerformance() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn("999");
        eventPerformanceController.viewPerformance();

        verify(view, never()).displaySpecificPerformance(anyString());
    }

    @Test
    @DisplayName("Testing displaySpecificPerformance not called for invalid ID")
    void testDisplayNotCalledForInvalidId() {
        loginAsStudent();

        when(view.getInput("Enter ID of performance to view")).thenReturn("abc");
        eventPerformanceController.viewPerformance();

        verify(view, never()).displaySpecificPerformance(anyString());
    }
}
