import java.io.ObjectInputFilter.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Performance {

    // attributes
    private long performanceID;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Collection<String> performerNames;
    private String venueAddress;
    private int venueCapacity;
    private boolean venueIsOutdoors;
    private boolean allowSmoking;
    private int numTicketsTotal;
    private int numTicketsSold;
    private double ticketPrice;
    private boolean isSponsored;
    private double sponsoredAmount;
    private Collection<Integer> reviewRating;
    private Collection<String> reviewComments;
    private PerformanceStatus status;
    private Event event;
    private Collection<Booking> bookings;

    // Constructor
    public Performance(long performanceID, LocalDateTime startdateTime, LocalDateTime endDateTime,
            Collection<String> pefromerNames,
            String venueAddress, int venueCapacity, boolean venueIsOutdoors, boolean allowSmoking, int numTicketsTotal,
            int numTicketsSold,
            double ticketPrice, boolean isSponsored, double sponsoredAmount, Collection<Integer> reviewRating,
            Collection<String> reviewComments,
            Status performanceStatus) {

        this.performanceID = performanceID;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.venueAddress = venueAddress;
        this.venueCapacity = venueCapacity;
        this.venueIsOutdoors = venueIsOutdoors;
        this.allowSmoking = allowSmoking;
        this.numTicketsTotal = numTicketsTotal;
        this.numTicketsSold = numTicketsSold;
        this.ticketPrice = ticketPrice;
        this.isSponsored = isSponsored;
        this.sponsoredAmount = sponsoredAmount;
        this.reviewRating = reviewRating;
        this.reviewComments = reviewComments;
        this.status = status;
        this.status = status;

    }

    // Getters and setters
    public long getPerformanceID() {
        return performanceID;
    }

    public void setPerformanceID(long performanceID) {
        this.performanceID = performanceID;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Collection<String> getPerformerNames() {
        return performerNames;
    }

    public void setPerformerNames(Collection<String> performerNames) {
        this.performerNames = performerNames;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }

    public boolean isVenueIsOutdoors() {
        return venueIsOutdoors;
    }

    public void setVenueIsOutdoors(boolean venueIsOutdoors) {
        this.venueIsOutdoors = venueIsOutdoors;
    }

    public boolean isAllowSmoking() {
        return allowSmoking;
    }

    public void setAllowSmoking(boolean allowSmoking) {
        this.allowSmoking = allowSmoking;
    }

    public int getNumTicketsTotal() {
        return numTicketsTotal;
    }

    public void setNumTicketsTotal(int numTicketsTotal) {
        this.numTicketsTotal = numTicketsTotal;
    }

    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    public void setNumTicketsSold(int numTicketsSold) {
        this.numTicketsSold = numTicketsSold;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isSponsored() {
        return isSponsored;
    }

    public void setSponsored(boolean isSponsored) {
        this.isSponsored = isSponsored;
    }

    public double getSponsoredAmount() {
        return sponsoredAmount;
    }

    public void setSponsoredAmount(double sponsoredAmount) {
        this.sponsoredAmount = sponsoredAmount;
    }

    public Collection<Integer> getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(Collection<Integer> reviewRating) {
        this.reviewRating = reviewRating;
    }

    public Collection<String> getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(Collection<String> reviewComments) {
        this.reviewComments = reviewComments;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setStatus(PerformanceStatus status) {
        this.status = status;
    }

    // Methods
    public void cancel() {
        this.status = PerformanceStatus.CANCELLED;
    }

    public Boolean checkIfEventIsTicketed() {
        return event.isTicketed();
    }

    public Boolean checkIfTicketsLeft(int numTicketsToBuy) {
        return (numTicketsTotal - numTicketsSold) >= numTicketsToBuy;
    }

    public double getFinalTicketPrice() {
        if (isSponsored) {
            return (ticketPrice - sponsoredAmount);
        } else {
            return ticketPrice;
        }
    }

    public String getOrganiserEmail() { // Unsure about this part here as it is a method of both performance and event
                                        // so unsure who implements it and how
        return event.getOrganiserEmail;
    }

    public String getEventTitle() {
        return event.getTitle();
    }

    public boolean checkHasNotHappenedYet() {
        return startDateTime.isAfter(LocalDateTime.now());
    }

    public boolean checkCreatedByEP(String email) {
        String organiserEmail = event.getOrganiserEmail();
        return organiserEmail.equals(email);
    }

    public boolean hasActiveBookings() {
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                return true;
            }
        }

        return false;
    }

    public String getBookingDetailsForRefund() {
        String details = "STUDENT DETAILS\n";
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                details += "Student Details: " + b.getStudentDetails() + "\n";
                details += "Amount Paid: " + b.getAmountPaid() + "\n";
                details += "Number of tickets purchased: " + b.getNumTickets() + "\n";
            }
        }

        return details;

    }

    public void Sponsor(double amount) {
        this.isSponsored = true;
        this.sponsoredAmount = amount;
    }

    public void Rating(int rating, String comment) {
        reviewRating.add(rating);
        reviewComments.add(comment);
    }

    public void addBooking(Booking b) {
        bookings.add(b);
    }

    /*
     * 
     * public String toString(){
     * 
     * I do not know what this actually is ?
     * 
     * }
     * 
     * 
     */

}
