package vinid.api.rest.auth;

import io.restassured.authentication.AuthenticationScheme;

public interface RestAuth {
    AuthenticationScheme getAuth();
}
