package ModelTests;

import Model.*; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class TestBooking {
    private Booking booking;
    private Student student;
    private Performance performance;
    private Event event;

    @BeforeEach
    void setup() {
        //Initialize the student who created the booking
        student = new Student("student@test.com", "password123");

        //Initialize the event for the performance booked
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password", 
                            "Test Entertainment Provider", "A123", "Test", "description");
        Event event = new Event(ep, 1234, "Test Title", EventType.MUSIC, true);

        //Initialize performance booked
        LocalDateTime performanceStartTime = LocalDateTime.of(2026, 4, 20, 16, 30);
        LocalDateTime performanceEndTime = LocalDateTime.of(2026, 4, 20, 20, 30);
        Collection<String> performerNames = new ArrayList<>(List.of("Performer 1", "Performer 2"));
        Performance performance = new Performance(123, performanceStartTime, performanceEndTime, performerNames, 
            "15 Test Street", 300, false, false, 50, 5.75, event);

        //Initialize booking
        LocalDateTime bookingTime = LocalDateTime.of(2026, 4, 1, 16, 30);
        booking = new Booking(0, 2, 11.5, bookingTime, student, performance);
    }

    //cancelByStudent tests
    @Test
    @DisplayName("Tests to see if cancellation by student correctly alters a booking's status")
    void testCancelByStudent(){
        booking.cancelByStudent();
        assertEquals(booking.getStatus(), BookingStatus.CANCELLEDBYSTUDENT, "Expected true: Booking should be cancelled by student");
    }
    
    //cancelPaymentFailed tests
    @Test
    @DisplayName("Tests to see if failed payment correctly invalidates a booking")
    void testCancelPaymentFailed(){
        booking.cancelPaymentFailed();
        assertEquals(booking.getStatus(), BookingStatus.PAYMENTFAILED, "Expected true: Booking should be inactive because of invalid payment");
    }

    //cancelByProvider tests
    @Test
    @DisplayName("Tests to see if cancellation by an entertainment provider correctly alters a booking's status")
    void testCancelByProvider(){
        booking.cancelByProvider();
        assertEquals(booking.getStatus(), BookingStatus.CANCELLEDBYPROVIDER, "Expecteed true: Booking should be cancelled by provider");
    }

    //checkBookedByStudent tests
    @Test
    @DisplayName("Testing correct value for checkBookedByStudent when emails match")
    void testCheckBookedByStudentMatching(){
        String testEmail = booking.getStudent().getEmail();
        boolean matching = booking.checkBookedByStudent(testEmail);
        assertTrue(matching, "Expected true: Students emails should be matching");
    }

    @Test
    @DisplayName("Testing correct value for checkBookedByStudent when emails do not match")
    void testCheckBookedByStudentNotMatching(){
        String testEmail = "wrongemail@test.com";
        boolean matching = booking.checkBookedByStudent(testEmail);
        assertFalse(matching, "Expected false: Students emails should not match");
    }

    @Test
    @DisplayName("Testing correct value for checkBookedByStudent when email provided is null")
    void testCheckBookedByStudentNull(){
        String testEmail = null;
        boolean matching = booking.checkBookedByStudent(testEmail);
        assertFalse(matching, "Expected false: Student's email should not match null");
    }

    //getStudentDetails tests
    @Test
    @DisplayName("Testing correct email value for getStudentDetails")
    void testStudentDetailsContainsEmail(){
        String studentEmail = booking.getStudent().getEmail();
        String result = booking.getStudentDetails();
        assertTrue(result.contains(studentEmail), "Expected true: the student's details should contain their email");
    }

    @Test
    @DisplayName("Testing correct phone number value for getStudentDetails")
    void testStudentDetailsContainsPhoneNumber(){
        //Sets the student's phone number as this would have been taken in when creating the booking
        booking.getStudent().setPhoneNumber(12345);
        String result = booking.getStudentDetails();
        assertTrue(result.contains("12345"), "Expected true: the student's details should contain their phone number");
    }

    //generateBookingRecord tests
    @Test
    @DisplayName("Testing correct ID value for toString")
    void testCorrectID(){
        String result = performance.toString();
        assertTrue(result.contains("123"), "should contain performance ID");
    }

    @Test
    @DisplayName("Testing correct student name value for generateBookingRecord")
    void testCorrectStudentName(){
        //Sets the student's phone number as this would have been taken in when creating the booking
        student.setName("Bob");
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("Bob"), "Expected true: booking record should contain student's name");
    }

    @Test
    @DisplayName("Testing correct student email for generateBookingRecord")
    void testCorrectStudentEmail(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains(student.getEmail()), "Expected true: booking record should contain student's email");
    }

    @Test
    @DisplayName("Testing correct student phone number for generateBookingRecord")
    void testCorrectStudentPhoneNumber(){
        //Sets the student's phone number as this would have been taken in when creating the booking
        student.setPhoneNumber(12345);
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("12345"), "Expected true: booking record should contain student's phone number");
    }

    @Test
    @DisplayName("Testing correct event name for generateBookingRecord")
    void testCorrectEventName(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains(event.getTitle()), "Expected true: booking record should contain event name");
    }

    @Test
    @DisplayName("Testing if event is correctly ticketed in generateBookingRecord")
    void testEventTicketedWhenTicketed(){
        String result = booking.generateBookingRecord();
        //Since there should only be bookings for ticketed events, it should always contain true
        assertTrue(result.contains("true"), "Expected true: booking record should contain that event is ticketed");
    }

    @Test
    @DisplayName("Testing correct organizer information for generateBookingRecord")
    void testOrganizerEmail(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains(event.getOrganizerEmail()), "Expected true: booking record should contain organizer's email");
    }

    @Test
    @DisplayName("Testing correct venue address for generateBookingRecord")
    void testCorrectVenueAddress(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains(performance.getVenueAddress()), "Expected true: booking record should contain venue address");
    }

    @Test
    @DisplayName("Testing correct performer names for generateBookingRecord")
    void testCorrectPerformerNames(){
        String result = booking.generateBookingRecord();
        String performerNames = String.join(", ", performance.getPerformerNames());
        assertTrue(result.contains(performerNames), "Expected true: booking record should contain performer names");
    }
    
    @Test
    @DisplayName("Testing correct ticket price for generateBookingRecord")
    void testCorrectTicketPrice(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains(String.valueOf(performance.getTicketPrice())), "Expected true: booking record should contain ticket price");
    }

    @Test
    @DisplayName("Testing correct sponsored status for generateBookingRecord when performance is not sponsored")
    void testCorrectSponsoredStatusWhenNotSponsored(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("false"), "Expected true: booking record should contain that performance is not sponsored");
    }

    @Test
    @DisplayName("Testing correct sponsored status for generateBookingRecord when performance is sponsored")
    void testCorrectSponsoredStatusWhenSponsored(){
        //Sets the performance as sponsored as this would be initialized to false
        performance.sponsor(1);
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("true"), "Expected true: booking record should contain that performance is sponsored");
    }

    @Test
    @DisplayName("Testing correct sponsorship amount for generateBookingRecord when performance is not sponsored")
    void testCorrectSponsorshipAmountWhenNotSponsored(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("0.0"), "Expected true: booking record should contain that sponsorship amount is 0.0");
    }

    @Test
    @DisplayName("Testing correct sponsorship amount for generateBookingRecord when performance is sponsored")
    void testCorrectSponsorshipAmountWhenSponsored(){
        //Sets the performance as sponsored as this would be initialized to false
        performance.sponsor(1);
        String sponsorshipAmt = String.valueOf(performance.getSponsoredAmount());
        String result = booking.generateBookingRecord();
        assertTrue(result.contains(sponsorshipAmt), "Expected true: booking record should contain the correct sponsorship amount");
    }

    @Test
    @DisplayName("Testing correct performance status for generateBookingRecord when performance is active")
    void testCorrectPerformanceStatusWhenActive(){
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("ACTIVE"), "Expected true: booking record should contain that performance is not cancelled");
    }

    @Test
    @DisplayName("Testing correct performance status for generateBookingRecord when performance is cancelled")
    void testCorrectPerformanceStatusWhenCancelled(){
        //Cancels the performance as this would be initialized to active
        performance.cancelPerformance();
        String result = booking.generateBookingRecord();
        assertTrue(result.contains("CANCELLED"), "Expected true: booking record should contain that performance is cancelled");
    }
}