package tests.ExternalSystemsTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.ExternalSystems.MockPaymentSystem 

public class TestMockPaymentSystem {

    private MockPaymentSystem paymentSystem;

    @BeforeEach
    void setUp() {
        paymentSystem = new MockPaymentSystem();
    }

    // processPayment() 

    @Test
    void processPayment_typicalInputs_returnsTrue() {
        boolean result = paymentSystem.processPayment(
            2, "Jazz Night", "student@test.com", 7700000001, "ep@test.com", 40.00);
        assertTrue(result, "processPayment() with typical inputs should return true");
    }

    @Test
    void processPayment_singleTicket_returnsTrue() {
        boolean result = paymentSystem.processPayment(
            1, "Jazz Night", "student@test.com", 7700000001, "ep@test.com", 20.00);
        assertTrue(result, "processPayment() with one ticket should return true");
    }

    @Test
    void processPayment_zeroAmount_returnsTrue() {
        // Boundary: free event
        boolean result = paymentSystem.processPayment(
            1, "Free Event", "student@test.com", 7700000001, "ep@test.com", 0.00);
        assertTrue(result, "processPayment() with zero amount should return true");
    }

    @Test
    void processPayment_largeAmount_returnsTrue() {
        // Boundary: very large transaction
        boolean result = paymentSystem.processPayment(
            100, "Big Concert", "student@test.com", 7700000001, "ep@test.com", 10000.00);
        assertTrue(result, "processPayment() with large amount should return true");
    }

    @Test
    void processPayment_emptyEventTitle_returnsTrue() {
        // Unusual: empty title
        boolean result = paymentSystem.processPayment(
            1, "", "student@test.com", 7700000001, "ep@test.com", 20.00);
        assertTrue(result, "processPayment() with empty event title should return true");
    }

    // ---- processRefund() ----

    @Test
    void processRefund_typicalInputs_returnsTrue() {
        boolean result = paymentSystem.processRefund(
            2, "Jazz Night", "student@test.com", 7700000001,
            "ep@test.com", 40.00, "Event cancelled.");
        assertTrue(result, "processRefund() with typical inputs should return true");
    }

    @Test
    void processRefund_singleTicket_returnsTrue() {
        boolean result = paymentSystem.processRefund(
            1, "Jazz Night", "student@test.com", 7700000001,
            "ep@test.com", 20.00, "Cancelled.");
        assertTrue(result, "processRefund() with one ticket should return true");
    }

    @Test
    void processRefund_zeroAmount_returnsTrue() {
        // Boundary: refunding a free event
        boolean result = paymentSystem.processRefund(
            1, "Free Event", "student@test.com", 7700000001,
            "ep@test.com", 0.00, "Cancelled.");
        assertTrue(result, "processRefund() with zero amount should return true");
    }

    @Test
    void processRefund_largeAmount_returnsTrue() {
        boolean result = paymentSystem.processRefund(
            100, "Big Concert", "student@test.com", 7700000001,
            "ep@test.com", 10000.00, "Venue unavailable.");
        assertTrue(result, "processRefund() with large amount should return true");
    }

    @Test
    void processRefund_emptyOrganiserMessage_returnsTrue() {
        // Boundary: empty cancellation message
        boolean result = paymentSystem.processRefund(
            1, "Jazz Night", "student@test.com", 7700000001,
            "ep@test.com", 20.00, "");
        assertTrue(result, "processRefund() with empty message should return true");
    }

    @Test
    void processRefund_emptyEventTitle_returnsTrue() {
        // Unusual: empty title
        boolean result = paymentSystem.processRefund(
            1, "", "student@test.com", 7700000001,
            "ep@test.com", 20.00, "Cancelled.");
        assertTrue(result, "processRefund() with empty event title should return true");
    }
}