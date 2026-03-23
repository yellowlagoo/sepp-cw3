import java.util.ArrayList;
import java.util.Collection;

public class Student extends User {

    // Attributes
    private String name;
    private int phoneNumber;
    private Collection<Booking> bookings;

   
    // Constructor
    public Student(String email, String password, String name, String phonenumber) {

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

    public void setPhoneNumber() {
        this.phoneNumber = phoneNumber;
    }

    // Method
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

}
