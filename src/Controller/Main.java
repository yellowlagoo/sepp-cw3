package src.Controller;

import src.View.*;
import src.Model.*;
import src.Controller.*;
import src.ExternalSystems.MockPaymentSystem;
import src.ExternalSystems.MockVerificationSystem;

public class Main {
    private TextUserInterface view;
    private MockVerificationSystem verificationSystem;
    private MockPaymentSystem paymentSystem;
    private BookingController bookingController;
    private MenuController menuController;
    private UserController userController;
    private AdminStaff initUser;
    public static void main(String[] args) {
        //initializeComponents();
    }

    private void initializeComponents() {
        view = new TextUserInterface();
        verificationSystem = new MockVerificationSystem();
        paymentSystem = new MockPaymentSystem();
        initUser = new AdminStaff("maintainer@acme.com", "initial");
        initUser.setName("Maintainer");

    }
}