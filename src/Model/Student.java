package src.Model; 

import java.util.ArrayList;
import java.util.Collection;

public class Student extends User {
    private String name;
    private int phoneNumber;
    private Collection<Booking> bookings;
    private StudentPreferences preferences;

    /**
     * Constructor for the student class
     * @param email - the student's email
     * @param password - the student's password
     * @param name - the student's name
     * @param phoneNumber - the student's phone number
     */
    public Student(String email, String password) {
        super(email, password);
        this.name = "";
        this.phoneNumber = -1;
        this.bookings = new ArrayList<>();
        this.preferences = new StudentPreferences();
    }
    public StudentPreferences getPreferences() {
        return this.preferences;
    }
    public void setPreferences(String rawInput) {
        preferences.updatePreferences(rawInput);
    }
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Adds a booking to the list of bookings associated with the student
     * @param booking - the booking to be added
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}
