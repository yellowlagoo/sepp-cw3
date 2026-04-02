package view;

import java.util.Collection;

public interface View {
    /**
     * Prompts the user to provide input by printing out a message, then returns the user's input
     * @param inputPrompt - the message prompting user input
     * @return - the user's input
     */
    public String getInput(String inputPrompt);

    /**
     * Displays a message indicating a successful operation
     * @param successMessage - the message to display
     */
    public void displaySuccess(String successMessage);

    /**
     * Displays a message indicating an error
     * @param errorMessage - the message to display
     */
    public void displayError(String errorMessage);

    /**
     * Displays a list of relevant performances to the user
     * @param listOfPerformances - the performances to be displayed
     */
    public void displayListofPerformances(Collection<String> listOfPerformances);

    /**
     * Displays the in-depth information about a performance, such as venue and reviews
     * @param performanceInfo - the performance to be displayed
     */
    public void displaySpecificPerformance(String performanceInfo);

    /**
     * Displays a booking record once the student has successfully booked a performance
     * @param bookingRecord - the booking record to be displayed
     */
    public void displayBookingRecord(String bookingRecord);
}