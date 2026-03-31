package Model; 

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Event{
    private EntertainmentProvider organizer;
    private long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;
    private ArrayList<Performance> performances;

    /**
     * Constructor for the event class
     * @param organizer - the entertainment provider that created the event
     * @param eventID - the event's ID
     * @param title - the event's title
     * @param type - the event's type (music, sports, etc.)
     * @param isTicketed - whether or not the event is ticketed
     */
    public Event(EntertainmentProvider organizer, long eventID, String title, EventType type, boolean isTicketed) {
        this.organizer = organizer;
        this.eventID = eventID;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        performances = new ArrayList<>();
    }

    /**
     * Creates a particular performance for the event
     * @param performanceID - the performance ID of the performance
     * @param startDateTime - when the performance starts
     * @param endDateTime - when the performance ends
     * @param performerNames - the names of the performers 
     * @param venueAddress - the address of the venue
     * @param venueCapacity - the capacity of the venue
     * @param venueIsOutdoors - whether or not the venue is outdoors
     * @param venueAllowsSmoking - whether or not the value allows smoking
     * @param numTickets - number of tickets available for the performance
     * @param ticketPrice - the ticket price of the performance (if ticketed)
     * @return the performance created
     */
    public Performance createPerformance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, 
        Collection<String> performerNames, String venueAddress, int venueCapacity, boolean venueIsOutdoors,
        boolean venueAllowsSmoking, int numTickets, double ticketPrice){
        Performance p = new Performance(performanceID, startDateTime, endDateTime, performerNames, venueAddress,
            venueCapacity, venueIsOutdoors, venueAllowsSmoking, numTickets, ticketPrice, this
        )
        addPerformance(p);
        return p;
    }

    //FINISH
    public Performance getPerformanceByID(long performanceID){
        //TO DO: iterate thru performances to find one with matching ID, return it
        //TO DO: how do I check to make sure it matches one of them?? I can't just return false or smth??
        return null;
    }

    //FINISH
    public Collection<String> getInfoOfPerformancesOnDate(LocalDateTime searchDateTime){
        return null;
    }

    /**
     * Returns the name of the entertainment provider that organized the event
     * @return the entertainment provider's name
     */
    private String getOrganizerName(){
        return organizer.getName();
    }

    /**
     * Returns the email of the entertainment provider that organized the event
     * @return the entertainment provider's email
     */
    public String getOrganizerEmail(){
        return organizer.getEmail();
    }

    //FINISH
    public double getAverageRatingOfPerformances(){
        double sum = 0;
        int numRatings = 0;
        for (Performance p : performances){
            for (int rating in p.getReviewRatings()){ //TO DO: make sure this method name is correct
                sum += rating;
                numRatings++;
            }
        }

        return sum / numRatings;
    }

    //FINISH
    public Collection<String> getAllPerformanceReviews{
        return null;
    }

    //FINISH
    private boolean hasPerformanceAtSameTimes(LocalDateTime startDateTime, LocalDateTime endDateTime){
        //TO DO: loop thru all of the performances associated with the event to see if they overlap
        return false;
    }

    /**
     * Adds a new performance to the list of performances associated with the event
     * @param p the performance to be added to the event
     */
    private void addPerformance(Performance p){
        performances.add(p);
    }

    //FINISH
    public String toString(){
        return "";
    }

    // Getters and setters
    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public boolean isTicketed() {
        return isTicketed;
    }

    public void setIsTicketed(boolean isTicketed) {
        this.isTicketed = isTicketed;
    }    
}