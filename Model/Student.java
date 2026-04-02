package model; 

import java.util.ArrayList;
import java.util.Collection;

public class Student extends User {
    private String name;
    private int phoneNumber;
    private Collection<Booking> bookings;

    /**
     * Constructor for the student class
     * @param email - the student's email
     * @param password - the student's password
     * @param name - the student's name
     * @param phonenumber - the student's phone number
     */
    public Student(String email, String password, String name, int phoneNumber) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bookings = new ArrayList<>();
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
