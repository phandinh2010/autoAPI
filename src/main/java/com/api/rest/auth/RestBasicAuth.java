package vinid.api.rest.auth;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.BasicAuthScheme;

public class RestBasicAuth  implements RestAuth{

    BasicAuthScheme basicAuthScheme = new BasicAuthScheme();

    public RestBasicAuth() {
    }

    public void setUserName(String userName) {
        this.basicAuthScheme.setUserName(userName);
    }

    public void setPassword(String password) {
        this.basicAuthScheme.setPassword(password);
    }

    public AuthenticationScheme getAuth() {
        return this.basicAuthScheme;
    }

}
