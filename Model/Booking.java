import java.time.LocalDateTime;

public class Booking {
    private long bookingNumber;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;

    /**
     * 
     * @param bookingNumber
     * @param numTickets
     * @param amountPaid
     * @param bookingDateTime
     * @param status
     */
    public Booking(long bookingNumber, int numTickets, double amountPaid, LocalDateTime bookingDateTime,
            BookingStatus status) {
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid; 
        this.bookingDateTime = bookingDateTime;
        this.status = status;
    }
    /**
     * 
     * @return
     */
    public long getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(long bookingNumber) {
        this.bookingNumber = bookingNumber;
    }
    /**
     * 
     * @return numTickets the number of tickets in the booking 
     */
    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }
    /**
     * 
     * @return amountPaid total paid for the booking 
     */
    public double getAmountPaid() {
        return amountPaid;
    }
    /**
     * 
     * @return
     */
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
}