package src.Controller;

import src.View.*;
import src.Model.*;

import java.util.ArrayList;
import java.util.Collection;

import src.external.MockPaymentSystem;
import src.external.MockVerificationService;

public class Main {
        public static void main(String[] args) {
                System.out.println("Working directory: " + System.getProperty("user.dir"));
                TextUserInterface view = new TextUserInterface();
                MockVerificationService verificationService = new MockVerificationService();
                MockPaymentSystem paymentSystem = new MockPaymentSystem();

                UserController userController = new UserController(view, verificationService);
                Collection<Performance> sharedPerformances = new ArrayList<>();

                EventPerformanceController epController = new EventPerformanceController(
                                1, 1, paymentSystem, view, sharedPerformances);

                BookingController bookingController = new BookingController(
                                paymentSystem, view, sharedPerformances);

                MenuController menuController = new MenuController(
                                userController, bookingController, epController, view);

                menuController.mainMenu();
        }
}