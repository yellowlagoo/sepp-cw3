package tests.ModelTests;

import src.Model.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

public class TestPerformance {

    private EntertainmentProvider ep;
    private Performance performance;
    private Event ticketedEvent;

    @BeforeAll
    static void initAll() {
        System.out.println("Testing for Performance class started");
        System.out.println("--------------------------------");
    }

    @BeforeEach
    void setup() {
        ep = new EntertainmentProvider("ep@test.com", "password", "Test Entertainment Provider", "A123", "Test",
                "description");
        ticketedEvent = new Event(ep, 1234, "Test Title", EventType.MUSIC, true);
        performance = new Performance(123, LocalDateTime.of(2030, 4, 20, 16, 30), LocalDateTime.of(2030, 4, 20, 20, 30),
                Arrays.asList("performer Names"), "12 adress", 100, false, false, 100, 50.00, ticketedEvent);
    }

    @AfterEach
    void betweenTests() {
        System.out.println("--------------------------------");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Testing for Performance class completed");
    }

    // test for status when a performance is cancelled
    @Test
    @DisplayName("Testing correct value for Cancel when performance is cancelled")
    void testCancelPerformanceNotCancelled() {
        performance.cancel();
        assertTrue(PerformanceStatus.CANCELLED == performance.getStatus(), "Performance should be cancelled");
    }

    // test for cancelling an already cancelled performance
    @Test
    @DisplayName("Testing correct value for Cancel when performance is cancelled twice")
    void testCancelPerformanceAlreadyCancelled() {
        performance.cancel();
        performance.cancel();
        assertTrue(PerformanceStatus.CANCELLED == performance.getStatus(), "Performance should still be cancelled");
    }

    // test for checking correct value stored when an event is ticketed
    @Test
    @DisplayName("Testing correct value for checkIfEventIsTicketed when event is ticketed")
    void testCheckIfEventIsTicketedTrue() {
        assertTrue(performance.checkIfEventIsTicketed(), "Event should be ticketed");
    }

    // test for checking correct value when event is not ticketed
    @Test
    @DisplayName("Testing correct value for checkIfEventIsTicketed when event is not ticketed")
    void testCheckIfEventIsTicketedFalse() {
        Event nonTicketedEvent = new Event(ep, 5678, "Non-Ticketed Event", EventType.MUSIC, false);
        Performance nonTicketedPerformance = new Performance(456, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), Arrays.asList("performer Names"), "12 address", 100, false, false, 100,
                50.00, nonTicketedEvent);
        assertFalse(nonTicketedPerformance.checkIfEventIsTicketed(), "Event should not be ticketed");
    }

    // testing for correct number of tickets available
    @Test
    @DisplayName("Testing correct value for checkIfTicketsLeft when tickets are available")
    void testCheckIfTicketsLeftTrue() {
        assertTrue(performance.checkIfTicketsLeft(50), "There should be tickets left");
    }

    // testing forcorrect number of tickets when not enough available
    @Test
    @DisplayName("Testing correct value for checkIfTicketsLeft when no tickets are available")
    void testCheckIfTicketsLeftFalse() {
        assertFalse(performance.checkIfTicketsLeft(150), "There should be no tickets left");
    }

    // testing correct value is stored for EP's email
    @Test
    @DisplayName("Testing correct value for getOrganiserEmail")
    void testGetOrganiserEmailIsEqual() {
        assertEquals(performance.getOrganizerEmail(), "ep@test.com");
    }

    // testing comparing two ep emails
    @Test
    @DisplayName("Testing correct value for getOrganiserEmail when emails do not match")
    void testGetOrganiserEmailIsNotEqual() {
        assertNotEquals(performance.getOrganizerEmail(), "ep2@test.com");
    }

    // testing correct event title
    @Test
    @DisplayName("Testing correct value for getEventTitle")
    void testGetEventTitleIsEqual() {
        assertEquals(performance.getEventTitle(), "Test Title");
    }

    // testing for incorrect event title
    @Test
    @DisplayName("Testing correct value for getEventTitle")
    void testGetEventTitleIsNotEqual() {
        assertNotEquals(performance.getEventTitle(), "Different Title");
    }

    // testing when performance occurs in the future
    @Test
    @DisplayName("Testing correct value for checkHasNotHappenedYet when performance is in the future")
    void testCheckHasNotHappenedYetTrue() {
        assertTrue(performance.checkHasNotHappenedYet(), "Performance should not have happened yet");
    }

    // testing when perforamnce has already occured
    @Test
    @DisplayName("Testing correct value for checkHasNotHappenedYet when performance is in the past")
    void testCheckHasNotHappenedYetFalse() {
        Performance pastPerformance = new Performance(789, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), Arrays.asList("performer Names"), "12 address", 100, false, false,
                100, 50.00, ticketedEvent);
        assertFalse(pastPerformance.checkHasNotHappenedYet(), "Performance should have happened already");
    }

    // testing when a perfromance is created the correct EP is associated with it
    @Test
    @DisplayName("Testing correct value for checkCreatedByEP when performance is created by the given EP")
    void testCheckCreatedByEPTrue() {
        assertTrue(performance.checkCreatedByEP("ep@test.com"), "Performance should be created by the given EP");
    }

    // testing when a performance is created non-associated EP's are recognised
    @Test
    @DisplayName("Testing correct value for checkCreatedByEP when performance is not created by the given EP")
    void testCheckCreatedByEPFalse() {
        assertFalse(performance.checkCreatedByEP("ep123@test.com"),
                "Performance should not be created by the given EP");
    }

    // testing when bookings are active
    @Test
    @DisplayName("Testing correct value for hasActiveBookings when performance has active bookings")
    void testHasActiveBookingsTrue() {
        Student s = new Student("Name", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        performance.addBooking(b);
        assertTrue(performance.hasActiveBookings(), "Performance should have active bookings");
    }

    // testing bookings when cancelled by a student
    @Test
    @DisplayName("Testing correct value for hasActiveBookings when performance cancelled by student")
    void testHasActiveBookingsFalseCancelledByStudent() {
        Student s = new Student("Name", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        b.cancelByStudent();
        performance.addBooking(b);
        assertFalse(performance.hasActiveBookings(), "Performance should have no active bookings");
    }

    // testing bookings when cancelled by an EP
    @Test
    @DisplayName("Testing correct value for hasActiveBookings when performance cancelled by EP")
    void testHasActiveBookingsFalseCancelledByEP() {
        Student s = new Student("Name", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        b.cancelByProvider();
        performance.addBooking(b);
        assertFalse(performance.hasActiveBookings(), "Performance should have no active bookings");
    }

    // testing bookings when payment fails
    @Test
    @DisplayName("Testing correct value for hasActiveBookings when performance payment failed")
    void testHasActiveBookingsFalsePaymentFailed() {
        Student s = new Student("Name", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        b.cancelPaymentFailed();
        performance.addBooking(b);
        assertFalse(performance.hasActiveBookings(), "Performance should have no active bookings");
    }

    // testing for correct booking details
    @Test
    @DisplayName("Testing correct email value for getBookingDetailsForRefund when performance has active bookings")
    void testGetBookingDetailsForRefundContainsEmail() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        performance.addBooking(b);
        String details = performance.getBookingDetailsForRefund();
        assertTrue(details.contains("student@email.com"),
                "should contain the email of the student that made the booking");
    }

    //testing bookings should contain the amount paid
    @Test
    @DisplayName("Testing correct value price value for getBookingDetailsForRefund when performance has active bookings")
    void testGetBookingDetailsForRefundContainsAmountPaid() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        performance.addBooking(b);
        String details = performance.getBookingDetailsForRefund();
        assertTrue(details.contains("50.0"), "should contain the amount paid for the booking");
    }

    // testing booking details contains the number of tickets sold
    @Test
    @DisplayName("Testing correct tickets sold value for getBookingDetailsForRefund when performance has active bookings")
    void testGetBookingDetailsForRefundContainsNumTickets() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        performance.addBooking(b);
        String details = performance.getBookingDetailsForRefund();
        assertTrue(details.contains("1"), "should contain the number of tickets for the booking");
    }


    // testing for the booking detials when no bookings are active when cancelled by EP
    @Test
    @DisplayName("Testing correct value for getBookingDetailsForRefund when performance has no active bookings when cancelled by EP")
    void testGetBookingDetailsForRefundContainsNullWhenCancelledByProvider() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        b.cancelByProvider();
        performance.addBooking(b);
        String details = performance.getBookingDetailsForRefund();
        assertEquals("", details, "should not contain any details when booking status is not active");
    }

    // testing for booking detials when no bookings are active when student cancels booking
    @Test
    @DisplayName("Testing correct value for getBookingDetailsForRefund when performance has no active bookings when cancelled by student")
    void testGetBookingDetailsForRefundContainsNullWhenCancelledByStudent() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        b.cancelByStudent();
        performance.addBooking(b);
        String details = performance.getBookingDetailsForRefund();
        assertEquals("", details, "should not contain any details when booking status is not active");
    }

    // testing for no active bookings on a performance when hte bookings are cancelled
    @Test
    @DisplayName("Testing correct value for getBookingDetailsForRefund when performance has no active bookings")
    void testGetBookingDetailsForRefundContainsNullWhenPaymentFailed() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        b.cancelPaymentFailed();
        performance.addBooking(b);
        String details = performance.getBookingDetailsForRefund();
        assertEquals("", details, "should not contain any details when booking status is not active");
    }

    // testing for sponsorship working
    @Test
    @DisplayName("Testing correct value for isSponsored when there are sponsors")
    void testSponsorSetsIsSponsored() {
        performance.sponsor(10.00);
        assertTrue(performance.isSponsored(), "Performance should be sponsored");
    }

    // testing for value of spnosorship
    @Test
    @DisplayName("Testing correct value for sponsorAmount when there are sponsors")
    void testSponsorAmount() {
        performance.sponsor(10.0);
        assertEquals(10.0, performance.getSponsoredAmount(), "Sponsored amount should be equal to 10");
    }

    // testing for correct final ticket price after sponsorship
    @Test
    @DisplayName("Testing correct value for final ticket price")
    void testFinalTicketPrice() {
        performance.sponsor(10.0);
        assertEquals(40.0, performance.getFinalTicketPrice(), "Ticket price should be reduced to 40.0");
    }

    // testing for correct rating value
    @Test
    @DisplayName("Testing correct value for rating")
    void testCorrectRating() {
        performance.review(3, "test comment");
        assertTrue(performance.getReviewRating().contains(3), "Rating should be a 3");
    }

    // testing for correct review value
    @Test
    @DisplayName("Testing correct value for review")
    void testCorrectReview() {
        performance.review(3, "test comment");
        assertTrue(performance.getReviewComments().contains("test comment"), "Review comment should be present");
    }

    // testing that bookings are added
    @Test
    @DisplayName("Testing correct value for adding the booking")
    void testBookingadded() {
        Student s = new Student("student@email.com", "Password");
        Booking b = new Booking(1, 1, 50.00, LocalDateTime.now(), s, performance);
        performance.addBooking(b);
        assertTrue(performance.getBookings().contains(b), "Booking should be added to the bookings");
    }

    // testing correct performance id value
    @Test
    @DisplayName("Testing correct ID value for toString")
    void testCorrectID() {
        String result = performance.toString();
        assertTrue(result.contains("123"), "should contain performance ID");
    }

    // testing for correct start date year
    @Test
    @DisplayName("Testing correct year value for toString")
    void testCorrectStartYear() {
        String result = performance.toString();
        assertTrue(result.contains("2030"), "should contain correct start year");
    }

    // testing for correct start date monht
    @Test
    @DisplayName("Testing correct Month value for toString")
    void testCorrectStartMonth() {
        String result = performance.toString();
        assertTrue(result.contains("4"), "should contain correct start month");
    }

    // testing for correct start date hour
    @Test
    @DisplayName("Testing correct hour value for toString")
    void testCorrectStartHour() {
        String result = performance.toString();
        assertTrue(result.contains("4"), "should contain correct start hour");
    }

    // testing for correct start dae time
    @Test
    @DisplayName("Testing correct minute value for toString")
    void testCorrectStartMinute() {
        String result = performance.toString();
        assertTrue(result.contains("30"), "should contain correct start minute");
    }

    // testing for correct end date year
    @Test
    @DisplayName("Testing correct year value for toString")
    void testCorrectEndYear() {
        String result = performance.toString();
        assertTrue(result.contains("2030"), "should contain correct end year");
    }

    // testing for correct end date month
    @Test
    @DisplayName("Testing correct month value for toString")
    void testCorrectEndMonth() {
        String result = performance.toString();
        assertTrue(result.contains("4"), "should contain correct end month");
    }


    // testing for correct end date hour
    @Test
    @DisplayName("Testing correct hour value for toString")
    void testCorrectEndHour() {
        String result = performance.toString();
        assertTrue(result.contains("20"), "should contain correct end hour");
    }

    // testing for correct end date minute
    @Test
    @DisplayName("Testing correct minute value for toString")
    void testCorrectEndMinute() {
        String result = performance.toString();
        assertTrue(result.contains("30"), "should contain correct end minute");
    }

    // test that performance names are included
    @Test
    @DisplayName("Testing correct performer names value for toString")
    void testCorrectPerformerNames() {
        String result = performance.toString();
        assertTrue(result.contains("performer Names"), "should contain correct performer names");
    }

    // test that venue address is included
    @Test
    @DisplayName("Testing correct venue address value for toString")
    void testCorrectAdress() {
        String result = performance.toString();
        assertTrue(result.contains("12 adress"), "should contain correct adress");
    }

    // test that correct capacity is included
    @Test
    @DisplayName("Testing correct capacity value for toString")
    void testCorrectCapacity() {
        String result = performance.toString();
        assertTrue(result.contains("100"), "should contain correct capacity");
    }

    //test that correct outdoors value is included
    @Test
    @DisplayName("Testing correct is outdoors value for toString")
    void testCorrectIsOutdoors() {
        String result = performance.toString();
        assertTrue(result.contains("false"), "should contain false for is outdoors");
    }

    // test that correct smoking rules are included
    @Test
    @DisplayName("Testing correct smoking rules value for toString")
    void testCorrectSmokingRules() {
        String result = performance.toString();
        assertTrue(result.contains("false"), "should contain correct smoking rules");
    }

    // test that correct total tickets is included
    @Test
    @DisplayName("Testing correct total tickets value for toString")
    void testCorrectTotalTickets() {
        String result = performance.toString();
        assertTrue(result.contains("100"), "should contain correct total tickets");
    }

    // test that ticket price is included
    @Test
    @DisplayName("Testing correct ticket price value for toString")
    void testCorrectTicketPrice() {
        String result = performance.toString();
        assertTrue(result.contains("50"), "should contain correct ticket price");
    }
}