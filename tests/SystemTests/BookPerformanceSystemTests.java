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

public class BookPerformanceSystemTests {

    private TextUserInterface view;
    private BookingController bookingController;
    private Collection<Performance> allPerformances;
    private MockPaymentSystem paymentSystem;
    private Performance performanceOne;
    private Performance performanceTwo;
    private Performance performanceThree;
    private EntertainmentProvider ep;
    private Event event;
    private Student student;

    @BeforeAll
    static void initAll() {
        System.out.println("Testing for BookPerformance use case started");
        System.out.println("--------------------------------");
    }

    @AfterEach
    void betweenTests() {
        System.out.println("--------------------------------");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Testing for BookPerformance use case completed");
    }

    @BeforeEach
    void setup() {

        view = mock(TextUserInterface.class);
        paymentSystem = new MockPaymentSystem();
        allPerformances = new ArrayList<>();
        bookingController = new BookingController(paymentSystem, view, allPerformances);

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

        student = new Student("Student@Hidenburgh.ed.ac.uk", "password123");
        student.setLoggedIn(true);
        bookingController.setCurrentUser(student);

    }

    // test that students can book a performance successfully
    @Test
    @DisplayName("checking if values returned for book performance are correct")
    void testBookPerformanceCorrectValues() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displaySuccess("Booking successful");
    }

    // checking booking record is returned
    @Test
    @DisplayName("checking if values for booking record are retured")
    void testBookPerformanceCorrectBookingRecord() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayBookingRecord(any());
    }

    // test for when student provides an invalid performance ID
    @Test
    @DisplayName("checking correct value for student providing invalid performance ID")
    void testStudentProvidesInvalidID() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("5912").thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Performance with given number does not exist.");
    }

    // Student requests too many tickets which exceeds tickets left
    @Test
    @DisplayName("checking correct value for student providing invalid number of tickets")
    void testStudentProvidesInvalidNumberOfTickets() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("1");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1000").thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Requested performance has no tickets left.");
    }

    // Student requests null tickets
    @Test
    @DisplayName("checking correct value for student providing invalid number of tickets")
    void testStudentProvidesNullTickets() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("1");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn(null).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Number of tickets cannot be empty. Please enter another number:");
    }

    // Student requests empty tickets
    @Test
    @DisplayName("checking correct value for student providing invalid number of tickets")
    void testStudentProvidesEmptyTickets() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("1");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("").thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Number of tickets cannot be empty. Please enter another number:");
    }

    // test for when a Student attempts to book non-ticketed performance
    @Test
    @DisplayName("checking value for student booking a non-ticketed performance")
    void testStudentBooksNonTicketedPerformance() {

        ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123", "Michael",
                "Testing ep");
        event = new Event(ep, 123, "Test Event", EventType.MOVIE, false);

        Performance performanceFour = new Performance(989, LocalDateTime.of(2030, 4, 23, 16, 30),
                LocalDateTime.of(2030, 4, 23, 20, 30), Arrays.asList("performer Names"), "12 adress", 100, false, false,
                100, 50.00, event);

        allPerformances.add(performanceFour);

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("989");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("The requested performance's event is not ticketed. There is no need to book it.");
    }

    // testing that empty name is handled
    @Test
    @DisplayName("checking value for an empty name input")
    void testEmptyName() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("").thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Name cannot be empty. Please enter your name.");
    }

    // testing that null name is handled
    @Test
    @DisplayName("checking value for an null name input")
    void testNullName() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn(null).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Name cannot be empty. Please enter your name.");
    }

    // testing that empty phone number is handled
    @Test
    @DisplayName("checking value for an empty phoneNumber")
    void testEmptyPhoneNumber() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("").thenReturn("01313131");

        bookingController.bookPerformance();

        verify(view).displayError("Phone number cannot be empty. Please enter your phone number.");
    }

    // testing that null phone number is handled
    @Test
    @DisplayName("checking value for a null phoneNumber")
    void testNullPhoneNumber() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn(null).thenReturn("01313131");

        bookingController.bookPerformance();

        verify(view).displayError("Phone number cannot be empty. Please enter your phone number.");
    }

    // test tickets have been registered as sold
    @Test
    @DisplayName("checking if values returned for book performance are correct")
    void testNumTicketsSold() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("5");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        assertEquals(5, performanceTwo.getNumTicketsSold(), "5 tickets should have been sold");
    }

    // test non-students cannot book performances
    @Test
    @DisplayName("checking value for when a non-studetn attempts to book a performance")
    void testNonStudentBooksPerformance(){

        ep = new EntertainmentProvider("ep@test.com", "secretPassword123", "OrganistionforTesting", "A123456789", "Businesscorp", "testing ep");
        bookingController.setCurrentUser(ep);
        ep.setLoggedIn(true);

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("5");
        when(view.getInput("Enter your name:")).thenReturn("definitelyNotAnEP");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Only students may book a performance");

    }

    // testing booking number increases after booking 
    @Test
    @DisplayName("checking value for booking number increases after a booking")
    void testBookingNumberIncrements(){

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("5");
        when(view.getInput("Enter your name:")).thenReturn("definitelyNotAnEP");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();
        bookingController.bookPerformance();
        bookingController.bookPerformance();

        assertEquals(3, bookingController.getNextBookingNumber(), "Booking number should be incremented each time a booking is made");

    }
    

}
