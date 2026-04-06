package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.ExternalSystems.*;

public class SearchForPerformancesSystemTests {

    private TextUserInterface view;
    private EventPerformanceController eventPerformanceController;
    private Collection<Performance> allPerformances;
    private MockPaymentSystem paymentSystem;
    private Performance performanceOne;
    private Performance performanceTwo;
    private Performance performanceThree;
    private EntertainmentProvider ep;
    private Event event;

    @BeforeEach
    void setup() {

        view = mock(TextUserInterface.class);
        paymentSystem = new MockPaymentSystem();
        allPerformances = new ArrayList<>();
        eventPerformanceController = new EventPerformanceController(1, 1234, paymentSystem, view, allPerformances);

    }

    @Test
    @DisplayName("checking correct values for when no performances exist")
    void testPerformancesDoNotExist() {

        eventPerformanceController.searchForPerformances();
        verify(view).displayError("There are no performances in the system.");

    }

    @Test
    @DisplayName("checking correct values for when performances exist")
    void testPerformancesExist() {

        ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123", "Michael",
                "Testing ep");
        event = new Event(ep, 1234567, "Test Event", EventType.DANCE, true);
        performanceOne = new Performance(1, LocalDateTime.of(2030, 4, 20, 16, 30),
                LocalDateTime.of(2030, 4, 20, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);
        performanceTwo = new Performance(2, LocalDateTime.of(2030, 4, 21, 16, 30),
                LocalDateTime.of(2030, 5, 20, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);
        performanceThree = new Performance(123, LocalDateTime.of(2030, 4, 22, 16, 30),
                LocalDateTime.of(2030, 4, 22, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);

        allPerformances.add(performanceOne);
        allPerformances.add(performanceTwo);
        allPerformances.add(performanceThree);

        eventPerformanceController.searchForPerformances();
        verify(view).displayListofPerformances(any());
    }

    @Test
    @DisplayName("checking correct values for when performances exist")
    void testPerformancesContainsAllPerformances() {

        ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123", "Michael",
                "Testing ep");
        event = new Event(ep, 1234567, "Test Event", EventType.DANCE, true);
        performanceOne = new Performance(1, LocalDateTime.of(2030, 4, 20, 16, 30),
                LocalDateTime.of(2030, 4, 20, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);
        performanceTwo = new Performance(2, LocalDateTime.of(2030, 4, 21, 16, 30),
                LocalDateTime.of(2030, 5, 20, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);
        performanceThree = new Performance(123, LocalDateTime.of(2030, 4, 22, 16, 30),
                LocalDateTime.of(2030, 4, 22, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);

        allPerformances.add(performanceOne);
        allPerformances.add(performanceTwo);
        allPerformances.add(performanceThree);

        eventPerformanceController.searchForPerformances();
        assertEquals(3, allPerformances.size(), "should contain the 3 performances");
    }

}