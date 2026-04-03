package ExternalSystems;
public interface PaymentSystem{

    public boolean processPayment(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail, double transactionAmount);

    public boolean processRefund(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail, double transactionAmount, String organiserMsg);

}