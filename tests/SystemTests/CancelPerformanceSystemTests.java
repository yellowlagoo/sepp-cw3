package tests.SystemTests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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

public class CancelPerformanceSystemTests {

        private TextUserInterface view;
        private EventPerformanceController eventPerformanceController;
        private Collection<Performance> allPerformances;
        private MockPaymentSystem paymentSystem;
        private Performance performanceOne;
        private Performance performanceTwo;
        private Performance performanceThree;
        private Performance performanceNew;
        private Performance performancePast;
        private EntertainmentProvider ep;
        private Event event;
        private Event event_new;

        
        @BeforeAll
        static void initAll() {
         System.out.println("Testing for CancelPerformance use case started");
                System.out.println("--------------------------------");
        }

        @AfterEach
        void betweenTests() {
                System.out.println("--------------------------------");
        }

        @AfterAll
        static void tearDownAll() {
                System.out.println("Testing for CancelPerformance use case completed");
        }

        @BeforeEach
        void setup() {

                view = mock(TextUserInterface.class);
                paymentSystem = new MockPaymentSystem();
                allPerformances = new ArrayList<>();
                eventPerformanceController = new EventPerformanceController(1, 1, paymentSystem, view, allPerformances);

                ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123563854",
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
                Performance performanceFour = new Performance(989, LocalDateTime.of(2030, 4, 23, 16, 30),
                                LocalDateTime.of(2030, 4, 23, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);

                allPerformances.add(performanceOne);
                allPerformances.add(performanceTwo);
                allPerformances.add(performanceThree);
                allPerformances.add(performanceFour);

                eventPerformanceController.setCurrentUser(ep);
        }

        // testing if ep can sucesfully cancel their event
        @Test
        @DisplayName("check values for ep cancelling a performance successfully")
        void testEPCancellingTheirPerformance() {

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                verify(view).displaySuccess("Cancellation successful!");

        }

        // testing invalid ID is handled
        @Test
        @DisplayName("check values for ep providing an invalid ID")
        void testEPCancellingNonExistenPerformance() {

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("100001312").thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                verify(view).displayError("Performance with given number does not exist");
        }

        // checking non-numbered ID is inputted
        @Test
        @DisplayName("check values for ep cancelling a performance with a non integer id")
        void testEPCancellingNonIntID() {

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("not a number").thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                verify(view).displayError("Performance ID must be a number");
        }

        // tesing an ep cannot cancel a performance which does not belong to them
        @Test
        @DisplayName("checking value for EP cancelling a performance which does not belong to them")
        void testCancellingPerformanceWhichEPDoesNotOwn() {

                EntertainmentProvider ep_new = new EntertainmentProvider("Newep@test.com", "password",
                                "Organisation mumber two", "B123563854", "New EP",
                                "New ep for cancelling performance tests");

                event_new = new Event(ep_new, 19245, "Test", EventType.SPORTS, true);

                performanceNew = new Performance(19743, LocalDateTime.of(2030, 6, 20, 16, 30),
                                LocalDateTime.of(2030, 6, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event_new);

                allPerformances.add(performanceNew);

                ep_new.setLoggedIn(true);
                eventPerformanceController.setCurrentUser(ep_new);

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("123").thenReturn("19743");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                verify(view).displayError("The performance with given number does not belong to you");
        }

        // testing that a performance that has already occured cannot be cancelled
        @Test
        @DisplayName("checking value for cancelling a performance which has already occured")
        void testCancellingPastPerformances() {

                ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123563854",
                                "Michael",
                                "Testing ep");

                event = new Event(ep, 12347, "Test Event", EventType.DANCE, true);

                performancePast = new Performance(1944242, LocalDateTime.of(2015, 4, 20, 16, 30),
                                LocalDateTime.of(2015, 4, 20, 20, 30), Arrays.asList("performer Names"), "12 adress",
                                100, false, false,
                                100, 50.00, event);

                allPerformances.add(performancePast);

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("1944242").thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                verify(view).displayError("Performance can't be cancelled as it has already happened");
        }

        // testing for empty message handling
        @Test
        @DisplayName("checking value when empty ep message is provided")
        void testEmptyEPMessage() {

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("").thenReturn("non empty message");

                eventPerformanceController.cancelPerformance();

                verify(view).displayError("Please provide a non-empty cancellation message for the students");

        }

        // testing if status is updated after cancellation
        @Test
        @DisplayName("check values for status is updated after cancelling")
        void testPerformanceStatusAfterCancelled() {

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                assertEquals(PerformanceStatus.CANCELLED, performanceThree.getStatus(),
                                "status should be updated to cancelled");
        }

        // testing booking status is updated
        @Test
        @DisplayName("checking booking status after performance cancelled")
        void testBookingStatusUpdated() {

                Student student = new Student("student@hidenburgh.ed.ac.uk", "secretpassword123");
                student.setName("Michael");
                student.setPhoneNumber(131443131);

                Booking booking = new Booking(1, 1, 50.0, LocalDateTime.now(), student, performanceThree);
                performanceThree.addBooking(booking);

                when(view.getInput("Enter ID of performance to cancel")).thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                assertEquals(BookingStatus.CANCELLEDBYPROVIDER, booking.getStatus(),
                                "status should be updated to cancelled");
        }

        // testing that only an EP can cancel a performance
        @Test
        @DisplayName("checking booking status after performance cancelled")
        void testNonEPCannotCancelPerformances() {

                Student student = new Student("student@hidenburgh.ed.ac.uk", "secretpassword123");
                student.setName("Michael");
                student.setPhoneNumber(131443131);

                eventPerformanceController.setCurrentUser(student);
                student.setLoggedIn(true);

                
                when(view.getInput("Enter ID of performance to cancel")).thenReturn("123");
                when(view.getInput("Provide a cancellation message for affected students:"))
                                .thenReturn("This is a test cancellation message");

                eventPerformanceController.cancelPerformance();

                verify(view).displayError("Only an entertainment provider can cancel a performance");

              
        }

}