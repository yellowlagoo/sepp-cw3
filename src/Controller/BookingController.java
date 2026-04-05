package src.Controller;

import java.util.*;

import src.Model.*;
import src.ExternalSystems.PaymentSystem;
import java.time.LocalDateTime;
import src.View.*;

public class BookingController extends Controller {

    private long nextBookingNumber;
    private Collection<Performance> performances;
    private PaymentSystem paymentSystem;
    private Collection<Booking> bookings;

    /**
     * Constructor for the BookingController class
     * @param currentUser - the user currently interacting with the system
     * @param nextBookingNumber - the next booking number to be assigned to a booking, which is incremented each time a booking is made
     * @param performances - the collection of all performances in the system
     * @param paymentSystem - the payment system used to process payments for bookings
     * @param view - the view used to interact with the user
     */
    public BookingController(User currentUser, long nextBookingNumber, Collection<Performance> performances, PaymentSystem paymentSystem, TextUserInterface view) {
        super(currentUser, view);
        this.nextBookingNumber = nextBookingNumber;
        this.performances = performances;
        this.paymentSystem = paymentSystem; 
        this.bookings = new ArrayList<Booking>();
    }

    /**
     * Books a performance for the current user, providing a booking record upon success
     */
    public void bookPerformance() {
        Performance performance = null;
        Integer numTickets = null;
        boolean possible = false;
        boolean isTicketed = false;

        //Ensures current user is a student, ending booking process if not
        if(!checkCurrentUserIsStudent()) {
            view.displayError("Only students may book a performance");
            return;
        }

        //Getting user input
        while ((performance == null) || (possible == false && isTicketed == true)){
            String performanceIDInput = view.getInput("Enter the ID of the performance you want to book:");
            long performanceID = Long.parseLong(performanceIDInput);

            //Gives error if invalid performance ID given
            performance = getPerformanceByID(performanceID);
            if (performance == null) {
                view.displayError("Performance with given number does not exist.");
                continue;
            }

            String numTicketsRequested = view.getInput("Enter the number of tickets you want to book:");
            numTickets = Integer.parseInt(numTicketsRequested);

            isTicketed = performance.checkIfEventIsTicketed();
            possible = checkIfBookingPossible(performance, numTickets);
        }
        
        //Create booking
        Student s = (Student) getCurrentUser();
        Booking b = new Booking(nextBookingNumber, numTickets, performance.getFinalTicketPrice() * numTickets, 
                                LocalDateTime.now(), s, performance);
        addBooking(b);
        performance.addBooking(b);

        //Increment next booking number so each booking has a unique ID
        nextBookingNumber++;

        //Process the student's payment, providing an error message if unsuccessful
        String eventTitle = performance.getEventTitle();
        String studentEmail = s.getEmail();
        Integer studentPhone = s.getPhoneNumber();
        String epEmail = performance.getOrganizerEmail();
        double transactionAmount = performance.getFinalTicketPrice() * numTickets;
        boolean paymentSuccessful = paymentSystem.processPayment(numTickets, eventTitle, studentEmail, 
                                                                studentPhone, epEmail, transactionAmount);
        if (paymentSuccessful == false) {
            view.displayError("There was an issue with payment.");
            b.cancelPaymentFailed();
        }
        else {
            int numTicketsSold = performance.getNumTicketsSold();
            performance.setNumTicketsSold(numTicketsSold + numTickets);
            view.displaySuccess("Booking successful");
            String bookingRecord = b.generateBookingRecord();
            view.displayBookingRecord(bookingRecord);
        } 
    }

    /**
     * Adds a student's review and rating to a performance that they have booked in the past
     */
    public void reviewPerformance() {
        if(!checkCurrentUserIsStudent()){
            view.displayError("Only students may review a performance");
            return;
        }
        else{
            String performanceIDinput = view.getInput("Enter the ID of the performance you would like to review: ");
            long performanceID = Long.parseLong(performanceIDinput);
            Performance performanceToReview = getPerformanceByID(performanceID);

            //Ensure performance with ID exists, reprompting user if not
            while(performanceToReview == null){
                view.displayError("A performance with the given ID does not exist. Please enter a new ID.");
                performanceIDinput = view.getInput("Enter the ID of the performance you would like to review: ");
                performanceID = Long.parseLong(performanceIDinput);
                performanceToReview = getPerformanceByID(performanceID);
            }

            //Ensure performance has a booking corresponding to the student, reprompting user if not
            String currentEmail = getCurrentUser().getEmail();
            boolean belongsToStudent = false;
            for(Booking b : performanceToReview.getBookings()){
                if(b.checkBookedByStudent(currentEmail))
                    belongsToStudent = true;
            }
            while(!belongsToStudent){
                view.displayError("You have not created a booking for this performance. Please enter a new performance ID.");
                performanceIDinput = view.getInput("Enter the ID of the performance you want to cancel: ");
                performanceID = Long.parseLong(performanceIDinput);
                performanceToReview = getPerformanceByID(performanceID);

                for(Booking b : performanceToReview.getBookings()){
                    if(b.checkBookedByStudent(currentEmail))
                        belongsToStudent = true;
                }
            }

            //Ask student for rating, providing an error upon invalid rating
            boolean valid = false;
            int rating = -1;
            while (!valid) {
                String ratingInput = view.getInput("Please enter a rating 1-5 for the performance: ");

                try {
                    rating = Integer.parseInt(ratingInput);

                    if (rating < 1 || rating > 5)
                        view.displayError("You must enter an integer rating between 1 and 5 for your review. Please try again.");
                    else
                        valid = true;
                } 
                catch (NumberFormatException e) {
                    view.displayError("You must enter an integer rating between 1 and 5 for your review. Please try again.");
                }
            }

            String comment = view.getInput("You may enter a comment for the review. Please press enter if you do not want to " +
                                            "add a comment, otherwise type your comment here: ");
            performanceToReview.review(rating, comment);
            view.displaySuccess("You have successfully reviewed the performance. Thank you!");
        }
    }

    /**
     * Cancels the booking of the current booking number when the student that booked it chooses to cancel
     */
    public void cancelBooking() {
        if(!checkCurrentUserIsStudent()) {
            view.displayError("Only students may cancel a booking");
            return;
        }
        else{
            String bookingIDinput = view.getInput("Enter the ID of the booking you want to cancel: ");
            long bookingID = Long.parseLong(bookingIDinput);
            Booking bookingToCancel = getBookingByNumber(bookingID);
            
            //Ensure booking with given number exists, reprompting user if not
            while(bookingToCancel == null){
                view.displayError("Booking with given number does not exist. Please enter a valid booking number.");
                bookingIDinput = view.getInput("Enter the ID of the booking you want to cancel: ");
                bookingID = Long.parseLong(bookingIDinput);
                bookingToCancel = getBookingByNumber(bookingID);
            }

            //Ensure booking belongs to user, reprompting if not
            String currentEmail = getCurrentUser().getEmail();
            boolean belongsToStudent = bookingToCancel.checkBookedByStudent(currentEmail);
            while(!belongsToStudent){
                view.displayError("This booking does not belong to you. Please enter a new booking number.");
                bookingIDinput = view.getInput("Enter the ID of the booking you want to cancel: ");
                bookingID = Long.parseLong(bookingIDinput);
                bookingToCancel = getBookingByNumber(bookingID);

                belongsToStudent = bookingToCancel.checkBookedByStudent(currentEmail);
            }

            //Ensure booking is at least 24 hours away, ending cancellation if not
            LocalDateTime oneDayLater = LocalDateTime.now().plusHours(24);
            LocalDateTime startTime = bookingToCancel.getPerformance().getStartDateTime();
            if (startTime.isBefore(oneDayLater)) {
                view.displayError("Bookings can only be cancelled if they are at least 24 hours away.");
                return;
            }

            //Refund payment, notifying student if there is an issue with processing refund
            boolean refunded = paymentSystem.processRefund(bookingToCancel.getNumTickets(), bookingToCancel.getPerformance().getEventTitle(), 
                                                        bookingToCancel.getStudent().getEmail(), bookingToCancel.getStudent().getPhoneNumber(), 
                                                        bookingToCancel.getPerformance().getOrganizerEmail(), bookingToCancel.getAmountPaid(), "");
            if(refunded){
                view.displaySuccess("The booking for the booking ID " + bookingID + " was successfully cancelled and refunded");
                bookingToCancel.cancelByStudent();
            }
            else{
                view.displayError("The booking was not successfully cancelled or refunded. Please try again later.");
            }
        }
    }

    /**
     * Adds a booking to the system
     * @param b - the booking to be added
     */
    private void addBooking(Booking b) {
        bookings.add(b);
    }

    /**
     * Returns a performance given its ID
     * @param performanceID - the performance ID to search for
     * @return the performance associated with that ID or null for an invalid ID
     */
    private Performance getPerformanceByID(long performanceID) {
        for (Performance p: performances) {
            if (p.getPerformanceID() == performanceID) {
                return p;
            }
        }        
        return null;
    }

    /**
     * Returns whether a booking is possible based on if there are enough tickets available
     * Provides an error message if a user tries to book is nonticketed
     * @param performance - the performance trying to be booked
     * @param numTickets - the number of tickets trying to be booked
     * @return - whether or not the booking is possible 
     */
    private boolean checkIfBookingPossible(Performance performance, int numTickets) {
        boolean isTicketed = performance.checkIfEventIsTicketed();
        if (!isTicketed) {
            view.displayError("The requested performance's event is not ticketed. There is no need to book it.");
            return false;
        }
        else {
            boolean enoughTicketsLeft = performance.checkIfTicketsLeft(numTickets);
            if (!enoughTicketsLeft) {
                view.displayError("Requested performance has no tickets left.");
                return false;
            }
            else {
                return true;
            }
        }
    }

    /**
     * Provides a list of all bookings for a particular event, given its ID
     * @param eventID - the event ID to search with
     * @return - all of the bookings associated with a particular event ID
     */
    private Collection<Booking> findBookingsByEvent(long eventID) {
        Collection<Booking> bookingsByEvent = new ArrayList<>();
        for(Performance p : performances){
            if(p.getEvent().getEventID() == eventID){
                bookings.addAll(p.getBookings());
            }
        }
        return bookingsByEvent;
    }

    /**
     * Iterates through all of the performances in the system to find the booking with a particular booking number
     * @param bookingNumber - the booking number to search for
     * @return - the booking corresponding with that number or null for an invalid number
     */
    private Booking getBookingByNumber(long bookingNumber) {
        for(Performance p : performances){
            for(Booking b : p.getBookings()){
                if(b.getBookingNumber() == bookingNumber)
                    return b;
            }
        }
        return null;
    }
}