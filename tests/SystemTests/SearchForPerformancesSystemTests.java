package tests.SystemTests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import src.external.*;

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

        @BeforeAll
        static void initAll() {
                System.out.println("Testing for SearchForPerformances use case started");
                System.out.println("--------------------------------");
        }

        @BeforeEach
        void setup() {

                view = mock(TextUserInterface.class);
                paymentSystem = new MockPaymentSystem();
                allPerformances = new ArrayList<>();
                eventPerformanceController = new EventPerformanceController(1, 1234, paymentSystem, view,
                                allPerformances);

        }

        @AfterEach
        void betweenTests() {
                System.out.println("--------------------------------");
        }

        @AfterAll
        static void tearDownAll() {
                System.out.println("Testing for SearchForPerformances use case completed");
        }

        // testing when no performance exists in the system
        @Test
        @DisplayName("checking correct values for when no performances exist")
        void testPerformancesDoNotExist() {

                eventPerformanceController.searchForPerformances();
                verify(view).displayError("There are no performances in the system.");

        }

        // testing when performances are in the system
        @Test
        @DisplayName("checking correct values for when performances exist")
        void testPerformancesExist() {

                ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123678596",
                                "Michael",
                                "Testing ep");
                event = new Event(ep, 1234567, "Test Event", EventType.DANCE, true);
                performanceOne = new Performance(1, LocalDateTime.of(2030, 4, 20, 16, 30),
                                LocalDateTime.of(2030, 4, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);
                performanceTwo = new Performance(2, LocalDateTime.of(2030, 4, 21, 16, 30),
                                LocalDateTime.of(2030, 5, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);
                performanceThree = new Performance(123, LocalDateTime.of(2030, 4, 22, 16, 30),
                                LocalDateTime.of(2030, 4, 22, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);

                allPerformances.add(performanceOne);
                allPerformances.add(performanceTwo);
                allPerformances.add(performanceThree);

                eventPerformanceController.searchForPerformances();
                verify(view).displayListofPerformances(any());
        }

        // testing that all performances are added
        @Test
        @DisplayName("checking correct values for when performances exist")
        void testPerformancesContainsAllPerformances() {

                ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123678596",
                                "Michael",
                                "Testing ep");
                event = new Event(ep, 1234567, "Test Event", EventType.DANCE, true);
                performanceOne = new Performance(1, LocalDateTime.of(2030, 4, 20, 16, 30),
                                LocalDateTime.of(2030, 4, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);
                performanceTwo = new Performance(2, LocalDateTime.of(2030, 4, 21, 16, 30),
                                LocalDateTime.of(2030, 5, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);
                performanceThree = new Performance(123, LocalDateTime.of(2030, 4, 22, 16, 30),
                                LocalDateTime.of(2030, 4, 22, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);

                allPerformances.add(performanceOne);
                allPerformances.add(performanceTwo);
                allPerformances.add(performanceThree);

                eventPerformanceController.searchForPerformances();
                assertEquals(3, allPerformances.size(), "should contain the 3 performances");
        }

        // testing that cancelled performances are not displayed and that if all performances are cancelled then the error is handled
        @Test
        @DisplayName("checking correct values that cancelled Performances are not displayed")
        void testCancelledPerformancesNotDisplayed() {

                ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123678596",
                                "Michael",
                                "Testing ep");
                event = new Event(ep, 1234567, "Test Event", EventType.DANCE, true);
                performanceOne = new Performance(1, LocalDateTime.of(2030, 4, 20, 16, 30),
                                LocalDateTime.of(2030, 4, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);
                

                allPerformances.add(performanceOne);
                performanceOne.cancel();
                
                eventPerformanceController.searchForPerformances();
                verify(view).displayError("There are no performances in the system.");

        }
}