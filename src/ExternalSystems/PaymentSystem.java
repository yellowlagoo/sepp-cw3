package src.ExternalSystems;
public interface PaymentSystem{

    public boolean processPayment(int numTickets, String eventTitle, String studentEmail, long studentPhone, String epEmail, double transactionAmount);

    public boolean processRefund(int numTickets, String eventTitle, String studentEmail, long studentPhone, String epEmail, double transactionAmount, String organiserMsg);

}