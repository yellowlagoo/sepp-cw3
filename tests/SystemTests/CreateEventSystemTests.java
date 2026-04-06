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
import static org.mockito.ArgumentMatchers.contains;

import java.util.*;

public class CreateEventSystemTests {

    private EventPerformanceController epController;
    private TextUserInterface view;
    private Collection<Performance> sharedPerformances;
    private MockPaymentSystem paymentSystem;
    private EntertainmentProvider ep;
    private Student student;
    private AdminStaff admin;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        sharedPerformances = new ArrayList<>();
        paymentSystem = new MockPaymentSystem();

        epController = new EventPerformanceController(1, 1, paymentSystem, view, sharedPerformances);

        ep = new EntertainmentProvider("ep@test.com", "password", "Test Org", "B1236285749", "Contact", "description");
        ep.setLoggedIn(true);
        epController.setCurrentUser(ep);

        student = new Student("student@test.com", "password");
        student.setLoggedIn(true);

        admin = new AdminStaff("admin@test.com", "password");
        admin.setLoggedIn(true);
    }

    // Helper: stub the minimal inputs to create an event without a performance
    private void stubCreateEventInputs(String title, String type, String isTicketed) {
        when(view.getInput("Enter title of event")).thenReturn(title);
        when(view.getInput("Enter event type (Music, Theatre, Dance, Movie, Sports)")).thenReturn(type);
        when(view.getInput("Is the event ticketed? (true/false)")).thenReturn(isTicketed);
        when(view.getInput("Would you like to add a performance to this event? (true/false)")).thenReturn("false");
    }

    // Helper: stub one performance on top of the event inputs
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

    // --- Access control ---

    @Test
    @DisplayName("EP can create an event and sees success message")
    void testEPCanCreateEventDisplaysSuccess() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        epController.createEvent();

        verify(view).displaySuccess(contains("Test Concert"));
    }

    @Test
    @DisplayName("Student cannot create an event — sees error")
    void testStudentCannotCreateEvent() {
        epController.setCurrentUser(student);

        Event result = epController.createEvent();

        assertNull(result, "Student should not be able to create an event");
        verify(view).displayError("Must be an entertainment provider to create an event");
    }

    @Test
    @DisplayName("Admin cannot create an event — sees error")
    void testAdminCannotCreateEvent() {
        epController.setCurrentUser(admin);

        Event result = epController.createEvent();

        assertNull(result, "Admin should not be able to create an event");
        verify(view).displayError("Must be an entertainment provider to create an event");
    }

    @Test
    @DisplayName("Guest (null user) cannot create an event — error and NPE thrown")
    void testLoggedOutUserCannotCreateEvent() {
        epController.setCurrentUser(null);

        assertThrows(NullPointerException.class, () -> epController.createEvent());
        verify(view).displayError("Must be logged in to create an event");
    }

    // --- Successful event creation ---

    @Test
    @DisplayName("Created event has the correct title")
    void testCreatedEventHasCorrectTitle() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        Event result = epController.createEvent();

        assertEquals("Test Concert", result.getTitle());
    }

    @Test
    @DisplayName("Created event has the correct type")
    void testCreatedEventHasCorrectType() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        Event result = epController.createEvent();

        assertEquals(EventType.MUSIC, result.getType());
    }

    @Test
    @DisplayName("Created event is ticketed when EP selects true")
    void testCreatedEventIsTicketed() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        Event result = epController.createEvent();

        assertTrue(result.isTicketed());
    }

    @Test
    @DisplayName("Created event is non-ticketed when EP selects false")
    void testCreatedEventIsNonTicketed() {
        stubCreateEventInputs("Free Show", "Dance", "false");

        Event result = epController.createEvent();

        assertFalse(result.isTicketed());
    }

    @Test
    @DisplayName("Created event belongs to the correct EP")
    void testCreatedEventBelongsToEP() {
        stubCreateEventInputs("Test Concert", "Music", "true");

        Event result = epController.createEvent();

        assertEquals("ep@test.com", result.getOrganizerEmail());
    }

    @Test
    @DisplayName("Creating events increments the event ID")
    void testCreatedEventsHaveIncrementingIDs() {
        stubCreateEventInputs("Concert One", "Music", "true");
        Event first = epController.createEvent();

        stubCreateEventInputs("Concert Two", "Theatre", "true");
        Event second = epController.createEvent();

        assertNotEquals(first.getEventID(), second.getEventID());
    }

    // --- Case-insensitive event type ---

    @Test
    @DisplayName("Event type input is case-insensitive")
    void testEventTypeInputIsCaseInsensitive() {
        stubCreateEventInputs("Test Concert", "music", "true");

        Event result = epController.createEvent();

        assertEquals(EventType.MUSIC, result.getType());
    }

    @Test
    @DisplayName("Event type input in uppercase works")
    void testEventTypeInputUppercase() {
        stubCreateEventInputs("Test Concert", "THEATRE", "true");

        Event result = epController.createEvent();

        assertEquals(EventType.THEATRE, result.getType());
    }

    // --- Event with performance ---

    @Test
    @DisplayName("EP can create an event with a performance — sees success messages")
    void testEPCanCreateEventWithPerformance() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        verify(view).displaySuccess(contains("Jazz Night"));
        verify(view).displaySuccess(contains("Performance added successfully"));
    }

    @Test
    @DisplayName("Performance is added to shared collection after event creation")
    void testPerformanceAddedToSharedCollection() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        assertEquals(1, sharedPerformances.size(), "Shared collection should have 1 performance");
    }

    @Test
    @DisplayName("Created performance has the correct venue address")
    void testCreatedPerformanceHasCorrectVenueAddress() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        Performance p = sharedPerformances.iterator().next();
        assertEquals("100 Main Street", p.getVenueAddress());
    }

    @Test
    @DisplayName("Created performance has the correct ticket price")
    void testCreatedPerformanceHasCorrectTicketPrice() {
        stubCreateEventWithOnePerformance("Jazz Night", "Music", "true");

        epController.createEvent();

        Performance p = sharedPerformances.iterator().next();
        assertEquals(25.00, p.getTicketPrice(), 0.001);
    }

    // --- Multiple event types ---

    @Test
    @DisplayName("Theatre event type is set correctly")
    void testTheatreEventType() {
        stubCreateEventInputs("A Play", "Theatre", "true");

        Event result = epController.createEvent();

        assertEquals(EventType.THEATRE, result.getType());
    }

    @Test
    @DisplayName("Sports event type is set correctly")
    void testSportsEventType() {
        stubCreateEventInputs("A Match", "Sports", "false");

        Event result = epController.createEvent();

        assertEquals(EventType.SPORTS, result.getType());
    }

    @Test
    @DisplayName("Multiple events can be created by the same EP")
    void testMultipleEventsBySameEP() {
        stubCreateEventInputs("Concert", "Music", "true");
        epController.createEvent();

        stubCreateEventInputs("Play", "Theatre", "true");
        epController.createEvent();

        verify(view).displaySuccess(contains("Concert"));
        verify(view).displaySuccess(contains("Play"));
    }
}
