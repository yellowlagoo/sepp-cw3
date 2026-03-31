package Controller;

import Model.User;
import Model.Event;
import Model.Performance;

public class EventPerformanceController extends Controller {

    private long nextEventID;
    private long nextPerformanceID;

    public EventPerformanceController(User currentUser, long nextEventID, long nextPerformanceID) {

        super(currentUser);
        this.nextEventID = nextEventID;
        this.nextPerformanceID = nextPerformanceID;

    }

    // Task 1 Use cases

    public Event createEvent() {
        return null;
        // This is a use case for task 1 (Karina's)
    }

    public void searchforPerformances() {
        // this is a use cae for task 1 (Toni's)
    }

    public void viewPerformance() {
        // this is a use case for task 1 (Michael's)
    }

    public void cancelPerformance() {
        // This is a use case for task 1 (Michael's)
    }

    private boolean checkIfSponsorshipPossible(Performance performance, int amount) {
        return false;
        // this will likely be used for toni's use case below
    }

    public void sponsorPerformance() {
        // This is a use case of task 1 (Toni's)
    }

    // Would be better to implement these as we do our use cases ???

    private void addEvent(Event e) {

    }

    private void addPerformance(Performance p) {

    }

    private Event getEventByID(long eventID) {
        return null;
    }

    private Event getEventByTitle(String title) {
        return null;
    }

    private Performance getPerformanceByID(long performanceID) {
        return null;
    }

}