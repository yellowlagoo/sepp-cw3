package Model; 

import java.time.LocalDateTime;

public class Booking {
    private long bookingNumber;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;
    private BookingController bookingController;
    private Student student;
    private Performance performance;
    /**
     * 
     * @param bookingNumber
     * @param numTickets
     * @param amountPaid
     * @param bookingDateTime
     * @param status 
     * @param student student that created the booking 
     * @param performance performance student is booking
     */
    public Booking(long bookingNumber, int numTickets, double amountPaid, LocalDateTime bookingDateTime,
            BookingStatus status, Student student, Performance performance) {
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid; 
        this.bookingDateTime = bookingDateTime;
        this.status = status;
        this.student = student;
        this.performance = performance;
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

    public void cancelByStudent() {

    }

    public void cancelPaymentFailed() {

    }

    public void cancelByProvider() {

    }
    
    public boolean checkBookedByStudent(String email) {
        return student.getEmail() == email;
    }

    public String getStudentDetails() {
        return student.getEmail();
    }

    public String generateBookingRecord() {
        String record = "STUDENT DETAILS\n";
        record += "Name: " + student.getName() + "\n";
        record += "Email: " + student.getEmail() + "\n";
        record += "Phone Number: " + student.getPhoneNumber() + "\n";

        record += "\nEVENT DETAILS\n";
        record += "Name: " + performance.getEventTitle() + "\n";
        record += "Ticketed: " + performance.checkIfEventsTicketed() + "\n";
        record += "Organiser: " + performance.getOrganiserEmail() + "\n";
        
        record += "\nPERFORMANCE DETAILS\n";
        record += "Venue: " + performance.getVenueAddress() + "\n";
        record += "Performers: " + performance.toString(performance.getPerformerNames()) + "\n";
        record += "Ticket price: " + performance.getTicketPrice() + "\n";
        record += "Sponsored: " + performance.isSponsored() + "\n";
        record += "Sponsor amount: " + performance.getSponsoredAmount() + "\n";
        record += "Status: " + performance.getStatus() + "\n";

        return record;
    }

}