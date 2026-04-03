package model; 

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
            boolean venueAllowsSmoking, int numTickets, double ticketPrice) {
        Performance p = new Performance(performanceID, startDateTime, endDateTime, performerNames, venueAddress,
            venueCapacity, venueIsOutdoors, venueAllowsSmoking, numTickets, ticketPrice, this
        );
        addPerformance(p);
        return p;
    }

    /**
     * Returns the performance associated with a particular ID
     * Returns null if the given performanceID does not match one of the event's associated performances
     * @param performanceID - the performance ID to search for
     * @return the performance associated with that ID
     */
    public Performance getPerformanceByID(long performanceID){
        Performance matchingID = null;
        for(Performance p : performances){
            if(p.getPerformanceID() == performanceID){
                matchingID = p;
            }
        }
        return matchingID;
    }

    /**
     * Returns the information of all the performances for a particular date and time
     * Assumes that the search date will be some ongoing time between the start and end time of the performance
     * @param searchDateTime - the time to be searched for
     * @return a list of information of all of the performances for that date and time
     */
    public Collection<String> getInfoOfPerformancesOnDate(LocalDateTime searchDateTime){
        Collection<String> infos = Collections.emptyList();
        for(Performance p: performances){
            LocalDateTime startTime = p.getStartDateTime();
            LocalDateTime endTime = p.getEndDateTime();
            if(!(startTime.isAfter(searchDateTime) || endTime.isBefore(searchDateTime))){
                infos.add(p.toString());
            }
        }
        return infos;
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

    /**
     * Returns the average rating of all the performances associated with the event
     * @return average rating of the performances
     */
    public double getAverageRatingOfPerformances(){
        double sum = 0;
        int numRatings = 0;
        for (Performance p : performances){
            for (int rating : p.getReviewRating()){
                sum += rating;
                numRatings++;
            }
        }
        return sum / numRatings;
    }

    /**
     * Returns a list of all of the reviews for all of the performances of the event
     * @return a collection of all of the reviews for the performances associated with the event
     */
    public Collection<String> getAllPerformanceReviews(){
        Collection<String> reviews = Collections.emptyList();
        for(Performance p : performances){
            reviews.addAll(p.getReviewComments());
        }
        return reviews;
    }

    /**
     * Returns whether or not there is an event with the exact same start and end time as specified
     * @param startDateTime - the start time to search for
     * @param endDateTime - the end time to search for
     * @return whether or not there is a performance for those exact times
     */
    private boolean hasPerformanceAtSameTimes(LocalDateTime startDateTime, LocalDateTime endDateTime){
        for(Performance p : performances){
            if(p.getStartDateTime().isEqual(startDateTime) && p.getEndDateTime().isEqual(endDateTime))
                return true;
        }
        return false;
    }

    /**
     * Adds a new performance to the list of performances associated with the event
     * @param p the performance to be added to the event
     */
    private void addPerformance(Performance p){
        performances.add(p);
    }

    @Override
    public String toString(){
        String ticketed;
        if(isTicketed)
            ticketed = "The event is ticketed";
        else
            ticketed = "The event is not ticketed";
        
        return "Event Organizer: " + organizer.getOrgName()
        + "\nEvent ID: " + eventID
        + "\nEvent Title: " + title
        + "\nEvent Type: " + type
        + "\n" + ticketed
        + "\nThere are " + performances.size() + " performances for this event";
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