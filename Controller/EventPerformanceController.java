package Controller;

import Model.*;
import ExternalSystems.PaymentSystem;

public class EventPerformanceController extends Controller {

    private long nextEventID;
    private long nextPerformanceID;
    private PaymentSystem paymentSystem;

    public EventPerformanceController(User currentUser, long nextEventID, long nextPerformanceID, PaymentSystem paymentSystem, View view) {

        super(currentUser, view);
        this.nextEventID = nextEventID;
        this.nextPerformanceID = nextPerformanceID;
        this.paymentSystem = paymentSystem;
    }


    // Task 1 Use cases

    public Event createEvent(String organizer, String title, String type, long eventID, Boolean isTicketed) {
        Event newEvent = new Event(organizer, eventID, title, type, isTicketed);
        this.event = newEvent;
        return newEvent;
        // This is a use case for task 1 (Karina's)
    }

    public Performance searchForPerformances(long performanceID) {
        // this is a use cae for task 1 (Karina's)
        String performanceIDInput = view.getInput("Enter ID of performance to view");
        long performanceID = Long.parseLong(performanceIDInput);
        Performance performance = event.getPerformanceByID(performanceID);
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

        while (organiserMessage == null || organiserMessage.isEmpty()){

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

                boolean refundSuccessful = paymentSystem.processRefund(numTickets, eventTitle, studentEmail, studentPhone, emailEP, transactionAmount, organiserMessage);

                if (refundSuccessful == false) {
                    view.displayError("There was an issue with a refund.  The performance cannot be cancelled");
                    return;
                }
            }

            for (Booking b: performance.getBookings()) {

                if (b.getStatus() == BookingStatus.ACTIVE) {

                    b.cancelByProvider();

                }
            }
        }
            performance.cancel();
            view.displaySuccess("Cancellation successful!");
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
