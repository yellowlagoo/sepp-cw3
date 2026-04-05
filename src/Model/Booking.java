package src.Model; 
import java.time.LocalDateTime;

public class Booking {
    private long bookingNumber;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;
    private Student student;
    private Performance performance;
    
    /**
     * Constructor for the booking class
     * @param bookingNumber - the unique ID of the booking
     * @param numTickets - the number of tickets to be booked
     * @param amountPaid - the amount the student has paid
     * @param bookingDateTime - the time the booking was created
     * @param student - the student that created the booking 
     * @param performance - the performance student is booking
     */
    public Booking(long bookingNumber, int numTickets, double amountPaid, LocalDateTime bookingDateTime,
                Student student, Performance performance) {
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid; 
        this.bookingDateTime = bookingDateTime;
        status = BookingStatus.ACTIVE;
        this.student = student;
        this.performance = performance;
    }

    //Getters and setters
    public long getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(long bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /**
     * Updates the booking's status to reflect when a student has cancelled their booking
     */
    public void cancelByStudent() {
        this.status = BookingStatus.CANCELLEDBYSTUDENT;
    }

    /**
     * Updates the booking's status to reflect when the payment has failed
     */
    public void cancelPaymentFailed() {
        this.status = BookingStatus.PAYMENTFAILED;
    }

    /**
     * Updates the booking's status to reflect when an entertainment provider has chosen to cancel the performance
     */
    public void cancelByProvider() {
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }
    
    /**
     * Checks whether a particular student made the booking
     * To be used to make sure another student does not cancel someone else's booking
     * @param email - the email to check against
     * @return - whether or not the student with that email address made the booking
     */
    public boolean checkBookedByStudent(String email) {
        return student.getEmail().equals(email);
    }

    /**
     * Provides the email and phone number of the student who made the booking
     * @return the details of the student that made the booking
     */
    public String getStudentDetails() {
        String studentEmail = student.getEmail();
        long studentPhoneNumber = student.getPhoneNumber();
        return studentEmail + "\n" + studentPhoneNumber;
    }

    /**
     * Provides a record of the booking, including student details, event details, and performance details
     * @return information about the student who booked the performance as well as the performance itself
     */
    public String generateBookingRecord() {
        String record = "STUDENT DETAILS\n";
        record += "Name: " + student.getName() + "\n";
        record += "Email: " + student.getEmail() + "\n";
        record += "Phone Number: " + student.getPhoneNumber() + "\n";

        record += "\nEVENT DETAILS\n";
        record += "Name: " + performance.getEventTitle() + "\n";
        record += "Ticketed: " + performance.checkIfEventIsTicketed() + "\n";
        record += "Organiser: " + performance.getOrganizerEmail() + "\n";
        
        record += "\nPERFORMANCE DETAILS\n";
        record += "Venue: " + performance.getVenueAddress() + "\n";
        record += "Performers: " + String.join(", ", performance.getPerformerNames()) + "\n";
        record += "Ticket price: " + performance.getTicketPrice() + "\n";
        record += "Sponsored: " + performance.isSponsored() + "\n";
        record += "Sponsor amount: " + performance.getSponsoredAmount() + "\n";
        record += "Status: " + performance.getStatus() + "\n";

        return record;
    }
}