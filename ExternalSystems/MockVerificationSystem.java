package ExternalSystems;
public class MockVerificationSystem implements VerificationSystem{

    @Override
    public boolean verifyEntertainmentProvider(String businessRegistrationNumber) {
        return true;
    }

}