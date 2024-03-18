package vinid.api;

import com.google.inject.Inject;
import io.cucumber.java.en.Given;
import model.RequestInfo;
import requests.LoginRequest;
import io.cucumber.guice.ScenarioScoped;
@ScenarioScoped
public class LoginSteps {
    @Inject
    private LoginRequest loginRequest;
    @Inject
    private RequestInfo requestInfo;

    @Given("^I login to app with phone number (.*) and  otp (.*)$")
    public void loginApp(String phoneNumber, String OTP) {
//        loginRequest.getTokenSDK(phoneNumber, OTP);
//        System.out.println("Access token: " + requestInfo.getAccessToken());
    }
}
