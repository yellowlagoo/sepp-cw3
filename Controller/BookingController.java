package Controller;

import Model.Booking;
import Model.Performance;
import Model.User;
import java.util.Collection;

public class BookingController extends Controller {

    private long nextBookingNumber;

    public BookingController(User currentUser, long nextBookingNumber) {
        super(currentUser);
        this.nextBookingNumber = nextBookingNumber;

    }

    // Use cases for task 1
    public void bookPerformance() {
        // This is one of our assinged use cases for task 1 (Michael's)
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
        return null;
    }

    private boolean checkIfBookingPossible(Performance performance, int numTickets) {
        return false;
    }

    private Collection<Booking> findBookingsByEvent(long eventID) {
        return null;
    }

    private Booking getBookingByNumber(long bookingNumber) {
        return null;
    }

}
