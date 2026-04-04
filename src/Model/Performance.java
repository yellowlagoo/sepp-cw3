package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Performance {
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

    /**
     * Constructor for the performance class
     * @param performanceID - the performance's unique ID
     * @param startDateTime - the time & date the performance starts
     * @param endDateTime - the time & date the performance ends
     * @param performerNames - the names of the performers
     * @param venueAddress - the address of the venue
     * @param venueCapacity - the capacity of the venue
     * @param venueIsOutdoors - whether or not the venue is outdoors
     * @param allowSmoking - whether or not the venue allows smoking
     * @param numTicketsTotal - the number of tickets available to be booked
     * @param ticketPrice - the price of a ticket
     * @param event - the event that the performance is associated with
     */
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

    public Event getEvent() {
        return event;
    }

    /**
     * Cancels a performance by changing its status
     */
    public void cancel() {
        this.status = PerformanceStatus.CANCELLED;
    }

    /**
     * Whether or not the event associated with the performance is ticketed
     * @return whether or not the evene is ticketed
     */
    public Boolean checkIfEventIsTicketed() {
        return event.isTicketed();
    }

    /**
     * Checks to see if there are enough tickets available to purchase
     * @param numTicketsToBuy - the number of tickets a student is looking to purchase
     * @return - whether or not that number of tickets is available
     */
    public Boolean checkIfTicketsLeft(int numTicketsToBuy) {
        return (numTicketsTotal - numTicketsSold) >= numTicketsToBuy;
    }

    /**
     * Returns the ticket price of the performance, factoring in if it has been sponsored by a staff member
     * @return - the ticket price
     */
    public double getFinalTicketPrice() {
        if (isSponsored) {
            return (ticketPrice - sponsoredAmount);
        } else {
            return ticketPrice;
        }
    }

    /**
     * Returns the email address of the entertainment provider that has created the perormance
     * @return - the organizer's email
     */
    public String getOrganizerEmail() {
        return event.getOrganizerEmail();
    }

    /**
     * Returns the title of the event associated with the performance
     * @return - the title of the event associated with the performance
     */
    public String getEventTitle() {
        return event.getTitle();
    }

    /**
     * Checks whether or not the event has already happened/begun
     * @return whether or not the event is happening after now
     */
    public boolean checkHasNotHappenedYet() {
        return startDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Checks whether the performance was created by a particular entertainment provider based on their email
     * @param email - the email of the entertainment provider to check
     * @return - whether or not the entertainment provider with the given email has created this performance
     */
    public boolean checkCreatedByEP(String email) {
        String organizerEmail = event.getOrganizerEmail();
        return organizerEmail.equals(email);
    }

    /**
     * Checks whether the performance has any active bookings, meaning it was not cancelled or had failed payment
     * @return whether there are any active bookings
     */
    public boolean hasActiveBookings() {
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the details of all active bookings for a performance to be used for refunds if a performance is cancelled
     * @return the booking details of all active bookings of the performance
     */
    public String getBookingDetailsForRefund() {
        String details = "";
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                String studentdetails = b.getStudentDetails();
                double amountPaid = b.getAmountPaid();
                int numTickets = b.getNumTickets();

                details += studentdetails + "\n " + amountPaid + "\n " + numTickets + "\n\n";
            }
        }
        return details;
    }

    /**
     * Reduces the ticket price of the performance when an admin staff chooses to sponsor it
     * @param amount - the amount to reduce the ticket price by
     */
    public void sponsor(double amount) {
        this.isSponsored = true;
        this.sponsoredAmount = amount;
    }

    /**
     * Adds a review to the performance, including a rating and a comment
     */
    public void review(int rating, String comment) {
        reviewRating.add(rating);
        reviewComments.add(comment);
    }

    /**
     * Adds a student's booking to the list of bookings associated with the performance
     * @param b - the booking to be added to the performance
     */
    public void addBooking(Booking b) {
        bookings.add(b);
    }

    @Override
    public String toString(){
        String message = "Performance ID: " + performanceID + ": \n";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        message += "Start time: " + formatter.format(startDateTime) + "\n";
        message += "End time: " + formatter.format(endDateTime) + "\n";

        message += "Performer names: " + String.join(", ", performerNames) + "\n";

        message += "Venue address: " + venueAddress + "\n";
        message += "Venue capacity: " + venueCapacity + "\n";
        message += "Is venue outdoors? " + venueIsOutdoors + "\n";
        message += "Does venue allow smoking? " + allowSmoking + "\n";

        message += "The performance has " + numTicketsTotal + " total tickets, " 
                + numTicketsSold + " of which have been sold\n"; 
        message += "Original ticket price: " + ticketPrice + "\n";

        if(isSponsored)
            message += "Sponsored ticket price: " + getFinalTicketPrice() + "\n";

        message += "The performance has " + reviewRating.size() + " reviews\n";
        message += "Status: " + status + "\n";
        message += "The performance has " + bookings.size() + "bookings";

        return message;
    }
}
