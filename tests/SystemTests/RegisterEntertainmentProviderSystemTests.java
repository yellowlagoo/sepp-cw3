package tests.SystemTests;

import src.Controller.*;
import src.Model.*;
import src.View.*;
import src.ExternalSystems.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


public class RegisterEntertainmentProviderSystemTests {

    private UserController userController;
    private TextUserInterface view;
    private MockVerificationSystem verificationSystem;

    @BeforeEach
    void setup() {
        view = new TextUserInterface();
        verificationSystem = new MockVerificationSystem();
        userController = new UserController(view, verificationSystem);
    }

    // --- Tests for EP account creation via model ---

    @Test
    @DisplayName("Entertainment provider is created with correct email")
    void testEPCreatedWithCorrectEmail() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertEquals("ep@test.com", ep.getEmail(), "EP email should match");
    }

    @Test
    @DisplayName("Entertainment provider is created with correct organisation name")
    void testEPCreatedWithCorrectOrgName() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertEquals("Test Org", ep.getOrgName(), "Organisation name should match");
    }

    @Test
    @DisplayName("Entertainment provider is created with correct business number")
    void testEPCreatedWithCorrectBusinessNumber() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertEquals("B123", ep.getBusinessNumber(), "Business number should match");
    }

    @Test
    @DisplayName("Entertainment provider is created with correct contact name")
    void testEPCreatedWithCorrectContactName() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertEquals("Contact Person", ep.getName(), "Contact name should match");
    }

    @Test
    @DisplayName("Entertainment provider is created with correct description")
    void testEPCreatedWithCorrectDescription() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertEquals("A test organisation", ep.getDescription(), "Description should match");
    }

    @Test
    @DisplayName("Entertainment provider starts with no events")
    void testEPStartsWithNoEvents() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertTrue(ep.getEvents().isEmpty(), "EP should have no events initially");
    }

    @Test
    @DisplayName("Entertainment provider is not logged in by default")
    void testEPNotLoggedInByDefault() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        assertFalse(ep.isLoggedIn(), "EP should not be logged in by default");
    }

    // --- Tests for verification system ---

    @Test
    @DisplayName("Mock verification system verifies valid business number")
    void testVerificationSystemVerifiesValidNumber() {
        assertTrue(verificationSystem.verifyEntertainmentProvider("B123"),
                "Mock verification should return true for any business number");
    }

    @Test
    @DisplayName("Mock verification system verifies any business number")
    void testVerificationSystemVerifiesAnyNumber() {
        assertTrue(verificationSystem.verifyEntertainmentProvider("ANYTHING"),
                "Mock verification should return true for any input");
    }

    // --- Tests for duplicate checking ---

    @Test
    @DisplayName("No users exist initially in the system")
    void testNoUsersInitially() {
        assertTrue(userController.getUsers().isEmpty(), "No users should exist initially");
    }

    @Test
    @DisplayName("EP added to user collection is retrievable")
    void testEPAddedToUserCollection() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact Person", "A test organisation");
        userController.getUsers().add(ep);
        assertEquals(1, userController.getUsers().size(), "Should have 1 user");
    }

    @Test
    @DisplayName("Duplicate EP can be detected by email, org name, and business number")
    void testDuplicateEPDetection() {
        EntertainmentProvider ep1 = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact 1", "Description 1");
        EntertainmentProvider ep2 = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact 2", "Description 2");
        userController.getUsers().add(ep1);

        boolean duplicateFound = false;
        for (User user : userController.getUsers()) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider existing = (EntertainmentProvider) user;
                if (existing.getEmail().equals(ep2.getEmail())
                        && existing.getOrgName().equals(ep2.getOrgName())
                        && existing.getBusinessNumber().equals(ep2.getBusinessNumber())) {
                    duplicateFound = true;
                }
            }
        }
        assertTrue(duplicateFound, "Duplicate EP should be detected");
    }

    @Test
    @DisplayName("Different email is not detected as duplicate")
    void testDifferentEmailNotDuplicate() {
        EntertainmentProvider ep1 = new EntertainmentProvider("ep1@test.com", "password",
                "Test Org", "B123", "Contact 1", "Description 1");
        EntertainmentProvider ep2 = new EntertainmentProvider("ep2@test.com", "password",
                "Test Org", "B123", "Contact 2", "Description 2");
        userController.getUsers().add(ep1);

        boolean duplicateFound = false;
        for (User user : userController.getUsers()) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider existing = (EntertainmentProvider) user;
                if (existing.getEmail().equals(ep2.getEmail())
                        && existing.getOrgName().equals(ep2.getOrgName())
                        && existing.getBusinessNumber().equals(ep2.getBusinessNumber())) {
                    duplicateFound = true;
                }
            }
        }
        assertFalse(duplicateFound, "Different email should not be detected as duplicate");
    }

    @Test
    @DisplayName("Different org name is not detected as duplicate")
    void testDifferentOrgNameNotDuplicate() {
        EntertainmentProvider ep1 = new EntertainmentProvider("ep@test.com", "password",
                "Test Org 1", "B123", "Contact 1", "Description 1");
        EntertainmentProvider ep2 = new EntertainmentProvider("ep@test.com", "password",
                "Test Org 2", "B123", "Contact 2", "Description 2");
        userController.getUsers().add(ep1);

        boolean duplicateFound = false;
        for (User user : userController.getUsers()) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider existing = (EntertainmentProvider) user;
                if (existing.getEmail().equals(ep2.getEmail())
                        && existing.getOrgName().equals(ep2.getOrgName())
                        && existing.getBusinessNumber().equals(ep2.getBusinessNumber())) {
                    duplicateFound = true;
                }
            }
        }
        assertFalse(duplicateFound, "Different org name should not be detected as duplicate");
    }

    @Test
    @DisplayName("Different business number is not detected as duplicate")
    void testDifferentBusinessNumberNotDuplicate() {
        EntertainmentProvider ep1 = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact 1", "Description 1");
        EntertainmentProvider ep2 = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B456", "Contact 2", "Description 2");
        userController.getUsers().add(ep1);

        boolean duplicateFound = false;
        for (User user : userController.getUsers()) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider existing = (EntertainmentProvider) user;
                if (existing.getEmail().equals(ep2.getEmail())
                        && existing.getOrgName().equals(ep2.getOrgName())
                        && existing.getBusinessNumber().equals(ep2.getBusinessNumber())) {
                    duplicateFound = true;
                }
            }
        }
        assertFalse(duplicateFound, "Different business number should not be detected as duplicate");
    }

    // --- Tests for multiple EP registrations ---

    @Test
    @DisplayName("Multiple different EPs can be registered")
    void testMultipleEPsRegistered() {
        EntertainmentProvider ep1 = new EntertainmentProvider("ep1@test.com", "pass1",
                "Org 1", "B001", "Contact 1", "Desc 1");
        EntertainmentProvider ep2 = new EntertainmentProvider("ep2@test.com", "pass2",
                "Org 2", "B002", "Contact 2", "Desc 2");
        EntertainmentProvider ep3 = new EntertainmentProvider("ep3@test.com", "pass3",
                "Org 3", "B003", "Contact 3", "Desc 3");
        userController.getUsers().add(ep1);
        userController.getUsers().add(ep2);
        userController.getUsers().add(ep3);
        assertEquals(3, userController.getUsers().size(), "Should have 3 registered EPs");
    }

    @Test
    @DisplayName("Registered EP is instance of EntertainmentProvider")
    void testRegisteredEPIsCorrectType() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact", "Description");
        userController.getUsers().add(ep);

        User retrieved = userController.getUsers().iterator().next();
        assertTrue(retrieved instanceof EntertainmentProvider,
                "Registered user should be an EntertainmentProvider");
    }

    @Test
    @DisplayName("Registered EP can be logged in")
    void testRegisteredEPCanLogIn() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact", "Description");
        userController.getUsers().add(ep);
        ep.setLoggedIn(true);
        assertTrue(ep.isLoggedIn(), "EP should be able to log in after registration");
    }

    @Test
    @DisplayName("Registered EP can create events after logging in")
    void testRegisteredEPCanCreateEvents() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact", "Description");
        ep.setLoggedIn(true);
        Event event = new Event(ep, 1, "Test Event", EventType.MUSIC, true);
        ep.addEvent(event);
        assertEquals(1, ep.getEvents().size(), "Logged in EP should be able to create events");
    }

    // --- Tests for EP fields ---

    @Test
    @DisplayName("EP password is stored correctly")
    void testEPPasswordStored() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "securePass123",
                "Test Org", "B123", "Contact", "Description");
        assertEquals("securePass123", ep.getPassword(), "Password should be stored correctly");
    }

    @Test
    @DisplayName("EP fields can be updated after creation")
    void testEPFieldsUpdatable() {
        EntertainmentProvider ep = new EntertainmentProvider("ep@test.com", "password",
                "Test Org", "B123", "Contact", "Description");
        ep.setOrgName("Updated Org");
        ep.setDescription("Updated description");
        assertEquals("Updated Org", ep.getOrgName(), "Org name should be updated");
        assertEquals("Updated description", ep.getDescription(), "Description should be updated");
    }
}