package controller;

import model.Booking;
import model.BookingStatus;
import model.Performance;
import model.User;
import model.Student;
import externalsystems.PaymentSystem;
import java.time.LocalDateTime;
import java.util.Collection;
import view.View;

public class BookingController extends Controller {

    private long nextBookingNumber;
    private View view;
    private Collection<Performance> performances;
    private PaymentSystem paymentSystem;

    public BookingController(User currentUser, long nextBookingNumber, Collection<Performance> performances, PaymentSystem paymentSystem) {
        super(currentUser);
        this.nextBookingNumber = nextBookingNumber;
        this.performances = performances;
        this.paymentSystem = paymentSystem; 
    }


    // BookPerformance() use case (Michael)
    public void bookPerformance() {

         Performance performance = null;
         Integer numTickets = null;
         boolean possible = false;
         boolean isTicketed = false;

        while ((performance == null) || (possible == false && isTicketed == true)){

        String performanceIDInput = view.getInput("Enter the ID of the performance you want to book:");
        long performanceID = Long.parseLong(performanceIDInput);

        String numTicketsRequested = view.getInput("Enter the number of tickets you want to book:");
        numTickets = Integer.parseInt(numTicketsRequested);

        performance = getPerformanceByID(performanceID);

        if (performance == null) {
            view.displayError("Performance with given number does not exist.");
            continue;
        }

        isTicketed = performance.checkIfEventIsTicketed();
        possible = checkIfBookingPossible(performance, numTickets);

        }
        
        Student s = (Student) getCurrentUser();

        Booking b = new Booking(nextBookingNumber, numTickets, performance.getFinalTicketPrice() * numTickets, LocalDateTime.now(), BookingStatus.ACTIVE, s, performance);
        addBooking(b);
        performance.addBooking(b);

        nextBookingNumber++;

        String eventTitle = performance.getEventTitle();
        String studentEmail = s.getEmail();
        Integer studentPhone = s.getPhoneNumber();
        String epEmail = performance.getOrganizerEmail();
        double transactionAmount = performance.getFinalTicketPrice() * numTickets;

         boolean paymentSuccessful = paymentSystem.processPayment(numTickets, eventTitle, studentEmail, studentPhone, epEmail, transactionAmount); // This error is due to the processPayment not being defined as of yet in the PaymentSystem etc

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

    public void reviewPerformance() {
        // This is one of our assigned use cases for task 1 (Toni's)
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
            
            //Ensure booking with given number exists and belongs to user, reprompting user if not
            String currentEmail = getCurrentUser().getEmail();
            boolean belongsToStudent = false; 
            if(bookingToCancel != null)
                belongsToStudent = bookingToCancel.checkBookedByStudent(currentEmail);
            while(bookingToCancel == null || !belongsToStudent){
                view.displayError("Booking with given number does not exist. Please enter a valid booking number: ");
                bookingIDinput = view.getInput("Enter the ID of the booking you want to cancel: ");
                bookingID = Long.parseLong(bookingIDinput);
                bookingToCancel = getBookingByNumber(bookingID);
            }

        }
    }

    // add to as we complete our use cases
    private void addBooking(Booking b) {

    }

    private Performance getPerformanceByID(long performanceID) {
        for (Performance p: performances) {
            if (p.getPerformanceID() == performanceID) {
                return p;
            }
        }        
        return null;
    }

    private boolean checkIfBookingPossible(Performance performance, int numTickets) {
        
        boolean isTicketed = performance.checkIfEventIsTicketed();

        if (isTicketed == false) {
            view.displayError("The requested performance's event is not ticketed. There is no need to book it.");
            return false;
        }
        
        else {
            boolean ticketsLeft = performance.checkIfTicketsLeft(numTickets);

            if (ticketsLeft == false) {
                view.displayError("Requested performance has no tickets left.");
                return false;
            }

            else {
                return true;
            }
        }
    }

    private Collection<Booking> findBookingsByEvent(long eventID) {
        return null;
    }

    private Booking getBookingByNumber(long bookingNumber) {
        //iterate thru performances
        return null;
    }

}

