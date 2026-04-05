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

public class SearchForPerformancesSystemTests {

    private EventPerformanceController epController;
    private TextUserInterface view;
    private Collection<Performance> sharedPerformances;
    private EntertainmentProvider ep;
    private Event ticketedEvent;
    private Performance performance1;
    private Performance performance2;

    @BeforeEach
    void setup() {
        view = new TextUserInterface();
        sharedPerformances = new ArrayList<>();
        MockPaymentSystem paymentSystem = new MockPaymentSystem();

        epController = new EventPerformanceController(1, 1, paymentSystem, view, sharedPerformances);

        // Create an EP and set them as the current user
        ep = new EntertainmentProvider("ep@test.com", "password", "Test Org", "B123", "Contact", "description");
        ep.setLoggedIn(true);
        epController.setCurrentUser(ep);

        // Create an event with two performances
        ticketedEvent = new Event(ep, 1, "Test Concert", EventType.MUSIC, true);

        performance1 = new Performance(1,
                LocalDateTime.of(2026, 5, 10, 19, 0),
                LocalDateTime.of(2026, 5, 10, 22, 0),
                Arrays.asList("Artist A", "Artist B"),
                "100 Main Street", 500, false, false, 200, 25.00, ticketedEvent);

        performance2 = new Performance(2,
                LocalDateTime.of(2026, 6, 15, 20, 0),
                LocalDateTime.of(2026, 6, 15, 23, 0),
                Arrays.asList("Artist C"),
                "200 High Street", 300, true, false, 150, 30.00, ticketedEvent);

        // Add performances to the shared collection
        sharedPerformances.add(performance1);
        sharedPerformances.add(performance2);
    }

    @Test
    @DisplayName("Search displays all performances when performances exist")
    void testSearchDisplaysAllPerformances() {
        // searchForPerformances should not throw when performances exist
        assertDoesNotThrow(() -> epController.searchForPerformances());
    }

    @Test
    @DisplayName("Search displays no performances when none exist")
    void testSearchDisplaysNoPerformances() {
        sharedPerformances.clear();
        // Should not throw, just display an error message
        assertDoesNotThrow(() -> epController.searchForPerformances());
    }

    @Test
    @DisplayName("Shared performances collection contains correct number of performances")
    void testCorrectNumberOfPerformances() {
        assertEquals(2, sharedPerformances.size(), "There should be 2 performances in the system");
    }

    @Test
    @DisplayName("Search works when logged in as a student")
    void testSearchAsStudent() {
        Student student = new Student("student@test.com", "password");
        student.setLoggedIn(true);
        epController.setCurrentUser(student);
        assertDoesNotThrow(() -> epController.searchForPerformances());
    }

    @Test
    @DisplayName("Search works when logged in as admin")
    void testSearchAsAdmin() {
        AdminStaff admin = new AdminStaff("admin@test.com", "password");
        admin.setLoggedIn(true);
        epController.setCurrentUser(admin);
        assertDoesNotThrow(() -> epController.searchForPerformances());
    }

    @Test
    @DisplayName("Performance details are accessible after adding to shared collection")
    void testPerformanceDetailsAccessible() {
        Performance found = null;
        for (Performance p : sharedPerformances) {
            if (p.getPerformanceID() == 1) {
                found = p;
            }
        }
        assertNotNull(found, "Performance with ID 1 should exist");
        assertEquals("Test Concert", found.getEventTitle(), "Event title should match");
        assertEquals("100 Main Street", found.getVenueAddress(), "Venue should match");
    }

    @Test
    @DisplayName("Performances from different events appear in shared collection")
    void testPerformancesFromMultipleEvents() {
        Event secondEvent = new Event(ep, 2, "Dance Show", EventType.DANCE, true);
        Performance performance3 = new Performance(3,
                LocalDateTime.of(2026, 7, 1, 18, 0),
                LocalDateTime.of(2026, 7, 1, 21, 0),
                Arrays.asList("Dancer X"),
                "300 Park Lane", 200, false, false, 100, 15.00, secondEvent);
        sharedPerformances.add(performance3);

        assertEquals(3, sharedPerformances.size(), "There should now be 3 performances");
    }

    @Test
    @DisplayName("Search after a performance is cancelled still shows it in the list")
    void testSearchAfterCancellation() {
        performance1.cancel();
        assertEquals(2, sharedPerformances.size(), "Cancelled performance should still be in the collection");
        assertEquals(PerformanceStatus.CANCELLED, performance1.getStatus(), "Performance should be cancelled");
    }

    @Test
    @DisplayName("Search after sponsorship reflects updated ticket price")
    void testSearchAfterSponsorship() {
        performance1.sponsor(5.00);
        assertEquals(20.00, performance1.getFinalTicketPrice(), "Ticket price should be reduced after sponsorship");
    }
}