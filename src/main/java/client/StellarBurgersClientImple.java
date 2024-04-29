package client;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class StellarBurgersClientImple implements StellarBurgersClient {
    private static final String BASE_URI="https://stellarburgers.nomoreparties.site/";
    private static final String CREATE_USER_ENDPOINT="/api/auth/register";
    String accessToken;
@Override
public ValidatableResponse createUser(User user) {
    return given()
            .log()
            .all()
            .header("Content-Type", "application/json")
            .baseUri(BASE_URI)
            .body(user)
            .post(CREATE_USER_ENDPOINT)
            .then()
            .log()
            .all();
}

    @Override
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .log()
                .all()
                .header("Authorization", accessToken)
                .auth().oauth2(accessToken)
                .baseUri(BASE_URI)
                .post(CREATE_USER_ENDPOINT)
                .then()
                .log()
                .all();
    }
@Override
public ValidatableResponse deleteUser(Credentials credentials) {
    return given()
            .log()
            .all()
            .header("Authorization", accessToken)
            .auth().oauth2(accessToken)
            .baseUri(BASE_URI)
            .delete(CREATE_USER_ENDPOINT)
            .then()
            .log()
            .all();
}


}

