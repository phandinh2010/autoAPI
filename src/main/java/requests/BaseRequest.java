package requests;

import com.api.rest.RestBody;
import com.api.rest.RestHeaders;

public class BaseRequest {


    public RestHeaders setHeadersWithToken(RestHeaders headers, String token) {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        headers.add("User-Agent", "UAT_VinID/123");
        headers.add("X-Channel", "BrWZKvdEANNUPka");
        headers.add("X-Device-ID", "one-shield-off");
        return headers;
    }
    public RestHeaders setHeadersWithFullParam(RestHeaders headers, String token) {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        headers.add("X-Device-OS", "ios");
        headers.add("X-Device-Session-ID", "one-shield-off");
        headers.add("X-Device-Version", "149.0");
        headers.add("Device-ID", "one-shield-off");
        headers.add("X-Device-UUID", "one-shield-off");
        headers.add("X-Device-Request-ID", "one-shield-off");
        headers.add("X-Device-ID", "one-shield-off");
        headers.add("User-Agent", "UAT_VinID/123");
        headers.add("token_level", "passwordless");

        return headers;
    }

    public RestHeaders setHeaders(RestHeaders headers, String token) {
        headers = new RestHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        headers.add("X-Device-ID", "one-shield-off");
        return headers;
    }

    public RestHeaders setHeadersWithUserAgent(RestHeaders headers, String token) {
        headers.add("Content-Type", "application/json");
        headers.add("User-Agent", token);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

    public RestHeaders setHeadersBasicAuthen(RestHeaders headers, String authen) {
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic " + authen);
        return headers;
    }

    public RestBody setBody(String requestBody) {
        return new RestBody(requestBody);
    }
}
