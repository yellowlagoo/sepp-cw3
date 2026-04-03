package controller;

import java.util.ArrayList;

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
    private Collection<Booking> bookings;

    public BookingController(User currentUser, long nextBookingNumber, Collection<Performance> performances, PaymentSystem paymentSystem) {
        super(currentUser);
        this.nextBookingNumber = nextBookingNumber;
        this.performances = performances;
        this.paymentSystem = paymentSystem; 
        this.bookings = new ArrayList<Booking>();
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
        String epEmail = performance.getOrganiserEmail();
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

    public void cancelBooking() {
        // This is one of our assigned use cases for task 1 (Amelia's)
    }

    // add to as we complete our use cases
    private void addBooking(Booking b) {
        bookings.add(b);

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
        return null;
    }

}

