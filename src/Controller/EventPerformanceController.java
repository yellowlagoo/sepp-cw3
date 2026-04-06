package src.Controller;

import src.external.*;
import src.Model.*;
import src.View.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller responsible for event and performance-related operations
 * including creating events, searching, viewing, cancelling,
 * and sponsoring performances.
 */
public class EventPerformanceController extends Controller {
    private long nextEventID;
    private long nextPerformanceID;
    private PaymentSystem paymentSystem;
    private Collection<Event> events;
    private Collection<Performance> performances;

    /**
     * Constructor for the EventPerformanceController class.
     * 
     * @param nextEventID       - the next ID to be given to an event
     * @param nextPerformanceID - the next ID to be given to a performance
     * @param paymentSystem     - the payment system to be interfaced with
     * @param view              - the user interface of the system
     */
    public EventPerformanceController(long nextEventID, long nextPerformanceID,
            PaymentSystem paymentSystem, TextUserInterface view, Collection<Performance> performances) {
        super(view);
        this.nextEventID = nextEventID;
        this.nextPerformanceID = nextPerformanceID;
        this.paymentSystem = paymentSystem;
        this.events = new ArrayList<>();
        this.performances = performances;
    }

    /**
     * Creates a new event and optionally adds performances to it.
     * Only entertainment providers can create events.
     * Auto-assigns the next available event ID.
     * 
     * @return the newly created Event, or null if creation failed
     */
    public Event createEvent() {
        try {
            if (!super.checkCurrentUserIsEntertainmentProvider()) {
                view.displayError("Must be an entertainment provider to create an event");
                return null;
            }
        } catch (NullPointerException e) {
            view.displayError("Must be logged in to create an event");
            throw new NullPointerException(super.getErrMsg());
        }

        EntertainmentProvider organizer = (EntertainmentProvider) this.getCurrentUser();

        long eventID = this.nextEventID++;

        String title = view.getInput("Enter title of event");

        String typeInput = view.getInput("Enter event type (Music, Theatre, Dance, Movie, Sports)");
        EventType type = null;
        try {
            type = EventType.findByName(typeInput);
        } catch (NullPointerException e) {
            view.displayError("This is an invalid event type");
            throw new IllegalArgumentException("This is an invalid event type");
        }

        String isTicketedInput = view.getInput("Is the event ticketed? (true/false)");
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

        // Ask if the user wants to add performances to the event
        String addPerformance = view.getInput("Would you like to add a performance to this event? (true/false)");
        while (addPerformance != null && addPerformance.trim().toLowerCase().equals("true")) {
            try {
                Performance performance = createPerformance(newEvent);
                if (performance != null) {
                    view.displaySuccess("Performance added successfully with ID: " + performance.getPerformanceID());
                }
            } catch (Exception e) {
                view.displayError("Error creating performance: " + e.getMessage());
            }
            addPerformance = view.getInput("Would you like to add another performance? (true/false)");
        }

        view.displaySuccess("Event '" + title + "' created successfully with ID: " + eventID);
        return newEvent;
    }

    /**
     * Creates a new performance and adds it to the given event.
     * Delegates to the Event model's createPerformance method.
     * 
     * @param event - the event to add the performance to
     * @return the newly created Performance
     */
    private Performance createPerformance(Event event) {
        long performanceID = this.nextPerformanceID++;

        String startDateInput = view.getInput("Enter start date and time (yyyy-MM-dd HH:mm)");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateInput,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String endDateInput = view.getInput("Enter end date and time (yyyy-MM-dd HH:mm)");
        LocalDateTime endDateTime = LocalDateTime.parse(endDateInput,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String performerNamesInput = view.getInput("Enter performer names (comma-separated)");
        Collection<String> performerNames = new ArrayList<>();
        for (String name : performerNamesInput.split(",")) {
            performerNames.add(name.trim());
        }

        String venueAddress = view.getInput("Enter venue address");

        String venueCapacityInput = view.getInput("Enter venue capacity");
        int venueCapacity = Integer.parseInt(venueCapacityInput);

        String venueIsOutdoorsInput = view.getInput("Is the venue outdoors? (true/false)");
        boolean venueIsOutdoors = Boolean.parseBoolean(venueIsOutdoorsInput.trim().toLowerCase());

        String allowSmokingInput = view.getInput("Does the venue allow smoking? (true/false)");
        boolean allowSmoking = Boolean.parseBoolean(allowSmokingInput.trim().toLowerCase());

        String numTicketsInput = view.getInput("Enter total number of tickets");
        int numTickets = Integer.parseInt(numTicketsInput);

        double ticketPrice = 0;
        if (event.isTicketed()) {
            String ticketPriceInput = view.getInput("Enter ticket price");
            ticketPrice = Double.parseDouble(ticketPriceInput);
        }

        // Delegate to Event model — it creates the Performance and adds it internally
        Performance performance = event.createPerformance(performanceID, startDateTime, endDateTime,
                performerNames, venueAddress, venueCapacity, venueIsOutdoors, allowSmoking,
                numTickets, ticketPrice);

        // Also add to the controller's collection so
        // searchForPerformances/viewPerformance can find it
        this.addPerformance(performance);

        return performance;
    }

    /**
     * Displays a list of all performances in the system.
     * If no performances exist, displays an appropriate message.
     */
    public void searchForPerformances() {
        if (performances.isEmpty()) {
            view.displayError("There are no performances in the system.");
            return;
        }

        Collection<String> performanceInfoList = new ArrayList<>();

        if (performanceInfoList.isEmpty()) {
            view.displayError("There are no performances in the system.");
        }

        for (Performance p : performances) {
            if (p.getStatus() != PerformanceStatus.CANCELLED) {
            performanceInfoList.add("ID: " + p.getPerformanceID()
                    + " | " + p.getEventTitle()
                    + " | " + p.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    + " | " + p.getVenueAddress()
                    + " | Status: " + p.getStatus());
            }
        }

        view.displayListofPerformances(performanceInfoList);
    }

    /**
     * Displays the details of a performance identified by the user-provided ID.
     */
    public void viewPerformance() {
        String performanceIDInput = view.getInput("Enter ID of performance to view");
        try {
            long performanceID = Long.parseLong(performanceIDInput);
            Performance performance = getPerformanceByID(performanceID);
            if (performance == null) {
                view.displayError("Performance with given number does not exist");
                return;
            }
            view.displaySpecificPerformance(performance.toString());
        } catch (NumberFormatException e) {
            view.displayError("Performance ID must be a number");
        }
    }

    /**
     * Cancels a performance. Only the entertainment provider who created the
     * performance can cancel it, and only if it has not yet happened.
     * If the performance has active bookings, refunds are processed before
     * cancellation.
     */
    public void cancelPerformance() {
        Performance performance = null;
        boolean sameEP = false;
        boolean hasNotHappenedYet = false;

         // Ensures current user is a student, ending booking process if not
        try {
            if (!super.checkCurrentUserIsEntertainmentProvider()) {
                view.displayError("Only an entertainment provider can cancel a performance");
                return;
            }
        } catch (NullPointerException e) {
            view.displayError("Must be a logged in to cancel a performance");
            return;
        }

        
        while (performance == null || sameEP == false || hasNotHappenedYet == false) {
            String performanceIDInput = view.getInput("Enter ID of performance to cancel");
            try {
                long performanceID = Long.parseLong(performanceIDInput);
                performance = getPerformanceByID(performanceID);
            } catch (NumberFormatException e) {
                view.displayError("Performance ID must be a number");
                continue;
            }

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
                view.displayError("Performance can't be cancelled as it has already happened");
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
                    view.displayError("There was an issue with a refund. The performance cannot be cancelled");
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

    /**
     * Checks whether a performance can be sponsored with the given amount.
     * The performance's event must be ticketed, and the amount must be
     * between 0 and the final ticket price.
     * 
     * @param performance - the performance to check
     * @param amount      - the proposed sponsorship amount
     * @return true if sponsorship is possible, false otherwise
     */
    private boolean checkIfSponsorshipPossible(Performance performance, double amount) {
        boolean ticketed = performance.getEvent().isTicketed();
        if (!ticketed) {
            view.displayError("The requested performance's event is nonticketed. It cannot be sponsored.");
            return false;
        } else if (amount < 0 || amount > performance.getFinalTicketPrice()) {
            view.displayError("The amount provided is invalid.");
            return false;
        }
        return true;
    }

    /**
     * Sponsors a performance by reducing its ticket price by the given amount.
     * Prompts the admin for a performance ID and sponsorship amount, validating
     * both before applying the sponsorship.
     */
    public void sponsorPerformance() {
        Performance performance = null;
        boolean possible = false;
        double amount = 0;

        while (performance == null || possible == false) {
            String performanceIDinput = view.getInput("Enter performance ID: ");
            try {
                long performanceID = Long.parseLong(performanceIDinput);
                performance = getPerformanceByID(performanceID);
            } catch (NumberFormatException e) {
                view.displayError("Performance ID must be a number");
                continue;
            }

            if (performance == null) {
                view.displayError("Performance with given number does not exist");
            } else {
                String amountInput = view.getInput("Enter amount to sponsor by: ");
                try {
                    amount = Double.parseDouble(amountInput);
                } catch (NumberFormatException e) {
                    view.displayError("Amount must be a number");
                    performance = null;
                    continue;
                }
                possible = checkIfSponsorshipPossible(performance, amount);
            }
        }

        performance.sponsor(amount);
        performance.setSponsored(true);
        performance.setSponsoredAmount(amount);

        view.displaySuccess("Sponsorship successfully processed.");
    }

    /**
     * Adds an event to the collection of events.
     * 
     * @param e - the event to add
     */
    private void addEvent(Event e) {
        this.events.add(e);
    }

    /**
     * Adds a performance to the collection of performances.
     * 
     * @param p - the performance to add
     */
    private void addPerformance(Performance p) {
        this.performances.add(p);
    }

    /**
     * Finds an event by its ID.
     * 
     * @param eventID - the ID of the event to find
     * @return the Event if found, or null if not found
     */
    private Event getEventByID(long eventID) {
        for (Event e : this.events) {
            if (e.getEventID() == eventID) {
                return e;
            }
        }
        return null;
    }

    /**
     * Finds an event by its title.
     * 
     * @param title - the title of the event to find
     * @return the Event if found, or null if not found
     */
    private Event getEventByTitle(String title) {
        for (Event e : this.events) {
            if (e.getTitle().equals(title)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Finds a performance by its ID.
     * 
     * @param performanceID - the ID of the performance to find
     * @return the Performance if found, or null if not found
     */
    private Performance getPerformanceByID(long performanceID) {
        for (Performance p : performances) {
            if (p.getPerformanceID() == performanceID) {
                return p;
            }
        }
        return null;
    }

    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
    }

    public User getCurrentUser() {
        return super.getCurrentUser();
    }
}