package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
    public Performance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Collection<String> performerNames,
            String venueAddress, int venueCapacity, boolean venueIsOutdoors, boolean allowSmoking, int numTicketsTotal,
            double ticketPrice, Event event) {

        this.performanceID = performanceID;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.venueAddress = venueAddress;
        this.venueCapacity = venueCapacity;
        this.venueIsOutdoors = venueIsOutdoors;
        this.allowSmoking = allowSmoking;
        this.numTicketsTotal = numTicketsTotal;
        numTicketsSold = 0;
        this.ticketPrice = ticketPrice;
        isSponsored = false;
        sponsoredAmount = 0;
        reviewRating = Collections.emptyList(); 
        reviewComments = Collections.emptyList();
        status = PerformanceStatus.ACTIVE;
        this.event = event;
        bookings = new ArrayList<>();
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

    public Collection<Booking> getBookings() {
        return bookings;
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

    public String getOrganiserEmail() {

        return event.getOrganiserEmail();
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
        String details = "";
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                String studentdetails = b.getStudentDetails() + "\n";
                double amountPaid = b.getAmountPaid();
                int numTickets = b.getNumTickets();

                details += studentdetails + "\n " + amountPaid + "\n " + numTickets + "\n\n";
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
