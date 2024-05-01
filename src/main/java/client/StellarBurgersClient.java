package client;

import io.restassured.response.ValidatableResponse;

public interface StellarBurgersClient {
    ValidatableResponse createUser(User user);
    ValidatableResponse login(Credentials credentials);
    ValidatableResponse deleteUser(String accessToken);
    ValidatableResponse changingUserData(UserData userData, String accessToken);
    ValidatableResponse changingUserDataWithoutAuthorization(UserData userData);
    ValidatableResponse orderWithIngredients(Order order, String accessToken);
    ValidatableResponse orderWithIngredients(Order order);
    ValidatableResponse orderWithoutIngredients(String accessToken);
    ValidatableResponse orderWithoutIngredients();
    ValidatableResponse orderUser(String accessToken);
    ValidatableResponse orderUser();

}
