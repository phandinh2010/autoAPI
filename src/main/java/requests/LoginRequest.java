package requests;

import com.google.inject.Inject;
import model.RequestInfo;
import org.junit.Assert;
import vinid.api.rest.*;
import vinid.api.utils.Log;

import static requests.PathUrl.*;


public class LoginRequest extends BaseRequest{

    private Log XLog = new Log(LoginRequest.class);
    @Inject
    private RequestInfo requestInfo;
    private RestHeaders headers;
    private RestRequest request;
    private String authorization_code;

    private void setHeaders() {
        headers = new RestHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("User-Agent", "vinid.uat/123");
        headers.add("X-Device-ID", "one-shield-off");
        headers.add("X-Channel", "BrWZKvdEANNUPka");
    }
    public void verifyOTP(String phoneNumber, String OTP)  {

        RestRequest request = new RestRequest(BASE_URL, VERIFY_OTP, RestMethod.POST);
        setHeaders();
        request.setHeader(headers);
        RestBody body = new RestBody();
        phoneNumber = "+84" + phoneNumber.subSequence(1,10);
        body.add("Phone_number", phoneNumber);
        body.add("otp", OTP);
        request.setBody(body);
        RestResponse response = request.send();
        if (response.extract().statusCode() != 200) {
            String message = "Request OTP is failed with code " + response.extract().statusCode();
            String detailResponse = response.extract().getBody().prettyPrint();
            this.XLog.error(message);
            this.XLog.error(detailResponse);
        } else {
            this.authorization_code = response.extract().jsonPath().getString("data.authorization_code");
        }
    }
    public void getToken() {
        RestRequest request = new RestRequest(BASE_URL, GET_TOKEN, RestMethod.POST);
        setHeaders();
        request.setHeader(headers);
        RestBody body = new RestBody();
        body.add("code_verifier", "testtuoitest");
        body.add("authorization_code", this.authorization_code);
        request.setBody(body);
        RestResponse response = request.send();
        if (response.extract().statusCode() != 200) {
            String message = "Get token is failed with code " + response.extract().statusCode();
            String detailResponse = response.extract().getBody().prettyPrint();
            System.out.println(message);
            System.out.println(detailResponse);
        } else {
            this.requestInfo.setUserID(response.extract().path("data.user_id"));
            this.requestInfo.setAccessToken(response.extract().jsonPath().getString("data.access_token"));
        }
    }

    public void requestOTP(String phoneNumber) {
        request = new RestRequest(BASE_URL, REQUEST_OTP, RestMethod.POST);
        setHeaders();
        request.setHeader(headers);
        RestBody body = new RestBody();
        phoneNumber = "+84" + phoneNumber.subSequence(1,10);
        body.add("phone_number", phoneNumber);
        request.setBody(body);
        RestResponse response = request.send();
        XLog.info("********** Response **********");
        response.extract().body().prettyPrint();
        Assert.assertEquals(200, response.extract().statusCode());
    }
    public void getTokenSDK(String phone, String OTP) {
        this.requestOTP(phone);
        this.verifyOTP(phone, OTP);
        this.getToken();
    }
}
