package src.Controller;

import src.ExternalSystems.*;
import src.Model.*;
import src.View.*;
import java.util.*;

public class EventPerformanceController extends Controller {
    private long nextEventID;
    private long nextPerformanceID;
    private PaymentSystem paymentSystem;
    private Collection<Event> events;
    private Collection<Performance> performances;

    /**
     * Constructor for the EventPerformanceController class
     * @param currentUser - the current user of the system
     * @param nextEventID - the next ID to be given to an event
     * @param nextPerformanceID - the next ID to be given to a performance
     * @param paymentSystem - the payment system to be interfaced with
     * @param view - the user interface of the system
     */
    public EventPerformanceController(User currentUser, long nextEventID, long nextPerformanceID,
            PaymentSystem paymentSystem, TextUserInterface view) {

        super(currentUser, view);
        this.nextEventID = nextEventID;
        this.nextPerformanceID = nextPerformanceID;
        this.paymentSystem = paymentSystem;
        performances = new ArrayList<>();
    }

    /**
     * 
     * 
     * @return event that was created
     */
    public Event createEvent() {
        if (super.checkCurrentUserIsEntertainmentProvider()) {
            EntertainmentProvider organizer = (EntertainmentProvider) super.getCurrentUser();
            
            String eventIDInput = view.getInput("Enter ID of event to view");

            long eventID = Long.parseLong(eventIDInput);

            String title = view.getInput("Enter title of event");

            //make sure declaring variables as null does not prematurely cause a NullPointerException 
            String typeInput = view.getInput("Enter event type");
            EventType type = null;
            try {
                type = EventType.findByName(typeInput);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("This is an invalid event type");
            }

            String isTicketedInput = view.getInput("Is the event ticketed?");
            Boolean isTicketed = null;
            try {
                isTicketedInput = isTicketedInput.toLowerCase();
                if ("true".equals(isTicketedInput) || "false".equals(isTicketedInput)) {
                    isTicketed = Boolean.parseBoolean(isTicketedInput);
                } else {
                    throw new IllegalArgumentException("Is ticketed must be True or False (not case sensitive).");
                }
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Is ticketed can't be empty");
            }
            
            Event newEvent = new Event(organizer, eventID, title, type, isTicketed);
            this.addEvent(newEvent);
            return newEvent;
        } else {
            throw new Error("the current user is not ");
        }
    }

    public Performance searchForPerformances() {
        // this is a use cae for task 1 (Karina's)
        String performanceIDInput = view.getInput("Enter ID of performance to view");
        long performanceID = Long.parseLong(performanceIDInput);
        Performance performance = this.event.getPerformanceByID(performanceID);
        return performance;
    }

    // viewPerformance() (Michael's)
    public void viewPerformance() {

        String performanceIDInput = view.getInput("Enter ID of performance to view");
        long performanceID = Long.parseLong(performanceIDInput);

        Performance performance = getPerformanceByID(performanceID);

        if (performance == null) {
            view.displayError("Performance with given number does not exits");
            return;
        }

        else {

            view.displaySpecificPerformance(performance.toString());

        }

    }

    // cancelPerformance() Use case (Michael's)
    public void cancelPerformance() {

        Performance performance = null;
        boolean sameEP = false;
        boolean hasNotHappenedYet = false;

        while (performance == null || sameEP == false || hasNotHappenedYet == false) {

            String performanceIDInput = view.getInput("Enter ID of performance to cancel");
            long performanceID = Long.parseLong(performanceIDInput);

            performance = getPerformanceByID(performanceID);

            if (performance == null) {
                view.displayError("Performance with given number does not exist");
                continue;
            }

            EntertainmentProvider currEP = (EntertainmentProvider) getCurrentUser();

            String currEp = currEP.getEmail();

            sameEP = performance.checkCreatedByEP(currEp);

            if (sameEP == false) {
                view.displayError("The performance with given number does not belong to you");
                continue;
            }

            hasNotHappenedYet = performance.checkHasNotHappenedYet();

            if (hasNotHappenedYet == false) {

                view.displayError("Performance cant't be cancelled as it has already happened");
                continue;

            }
        }

        String organiserMessage = null;

        while (organiserMessage == null || organiserMessage.isEmpty()) {

            String organiserMessageInpt = view.getInput("Provide a cancellation message for affected students:");
            organiserMessage = organiserMessageInpt;

            if (organiserMessage.isEmpty()) {

                view.displayError("Please provide a non-empty cancellation message for the students");
                continue;

            }
        }

        boolean hasActiveBookings = performance.hasActiveBookings();

        if (hasActiveBookings == true) {

            String eventTitle = performance.getEventTitle();
            EntertainmentProvider ep = (EntertainmentProvider) getCurrentUser();
            String emailEP = ep.getEmail();
            String bookingDetailsForRefund = performance.getBookingDetailsForRefund();

            String[] bookingEntries = bookingDetailsForRefund.split("\n\n");

            for (String bookingEntry : bookingEntries) {

                String[] lines = bookingEntry.split("\n");
                String studentEmail = lines[0];
                int studentPhone = Integer.parseInt(lines[1]);
                double transactionAmount = Double.parseDouble(lines[2]);
                int numTickets = Integer.parseInt(lines[3]);

                boolean refundSuccessful = paymentSystem.processRefund(numTickets, eventTitle, studentEmail,
                        studentPhone, emailEP, transactionAmount, organiserMessage);

                if (refundSuccessful == false) {
                    view.displayError("There was an issue with a refund.  The performance cannot be cancelled");
                    return;
                }
            }

            for (Booking b : performance.getBookings()) {

                if (b.getStatus() == BookingStatus.ACTIVE) {

                    b.cancelByProvider();

                }
            }
        }
        performance.cancel();
        view.displaySuccess("Cancellation successful!");
    }

    private boolean checkIfSponsorshipPossible(Performance performance, int amount) {
        boolean ticketed = performance.getEvent().isTicketed();
        if(!ticketed){
            view.displayError("The requested performance's event is nonticketed. It cannot be sponsored.");
            return false;
        }
        else if(amount < 0 || amount > performance.getFinalTicketPrice()){
            view.displayError("The amount provided is invalid.");
            return false;
        }
        return true;
    }

    public void sponsorPerformance() {
        Performance performance = null;
        boolean possible = false;
        int amount = 0;

        while(performance == null || possible == false){
            String performanceIDinput = view.getInput("Enter performance ID: ");
            long performanceID = Long.parseLong(performanceIDinput);
            performance = getPerformanceByID(performanceID);

            if(performance == null){
                view.displayError("Performance with given number does not exist");
            }
            else{
                String amountInput = view.getInput("Enter amount to sponsor by: ");
                amount = Integer.parseInt(amountInput);
                possible = checkIfSponsorshipPossible(performance, amount);
            }
        }

        performance.sponsor(amount);
        performance.setSponsored(true);
        performance.setSponsoredAmount(amount);

        view.displaySuccess("Sponsorship successfully processed.");
    }

    // Would be better to implement these as we do our use cases ???

    private void addEvent(Event e) {
        this.events.add(e);
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
        for (Performance p : performances) {
            if (p.getPerformanceID() == performanceID) {
                return p;
            }
        }
        return null;
    }

}
