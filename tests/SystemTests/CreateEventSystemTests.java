package tests.SystemTests;

import src.Controller.*;
import src.Model.*;
import src.View.*;
import src.external.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.*;

public class CreateEventSystemTests {

    // Controller under tset shared across all tests
    private EventPerformanceController epController;
    private TextUserInterface view;
    // Shared performance list passed into the controller
    private Collection<Performance> sharedPerformances;
    private MockPaymentSystem paymentSystem;
    // Three user types covering the main access control cases
    private EntertainmentProvider ep;
    private Student student;
    private AdminStaff admin;

    @BeforeEach
    void setup() {
        // Use a mocked view so we can verify what gets displayed
        view = mock(TextUserInterface.class);
        sharedPerformances = new ArrayList<>();
        paymentSystem = new MockPaymentSystem();

        epController = new EventPerformanceController(1, 1, paymentSystem, view, sharedPerformances);

        // Default user is an EPm most tests run as this user
        ep = new EntertainmentProvider("ep@test.com", "password", "Test Org", "B1236285749", "Contact", "description");
        ep.setLoggedIn(true);
        epController.setCurrentUser(ep);

        student = new Student("student@test.com", "password");
        student.setLoggedIn(true);

        admin = new AdminStaff("admin@test.com", "password");
        admin.setLoggedIn(true);
    }

    //  Helpers                                                       

    private void stubCreateEventInputs(String title, String type, String isTicketed) {
        when(view.getInput("Enter title of event")).thenReturn(title);
        when(view.getInput("Enter event type (Music, Theatre, Dance, Movie, Sports)")).thenReturn(type);
        when(view.getInput("Is the event ticketed? (true/false)")).thenReturn(isTicketed);
        when(view.getInput("Would you like to add a performance to this event? (true/false)")).thenReturn("false");
    }

    private void stubCreateEventWithOnePerformance(String title, String type, String isTicketed) {
        when(view.getInput("Enter title of event")).thenReturn(title);
        when(view.getInput("Enter event type (Music, Theatre, Dance, Movie, Sports)")).thenReturn(type);
        when(view.getInput("Is the event ticketed? (true/false)")).thenReturn(isTicketed);
        when(view.getInput("Would you like to add a performance to this event? (true/false)")).thenReturn("true");
        when(view.getInput("Would you like to add another performance? (true/false)")).thenReturn("false");
        when(view.getInput("Enter start date and time (yyyy-MM-dd HH:mm)")).thenReturn("2030-06-15 19:00");
        when(view.getInput("Enter end date and time (yyyy-MM-dd HH:mm)")).thenReturn("2030-06-15 22:00");
        when(view.getInput("Enter performer names (comma-separated)")).thenReturn("Artist A, Artist B");
        when(view.getInput("Enter venue address")).thenReturn("100 Main Street");
        when(view.getInput("Enter venue capacity")).thenReturn("500");
        when(view.getInput("Is the venue outdoors? (true/false)")).thenReturn("false");
        when(view.getInput("Does the venue allow smoking? (true/false)")).thenReturn("false");
        when(view.getInput("Enter total number of tickets")).thenReturn("200");
        when(view.getInput("Enter ticket price")).thenReturn("25.00");
    }

    //  Access control                                        

    @Test
    @DisplayName("EP can create an event — success message is displayed")
    void testEPCanCreateEventDisplaysSuccess() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        epController.createEvent();

        verify(view).displaySuccess(contains("Test Concert"));
    }

    @Test
    @DisplayName("EP can create an event — no error is shown")
    void testEPCreateEventNoErrorShown() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        epController.createEvent();

        verify(view, never()).displayError(anyString());
    }

    @Test
    @DisplayName("Student cannot create an event — error is displayed")
    void testStudentCannotCreateEventShowsError() {
        epController.setCurrentUser(student);

        epController.createEvent();

        verify(view).displayError("Must be an entertainment provider to create an event");
    }

    @Test
    @DisplayName("Student cannot create an event — no success message shown")
    void testStudentCannotCreateEventNoSuccess() {
        epController.setCurrentUser(student);

        epController.createEvent();

        verify(view, never()).displaySuccess(anyString());
    }

    @Test
    @DisplayName("Admin cannot create an event — error is displayed")
    void testAdminCannotCreateEventShowsError() {
        epController.setCurrentUser(admin);

        epController.createEvent();

        verify(view).displayError("Must be an entertainment provider to create an event");
    }

    @Test
    @DisplayName("Admin cannot create an event — no success message shown")
    void testAdminCannotCreateEventNoSuccess() {
        epController.setCurrentUser(admin);

        epController.createEvent();

        verify(view, never()).displaySuccess(anyString());
    }

    @Test
    @DisplayName("Guest cannot create an event — error is displayed")
    void testGuestCannotCreateEventShowsError() {
        epController.setCurrentUser(null);

        epController.createEvent();

        verify(view).displayError("Must be logged in to create an event");
    }


    //  Successful event creation — correct messages shown           

    @Test
    @DisplayName("Creating a ticketed event — success message contains title")
    void testCreateTicketedEventSuccessMessageContainsTitle() {
        stubCreateEventInputs("Jazz Night", "Music", "true");

        epController.createEvent();

        verify(view).displaySuccess(contains("Jazz Night"));
    }

    @Test
    @DisplayName("Creating a free event — success message contains title")
    void testCreateFreeEventSuccessMessageContainsTitle() {
        stubCreateEventInputs("Free Dance Show", "Dance", "false");

        epController.createEvent();

        verify(view).displaySuccess(contains("Free Dance Show"));
    }

    @Test
    @DisplayName("Creating two events — success message shown for each")
    void testCreateTwoEventsShowsSuccessForEach() {
        stubCreateEventInputs("Concert", "Music", "true");
        epController.createEvent();

        stubCreateEventInputs("Play", "Theatre", "true");
        epController.createEvent();

        verify(view).displaySuccess(contains("Concert"));
        verify(view).displaySuccess(contains("Play"));
    }

    //  Event type input variations                           

    @Test
    @DisplayName("Lowercase event type input — success message shown")
    void testLowercaseEventTypeShowsSuccess() {
        stubCreateEventInputs("Test Concert", "music", "true");

        epController.createEvent();

        verify(view).displaySuccess(anyString());
    }

    @Test
    @DisplayName("Uppercase event type input — success message shown")
    void testUppercaseEventTypeShowsSuccess() {
        stubCreateEventInputs("Test Concert", "THEATRE", "true");

        epController.createEvent();

        verify(view).displaySuccess(anyString());
    }

    @Test
    @DisplayName("Invalid event type - error message shown")
    void testInvalidEventTypeShowsError() {
        stubCreateEventInputs("Test Concert", "InvalidType", "true");

        epController.createEvent();

        verify(view).displayError(anyString());
    }

    @Test
    @DisplayName("Invalid event type — no success message shown")
    void testInvalidEventTypeNoSuccess() {
        stubCreateEventInputs("Test Concert", "InvalidType", "true");

        epController.createEvent();

        verify(view, never()).displaySuccess(anyString());
    }

    //  Event with performance                                   

    @Test
    @DisplayName("EP creates event with performance — event success message shown")
    void testCreateEventWithPerformanceShowsEventSuccess() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        verify(view).displaySuccess(contains("Jazz Night"));
    }

    @Test
    @DisplayName("EP creates event with performance — performance success message shown")
    void testCreateEventWithPerformanceShowsPerformanceSuccess() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        verify(view).displaySuccess(contains("Performance added successfully"));
    }

    @Test
    @DisplayName("EP creates event with performance — no error shown")
    void testCreateEventWithPerformanceNoError() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        verify(view, never()).displayError(anyString());
    }

    @Test
    @DisplayName("EP creates event without performance — no performance success message")
    void testCreateEventWithoutPerformanceNoPerformanceSuccess() {
        stubCreateEventInputs("Jazz Night", "Music", "true");

        epController.createEvent();

        verify(view, never()).displaySuccess(contains("Performance added successfully"));
    }
    
    //  Multiple event types                                 

    @Test
    @DisplayName("Theatre event created - success message shown")
    void testTheatreEventShowsSuccess() {
        stubCreateEventInputs("A Play", "Theatre", "true");

        epController.createEvent();

        verify(view).displaySuccess(anyString());
    }

    @Test
    @DisplayName("Sports event created — success message shown")
    void testSportsEventShowsSuccess() {
        stubCreateEventInputs("A Match", "Sports", "false");

        epController.createEvent();

        verify(view).displaySuccess(anyString());
    }

    @Test
    @DisplayName("Movie event created — success message shown")
    void testMovieEventShowsSuccess() {
        stubCreateEventInputs("A Film", "Movie", "false");

        epController.createEvent();

        verify(view).displaySuccess(anyString());
    }
}