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

    // studetn can book the performance test
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

    // checking booking record is also returned
    @Test
    @DisplayName("checking if values returned for book performance are correct")
    void testBookPerformanceCorrectBookingRecord() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayBookingRecord(any());
    }

    // student provides invalid id
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

    // Student provides invalid number of tickets
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

    // Student attempts to book non-ticketed performance
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

    // testing that empty name/ null name is handled
    @Test
    @DisplayName("checking value for an empty name input")
    void testEmptyName() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn(null).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn("123");

        bookingController.bookPerformance();

        verify(view).displayError("Name cannot be empty. Please enter your name.");
    }

    // testing that empty name/ null name is handled
    @Test
    @DisplayName("checking value for an empty phoneNumber")
    void testEmptyPhoneNumber() {

        when(view.getInput("Enter the ID of the performance you want to book:")).thenReturn("2");
        when(view.getInput("Enter the number of tickets you want to book:")).thenReturn("1");
        when(view.getInput("Enter your name:")).thenReturn("Michael");
        when(view.getInput("Enter your phone number:")).thenReturn(null).thenReturn("01313131");

        bookingController.bookPerformance();

        verify(view).displayError("Phone number cannot be empty. Please enter your phone number.");
    }

    // check tickets have been registered as sold
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

}
