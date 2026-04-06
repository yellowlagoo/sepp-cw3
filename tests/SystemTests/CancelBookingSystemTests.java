package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.external.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CancelBookingSystemTests {

    private TextUserInterface view;
    private BookingController bookingController;
    private MockPaymentSystem paymentSystem;
    private Collection<Performance> allPerformances;
    private EntertainmentProvider ep;
    private Event event;
    private Performance futurePerformance;
    private Performance soonPerformance;
    private Student student;
    private Booking booking;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        paymentSystem = new MockPaymentSystem();
        allPerformances = new ArrayList<>();
        bookingController = new BookingController(paymentSystem, view, allPerformances);

        ep = new EntertainmentProvider("ep@test.com", "password123", "Test Org", "B9999", "Alice", "Test org desc");
        event = new Event(ep, 1, "Jazz Night", EventType.MUSIC, true);

        // far future - can cancel 
        futurePerformance = new Performance(1,
                LocalDateTime.of(2030, 6, 15, 19, 30),
                LocalDateTime.of(2030, 6, 15, 22, 0),
                Arrays.asList("John"), "Edinburgh Venue, EH1 1AA",
                100, false, false, 50, 20.00, event);

        // Within 24 hours — cant cancel
        soonPerformance = new Performance(2,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3),
                Arrays.asList("John"), "Edinburgh Venue, EH1 1AA",
                100, false, false, 50, 20.00, event);

        allPerformances.add(futurePerformance);
        allPerformances.add(soonPerformance);

        student = new Student("bob@hindeburgh.ed.ac.uk", "monkeys99$");
        student.setLoggedIn(true);
        bookingController.setCurrentUser(student);

        booking = new Booking(0, 2, 40.00, LocalDateTime.now(), student, futurePerformance);
        futurePerformance.addBooking(booking);
    }

    //  Helpers

    private void loginAsAdmin() {
        AdminStaff admin = new AdminStaff("AdminStaff@ed.ac.uk", "password1");
        admin.setLoggedIn(true);
        bookingController.setCurrentUser(admin);
    }

    private void loginAsEP() {
        ep.setLoggedIn(true);
        bookingController.setCurrentUser(ep);
    }

    //  Cancel booking — success
    @Test
    @DisplayName("Testing student can cancel their booking")
    void testStudentCanCancelBooking() {
        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("0");

        bookingController.cancelBooking();

        verify(view).displaySuccess("The booking for the booking ID 0 was successfully cancelled and refunded");
    }

    @Test
    @DisplayName("Testing booking status is CANCELLEDBYSTUDENT after cancellation")
    void testBookingStatusIsUpdatedAfterCancellation() {
        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("0");

        bookingController.cancelBooking();

        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus());
    }

    //  Cancel booking — refund fails

    @Test
    @DisplayName("Testing error shown when refund fails")
    void testRefundFailureShowsError() {
        PaymentSystem failingPayment = mock(PaymentSystem.class);
        when(failingPayment.processRefund(anyInt(), anyString(), anyString(), anyInt(), anyString(), anyDouble(), anyString()))
                .thenReturn(false);
        bookingController = new BookingController(failingPayment, view, allPerformances);
        bookingController.setCurrentUser(student);

        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("0");

        bookingController.cancelBooking();

        verify(view).displayError("The booking was not successfully cancelled or refunded. Please try again later.");
    }

    @Test
    @DisplayName("Testing booking status is not CANCELLEDBYSTUDENT when refund fails")
    void testBookingStatusNotUpdatedWhenRefundFails() {
        PaymentSystem failingPayment = mock(PaymentSystem.class);
        when(failingPayment.processRefund(anyInt(), anyString(), anyString(), anyInt(), anyString(), anyDouble(), anyString()))
                .thenReturn(false);
        bookingController = new BookingController(failingPayment, view, allPerformances);
        bookingController.setCurrentUser(student);

        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("0");

        bookingController.cancelBooking();

        assertNotEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus());
    }

    //  Cancel booking — performance within 24 hours

    @Test
    @DisplayName("Testing error shown when performance is less than 24 hours away")
    void testCancellationWithin24HoursShowsError() {
        Booking soonBooking = new Booking(1, 1, 20.00, LocalDateTime.now(), student, soonPerformance);
        soonPerformance.addBooking(soonBooking);

        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("1");

        bookingController.cancelBooking();

        verify(view).displayError("Bookings can only be cancelled if they are at least 24 hours away.");
    }

    @Test
    @DisplayName("Testing displaySuccess is not called when performance is within 24 hours")
    void testSuccessNotCalledWhenWithin24Hours() {
        Booking soonBooking = new Booking(1, 1, 20.00, LocalDateTime.now(), student, soonPerformance);
        soonPerformance.addBooking(soonBooking);

        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("1");

        bookingController.cancelBooking();

        verify(view, never()).displaySuccess(anyString());
    }

    //  Cancel booking — invalid booking ID

    @Test
    @DisplayName("Testing error shown when booking ID does not exist, then valid ID accepted")
    void testInvalidBookingIdShowsError() {
        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("999").thenReturn("0");

        bookingController.cancelBooking();

        verify(view).displayError("Booking with given number does not exist. Please enter a valid booking number.");
    }

    //  Cancel booking — booking belongs to another student

    @Test
    @DisplayName("Testing error shown when booking belongs to a different student")
    void testBookingBelongingToAnotherStudentShowsError() {
        Student otherStudent = new Student("other@hindeburgh.ed.ac.uk", "pass99$");
        otherStudent.setLoggedIn(true);
        bookingController.setCurrentUser(otherStudent);

        // otherStudent's own booking to end the loop
        Booking otherBooking = new Booking(1, 1, 20.00, LocalDateTime.now(), otherStudent, futurePerformance);
        futurePerformance.addBooking(otherBooking);

        when(view.getInput("Enter the ID of the booking you want to cancel: ")).thenReturn("0").thenReturn("1");

        bookingController.cancelBooking();

        verify(view).displayError("This booking does not belong to you. Please enter a new booking number.");
    }

    //  Cancel booking — access control

    @Test
    @DisplayName("Testing EP cannot cancel a booking")
    void testEPCannotCancelBooking() {
        loginAsEP();

        assertThrows(IllegalArgumentException.class, () -> bookingController.cancelBooking());
        verify(view).displayError("Only students may cancel a booking");
    }

    @Test
    @DisplayName("Testing admin cannot cancel a booking")
    void testAdminCannotCancelBooking() {
        loginAsAdmin();

        assertThrows(IllegalArgumentException.class, () -> bookingController.cancelBooking());
        verify(view).displayError("Only students may cancel a booking");
    }

    @Test
    @DisplayName("Testing guest cannot cancel a booking")
    void testGuestCannotCancelBooking() {
        bookingController.setCurrentUser(null);

        assertThrows(NullPointerException.class, () -> bookingController.cancelBooking());
    }
}
