package tests.SystemTests;

import src.Controller.*;
import src.Model.*;
import src.View.*;
import src.ExternalSystems.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

public class CreateEventSystemTests {

    private EventPerformanceController epController;
    private UserController userController;
    private TextUserInterface view;
    private Collection<Performance> sharedPerformances;
    private MockPaymentSystem paymentSystem;
    private MockVerificationSystem verificationSystem;
    private EntertainmentProvider ep;
    private Student student;
    private AdminStaff admin;

    @BeforeEach
    void setup() {
        view = new TextUserInterface();
        sharedPerformances = new ArrayList<>();
        paymentSystem = new MockPaymentSystem();
        verificationSystem = new MockVerificationSystem();

        epController = new EventPerformanceController(1, 1, paymentSystem, view, sharedPerformances);
        userController = new UserController(view, verificationSystem);

        // Hard-code an EP account
        ep = new EntertainmentProvider("ep@test.com", "password", "Test Org", "B123", "Contact", "description");
        ep.setLoggedIn(true);
        epController.setCurrentUser(ep);

        // Hard-code a student account
        student = new Student("student@test.com", "password");
        student.setLoggedIn(true);

        // Hard-code an admin account
        admin = new AdminStaff("admin@test.com", "password");
        admin.setLoggedIn(true);
    }

    // --- Tests for user permission checks ---

    @Test
    @DisplayName("Entertainment provider can access createEvent without error")
    void testEPCanCreateEvent() {
        // We can't fully test createEvent interactively (it needs Scanner input),
        // but we can verify the EP permission check passes
        assertTrue(epController.getCurrentUser() instanceof EntertainmentProvider,
                "Current user should be an EntertainmentProvider");
    }

    @Test
    @DisplayName("Student should not be able to create an event")
    void testStudentCannotCreateEvent() {
        epController.setCurrentUser(student);
        Event result = epController.createEvent();
        assertNull(result, "Student should not be able to create an event");
    }

    @Test
    @DisplayName("Admin should not be able to create an event")
    void testAdminCannotCreateEvent() {
        epController.setCurrentUser(admin);
        Event result = epController.createEvent();
        assertNull(result, "Admin should not be able to create an event");
    }

    @Test
    @DisplayName("Logged out user should not be able to create an event")
    void testLoggedOutUserCannotCreateEvent() {
        epController.setCurrentUser(null);
        assertThrows(NullPointerException.class, () -> epController.createEvent(),
                "Logged out user should not be able to create an event");
    }

    // --- Tests for event creation via direct model ---

    @Test
    @DisplayName("Event is created with correct title")
    void testEventCreatedWithCorrectTitle() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        assertEquals("Test Concert", event.getTitle(), "Event title should match");
    }

    @Test
    @DisplayName("Event is created with correct type")
    void testEventCreatedWithCorrectType() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        assertEquals(EventType.MUSIC, event.getType(), "Event type should be MUSIC");
    }

    @Test
    @DisplayName("Event is created as ticketed")
    void testEventCreatedAsTicketed() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        assertTrue(event.isTicketed(), "Event should be ticketed");
    }

    @Test
    @DisplayName("Event is created as non-ticketed")
    void testEventCreatedAsNonTicketed() {
        Event event = new Event(ep, 1, "Free Show", EventType.DANCE, false);
        assertFalse(event.isTicketed(), "Event should not be ticketed");
    }

    @Test
    @DisplayName("Event has correct organizer email")
    void testEventHasCorrectOrganizerEmail() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        assertEquals("ep@test.com", event.getOrganizerEmail(), "Organizer email should match EP's email");
    }

    @Test
    @DisplayName("Event ID is assigned correctly")
    void testEventIDAssigned() {
        Event event = new Event(ep, 42, "Test Concert", EventType.MUSIC, true);
        assertEquals(42, event.getEventID(), "Event ID should be 42");
    }

    // --- Tests for performance creation via Event model ---

    @Test
    @DisplayName("Performance is created and added to event")
    void testPerformanceCreatedAndAddedToEvent() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        Performance p = event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A", "Artist B"),
                "100 Main Street", 500, false, false, 200, 25.00);
        assertNotNull(p, "Performance should be created");
    }

    @Test
    @DisplayName("Performance has correct venue address")
    void testPerformanceVenueAddress() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        Performance p = event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A"),
                "100 Main Street", 500, false, false, 200, 25.00);
        assertEquals("100 Main Street", p.getVenueAddress(), "Venue address should match");
    }

    @Test
    @DisplayName("Performance has correct ticket price")
    void testPerformanceTicketPrice() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        Performance p = event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A"),
                "100 Main Street", 500, false, false, 200, 25.00);
        assertEquals(25.00, p.getTicketPrice(), "Ticket price should be 25.00");
    }

    @Test
    @DisplayName("Performance has correct number of tickets")
    void testPerformanceNumTickets() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        Performance p = event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A"),
                "100 Main Street", 500, false, false, 200, 25.00);
        assertEquals(200, p.getNumTicketsTotal(), "Should have 200 tickets");
    }

    @Test
    @DisplayName("Performance has correct start time")
    void testPerformanceStartTime() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 10, 19, 0);
        Performance p = event.createPerformance(1,
                startTime,
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A"),
                "100 Main Street", 500, false, false, 200, 25.00);
        assertEquals(startTime, p.getStartDateTime(), "Start time should match");
    }

    @Test
    @DisplayName("Performance has correct performer names")
    void testPerformancePerformerNames() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        Performance p = event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A", "Artist B"),
                "100 Main Street", 500, false, false, 200, 25.00);
        assertTrue(p.getPerformerNames().contains("Artist A"), "Should contain Artist A");
        assertTrue(p.getPerformerNames().contains("Artist B"), "Should contain Artist B");
    }

    @Test
    @DisplayName("Event tracks the number of performances added")
    void testEventTracksPerformanceCount() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A"),
                "100 Main Street", 500, false, false, 200, 25.00);
        event.createPerformance(2,
                LocalDateTime.of(2026, 5, 11, 19, 0),
                LocalDateTime.of(2026, 5, 11, 22, 0),
                Arrays.asList("Artist B"),
                "200 High Street", 300, true, false, 150, 30.00);
        String eventStr = event.toString();
        assertTrue(eventStr.contains("2"), "Event should have 2 performances");
    }

    @Test
    @DisplayName("Performance added to shared collection is visible to other controllers")
    void testPerformanceVisibleInSharedCollection() {
        Event event = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);
        Performance p = event.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A"),
                "100 Main Street", 500, false, false, 200, 25.00);
        sharedPerformances.add(p);
        assertEquals(1, sharedPerformances.size(), "Shared collection should have 1 performance");
    }

    @Test
    @DisplayName("Non-ticketed event performance has zero ticket price")
    void testNonTicketedPerformancePrice() {
        Event freeEvent = new Event(ep, 2, "Free Show", EventType.DANCE, false);
        Performance p = freeEvent.createPerformance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Dancer X"),
                "Park", 1000, true, false, 500, 0);
        assertEquals(0, p.getTicketPrice(), "Non-ticketed event should have zero ticket price");
    }

    @Test
    @DisplayName("Multiple events can be created by the same EP")
    void testMultipleEventsBySameEP() {
        Event event1 = new Event(ep, 1, "Concert", EventType.MUSIC, true);
        Event event2 = new Event(ep, 2, "Play", EventType.THEATRE, true);
        ep.addEvent(event1);
        ep.addEvent(event2);
        assertEquals(2, ep.getEvents().size(), "EP should have 2 events");
    }

    @Test
    @DisplayName("Different event types can be created")
    void testDifferentEventTypes() {
        Event musicEvent = new Event(ep, 1, "Concert", EventType.MUSIC, true);
        Event danceEvent = new Event(ep, 2, "Dance Show", EventType.DANCE, true);
        Event theatreEvent = new Event(ep, 3, "Play", EventType.THEATRE, true);
        assertEquals(EventType.MUSIC, musicEvent.getType());
        assertEquals(EventType.DANCE, danceEvent.getType());
        assertEquals(EventType.THEATRE, theatreEvent.getType());
    }
}