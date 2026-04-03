package model; 

import java.util.ArrayList;
import java.util.Collection;

public class EntertainmentProvider extends User {

    private String orgName;
    private String businessNumber;
    private String name;
    private String description;
    private Collection<Event> events;

    /**
     * Constructor for the EntertainmentProvider class
     * @param email - the entertainment provider's email
     * @param password - the entertainment provider's password
     * @param orgName - the name of the entertainment provider's business
     * @param businessNumber - the official business number of the organization
     * @param name - the name of the main contact for the business
     * @param description - a description of the business
     */
    public EntertainmentProvider(String email, String password, String orgName, String businessNumber, String name,
            String description) {
        super(email, password);
        this.orgName = orgName;
        this.businessNumber = businessNumber;
        this.name = name;
        this.description = description;
        this.events = new ArrayList<>();

    }

    //Getters and setters
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addEvent(Event event) {
        events.add(event);
    }
}
