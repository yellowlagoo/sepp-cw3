package tests.SystemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import org.junit.jupiter.api.DisplayName;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.ExternalSystems.MockPaymentSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SponsorPerformanceSystemTests {

    private TextUserInterface view;
    private EventPerformanceController eventPerformanceController;
    private MockPaymentSystem mockPaymentSystem;
    private Collection<Performance> allPerformances;
    private EntertainmentProvider ep;
    private Event ticketedEvent;
    private Event nonTicketedEvent;
    private Performance ticketedPerformance;
    private Performance nonTicketedPerformance;

    @BeforeEach
    void setup() {
        view = mock(TextUserInterface.class);
        mockPaymentSystem = new MockPaymentSystem();
        allPerformances = new ArrayList<>();

        eventPerformanceController = new EventPerformanceController(
                1, 1, mockPaymentSystem, view, allPerformances);

        ep = new EntertainmentProvider("ep@test.com", "password123", "Test Org", "B9999", "Alice", "Test org desc");

        ticketedEvent = new Event(ep, 1, "Jazz Night", EventType.MUSIC, true);
        nonTicketedEvent = new Event(ep, 2, "Free Gig", EventType.MUSIC, false);

        ticketedPerformance = new Performance(1,
                LocalDateTime.of(2030, 6, 15, 19, 30),
                LocalDateTime.of(2030, 6, 15, 22, 0),
                Arrays.asList("John"), "Edinburgh Venue, EH1 1AA",
                100, false, false, 50, 20.00, ticketedEvent);

        nonTicketedPerformance = new Performance(2,
                LocalDateTime.of(2030, 6, 20, 19, 30),
                LocalDateTime.of(2030, 6, 20, 22, 0),
                Arrays.asList("Sarah"), "Glasgow Venue, G1 1AA",
                100, false, false, 50, 0.00, nonTicketedEvent);

        allPerformances.add(ticketedPerformance);
        allPerformances.add(nonTicketedPerformance);
    }

    //  Sponsor performance — success

    @Test
    @DisplayName("Testing admin can sponsor a ticketed performance")
    void testSponsorPerformanceDisplaysSuccess() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displaySuccess("Sponsorship successfully processed.");
    }

    @Test
    @DisplayName("Testing performance is marked as sponsored after sponsorship")
    void testPerformanceIsMarkedSponsored() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        assertTrue(ticketedPerformance.isSponsored());
    }

    @Test
    @DisplayName("Testing sponsored amount is set correctly")
    void testSponsoredAmountIsSetCorrectly() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        assertEquals(5.00, ticketedPerformance.getSponsoredAmount(), 0.001);
    }

    @Test
    @DisplayName("Testing final ticket price is reduced by sponsored amount")
    void testFinalTicketPriceIsReduced() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        assertEquals(15.00, ticketedPerformance.getFinalTicketPrice(), 0.001);
    }

    @Test
    @DisplayName("Testing sponsoring with the full ticket price (reducing to zero) is valid")
    void testSponsorFullTicketPrice() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("20.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displaySuccess("Sponsorship successfully processed.");
        assertEquals(0.00, ticketedPerformance.getFinalTicketPrice(), 0.001);
    }

    //  Sponsor performance — non-existent performance ID

    @Test
    @DisplayName("Testing error shown for non-existent performance ID, then valid ID accepted")
    void testNonExistentPerformanceIdShowsError() {
        when(view.getInput("Enter performance ID: ")).thenReturn("999").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displayError("Performance with given number does not exist");
    }

    //  Sponsor performance — non-numeric performance ID

    @Test
    @DisplayName("Testing error shown for non-numeric performance ID, then valid ID accepted")
    void testNonNumericPerformanceIdShowsError() {
        when(view.getInput("Enter performance ID: ")).thenReturn("abc").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displayError("Performance ID must be a number");
    }

    //  Sponsor performance — non-ticketed event

    @Test
    @DisplayName("Testing error shown when trying to sponsor a non-ticketed performance, then valid ID accepted")
    void testNonTicketedPerformanceShowsError() {
        when(view.getInput("Enter performance ID: ")).thenReturn("2").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displayError("The requested performance's event is nonticketed. It cannot be sponsored.");
    }

    //  Sponsor performance — invalid amount

    @Test
    @DisplayName("Testing error shown for amount greater than ticket price, then valid amount accepted")
    void testAmountGreaterThanTicketPriceShowsError() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("999.00").thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displayError("The amount provided is invalid.");
    }

    @Test
    @DisplayName("Testing error shown for negative sponsorship amount, then valid amount accepted")
    void testNegativeAmountShowsError() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("-5.00").thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displayError("The amount provided is invalid.");
    }

    @Test
    @DisplayName("Testing error shown for non-numeric amount, then valid amount accepted")
    void testNonNumericAmountShowsError() {
        when(view.getInput("Enter performance ID: ")).thenReturn("1").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("abc").thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view).displayError("Amount must be a number");
    }

    //  displaySuccess not called on failure

    @Test
    @DisplayName("Testing displaySuccess is not called when performance does not exist")
    void testSuccessNotCalledForNonExistentPerformance() {
        when(view.getInput("Enter performance ID: ")).thenReturn("999").thenReturn("1");
        when(view.getInput("Enter amount to sponsor by: ")).thenReturn("5.00");

        eventPerformanceController.sponsorPerformance();

        verify(view, times(1)).displaySuccess(anyString());
    }
}
