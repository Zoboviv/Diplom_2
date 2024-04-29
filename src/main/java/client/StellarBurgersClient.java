package client;

import io.restassured.response.ValidatableResponse;

public interface StellarBurgersClient {
    ValidatableResponse createUser(User user);
    ValidatableResponse login(Credentials credentials, String accessToken);
    ValidatableResponse deleteUser(String accessToken);

}
