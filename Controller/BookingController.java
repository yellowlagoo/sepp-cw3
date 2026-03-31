package Controller;

import Model.Booking;
import Model.Performance;
import Model.User;
import java.util.Collection;
import View.View;

public class BookingController extends Controller {

    private long nextBookingNumber;
    private View view;
    private Collection<Performance> performances;

    public BookingController(User currentUser, long nextBookingNumber, Collection<Performance> performances) {
        super(currentUser);
        this.nextBookingNumber = nextBookingNumber;
        this.performances = performances;

    }

    // Use cases for task 1
    public void bookPerformance() {

        String performanceIDInput = view.getInput("Enter the ID of the performance you want to book:");
        long performanceID = Long.parseLong(performanceIDInput);

        String numTicketsRequested = view.getInput("Enter the number of tickets you want to book:");
        int numTickets = Integer.parseInt(numTicketsRequested);

        Performance performance = getPerformanceByID(performanceID);

        if (performance == null) {
            view.displayError("Performance with given number does not exist.");
            return;
        }

        else {

            boolean possible = checkIfBookingPossible(performance, numTickets);

            if (possible == true) {

                User curentUser = getCurrentUser();
                


            }




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

    }

    private Performance getPerformanceByID(long performanceID) {
        for (Performance p: performances) {
            if (p.getPerformanceID() == performanceID) {
                return p;
            }
        }        return null;
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
