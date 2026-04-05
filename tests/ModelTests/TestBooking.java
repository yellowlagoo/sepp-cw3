package ModelTests;

import src.Model.*; 
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import Model.Booking;
import Model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class TestBooking {
    private Booking booking;

    @BeforeEach
    void setup() {
        LocalDateTime bookingTime = LocalDateTime.of(2026, 4, 1, 16, 30);
        Student student = new Student("student@test.com", "password123");
        booking = new Booking(0, 2, 12.50, bookingTime, BookingStatus.ACTIVE, student, null);
    }

    //cancelByStudent tests
    @Test
    @DisplayName("Tests to see if cancel by student correctly alters a booking's status")
    void testCancelByStudent(){
        booking.cancelByStudent();
    }
    
    // Cancel payment failed tests

    // Cancel by provider tests

    // Check booked by student tests

    // Generate booking record tests
}
