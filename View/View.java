package view;

import java.util.Collection;

public interface View {
    public String getInput(String inputPrompt);

    public void displaySuccess(String successMessage);

    public void displayError(String errorMessage);

    public void displayListofPerformances(Collection<String> listOfPerformances);

    public void displaySpecificPerformance(String performanceInfo);

    public void displayBookingRecord(String bookingRecord);
}