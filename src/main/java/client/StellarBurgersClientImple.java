package client;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class StellarBurgersClientImple implements StellarBurgersClient {
    private static final String BASE_URI="https://stellarburgers.nomoreparties.site/";
    private static final String REGISTER_ENDPOINT="/api/auth/register";
    private static final String LOGIN_ENDPOINT="/api/auth/login";
    private static final String USER_ENDPOINT="/api/auth/user";


    @Override
    public ValidatableResponse createUser(User user) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URI)
                .body(user)
                .post(REGISTER_ENDPOINT)
                .then()
                .log()
                .all();
    }

    @Override
    public ValidatableResponse login(Credentials credentials, String accessToken) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .auth().oauth2(accessToken)
                .baseUri(BASE_URI)
                .post(LOGIN_ENDPOINT)
                .then()
                .log()
                .all();
    }

    @Override
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .auth().oauth2(accessToken)
                .baseUri(BASE_URI)
                .delete(USER_ENDPOINT)
                .then()
                .log()
                .all();
    }

}

