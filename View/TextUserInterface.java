package view;
import java.util.Collection;
import java.util.Scanner;

public class TextUserInterface implements View {

    private Scanner scanner = new Scanner(System.in);

    public String getInput(String inputPrompt) {
        System.out.println(inputPrompt);
        String input = scanner.nextLine();
        return input;
    }

    public void displaySuccess(String successMessage) {
        System.out.println(successMessage);
    }

    public void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }

    public void displayListofPerformances(Collection<String> listofPerformanceInfo) {
        for (String p : listofPerformanceInfo) {
            System.out.println(p);
        }
    }

    public void displaySpecificPerformance(String performanceInfo) {
        System.out.println(performanceInfo);
    }

    public void displayBookingRecord(String bookingRecord) {
        System.out.println(bookingRecord);
    }

}