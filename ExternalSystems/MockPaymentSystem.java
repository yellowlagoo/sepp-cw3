package ExternalSystems;

public class MockPaymentSystem implements PaymentSystem{

    public boolean processPayment(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail, double transactionAmount) {
        return true;
    }

    public boolean processRefund(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail, double transactionAmount, String organiserMsg) {
        return true;
    }

}