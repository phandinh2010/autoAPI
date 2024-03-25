package vinid.api.rest;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class RestResponse {

    private Response response;

    private RestResponse() {
    }

    public RestResponse(Response response) {
        this.response = response;
    }

    public ValidatableResponse validate() {

        return (ValidatableResponse) this.response.then();
    }

    public Response extract() {
        return this.response;
    }
}
