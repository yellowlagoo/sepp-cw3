package src.View;
import java.util.*;

public class TextUserInterface implements View {
    @Override
    public String getInput(String inputPrompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(inputPrompt);
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }

    @Override
    public void displaySuccess(String successMessage) {
        System.out.println(successMessage);
    }

    @Override
    public void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }

    @Override
    public void displayListofPerformances(Collection<String> listofPerformanceInfo) {
        for (String p : listofPerformanceInfo) {
            System.out.println(p);
        }
    }

    @Override
    public void displaySpecificPerformance(String performanceInfo) {
        System.out.println(performanceInfo);
    }

    @Override
    public void displayBookingRecord(String bookingRecord) {
        System.out.println(bookingRecord);
    }

}