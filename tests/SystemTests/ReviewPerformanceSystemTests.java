package tests.SystemTests;

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

public class ReviewPerformanceSystemTests {

        private TextUserInterface view;
        private BookingController bookingController;
        private Collection<Performance> allPerformances;
        private MockPaymentSystem paymentSystem;
        private Performance performanceOne;
        private Performance performanceTwo;
        private EntertainmentProvider ep;
        private Event event;
        private Student student;

        @BeforeEach
        void setup() {
                view = mock(TextUserInterface.class);
                paymentSystem = new MockPaymentSystem();
                allPerformances = new ArrayList<>();
                bookingController = new BookingController(paymentSystem, view, allPerformances);

                ep = new EntertainmentProvider("ep@test.com", "password123", "Organisation Name", "A123684956",
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

                allPerformances.add(performanceOne);
                allPerformances.add(performanceTwo);

                student = new Student("student@hidenburgh.ed.ac.uk", "secretpassword123");
                student.setName("Michael");
                student.setPhoneNumber(131443131);
                student.setLoggedIn(true);

                Booking booking = new Booking(1, 1, 50.0, LocalDateTime.now(), student, performanceOne);
                performanceOne.addBooking(booking);

                bookingController.setCurrentUser(student);

        }

        // tsting for successful review
        @Test
        @DisplayName("checking values for when a review is sucessfully posted")
        void testReview() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("test comment");

                bookingController.reviewPerformance();

                verify(view).displaySuccess("You have successfully reviewed the performance. Thank you!");
        }

        // testing for an invalid performance ID
        @Test
        @DisplayName("checking values for when invalid performance ID is provided")
        void testInvalidIdReviewRequest() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("91231")
                                .thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("test comment");

                bookingController.reviewPerformance();

                verify(view).displayError("A performance with the given ID does not exist. Please enter a new ID.");
        }

        // testing for student requesting to leave a review on a performance they have not booked
        @Test
        @DisplayName("checking value when student attempts to review a performance they have not booked")
        void testReviewPerformanceNotBookedByStudent() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("2");
                when(view.getInput("Enter a performance ID or 'n' to stop: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("test comment");

                bookingController.reviewPerformance();

                verify(view).displayError("You have not created a booking for this performance.");
        }

        // test when Student rates performance too low
        @Test
        @DisplayName("checking values for when a review is sucessfully left")
        void testReviewTooLowRating() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("-100")
                                .thenReturn("2");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("test comment");

                bookingController.reviewPerformance();

                verify(view)
                                .displayError("You must enter an integer rating between 1 and 5 for your review. Please try again.");
        }

        // test when student rates performane too high
        @Test
        @DisplayName("checking values for when a review is sucessfully left")
        void testReviewTooHighRating() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("1000")
                                .thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("test comment");

                bookingController.reviewPerformance();

                verify(view)
                                .displayError("You must enter an integer rating between 1 and 5 for your review. Please try again.");
        }

        // testing valid rating but with no comment works
        @Test
        @DisplayName("checking values for when a review is sucessfully left")
        void testReviewWithNoComment() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn(null);

                bookingController.reviewPerformance();

                verify(view).displaySuccess("You have successfully reviewed the performance. Thank you!");
        }

        // testing review Comment is being added to the performance
        @Test
        @DisplayName("checking values for when a review is sucessfully left")
        void testReviewCommentPresent() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("Performance was great");

                bookingController.reviewPerformance();

                assertTrue(performanceOne.getReviewComments().contains("Performance was great"),
                                "should contain the added review comment");
        }

        // testing review rating is being added to the performance
        @Test
        @DisplayName("checking values for when a review is sucessfully left")
        void testReviewRatingPresent() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("Performance was great");

                bookingController.reviewPerformance();

                assertTrue(performanceOne.getReviewRating().contains(5),
                                "should contain the added review comment");
        }

        // testing non students cannot review a performance
        @Test
        @DisplayName("checking correct value for when a non student attempts to review a performance")
        void testOnlyStudentsCanReview() {

                bookingController.setCurrentUser(ep);

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");

                bookingController.reviewPerformance();

                verify(view).displayError("Only students may review a performance");

        }

        //Testing when student provides non-integer rating
        @Test
        @DisplayName("checking values for when a student does not leave an integer rating")
        void testReviewWithNonIntegerRating() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("this is not an integer rating");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("this is a comment");
                when(view.getInput("Would you like to retry? (y/n not case sensitive)")).thenReturn("n");

                bookingController.reviewPerformance();

                verify(view).displayError("You must enter an integer rating between 1 and 5 for your review.");
        }

        // test that review ratings count is increased after a successful rating
        @Test
        @DisplayName("checking rating count increases after successful rating")
        void testReviewRatingCount() {

                when(view.getInput("Enter the ID of the performance you would like to review: ")).thenReturn("1");
                when(view.getInput("Please enter a rating 1-5 for the performance: ")).thenReturn("5");
                when(view.getInput(
                                "You may enter a comment for the review. Please press enter if you do not want to add a comment, otherwise type your comment here: "))
                                .thenReturn("test comment");

                bookingController.reviewPerformance();
                bookingController.reviewPerformance();
                bookingController.reviewPerformance();
                bookingController.reviewPerformance();

                assertEquals(4, performanceOne.getReviewRating().size(), "should contain 4 ratings ");
        }


        
}
