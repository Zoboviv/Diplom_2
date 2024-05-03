import client.Credentials;
import client.StellarBurgersClient;
import client.StellarBurgersClientImple;
import client.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    private StellarBurgersClient client;
    private String accessToken;

    @After
    @Step("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse deleteUserResponse=client.deleteUser(accessToken);
            deleteUserResponse.assertThat().statusCode(202).and().body("success", equalTo(true));
        }
    }

    @Step("Создание пользователя")
    public void createUser() {
        client=new StellarBurgersClientImple();
        User user=User.create("Puskin1@yandex.ru", "1234", "Puskin1");
        ValidatableResponse response=client.createUser(user);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
        accessToken=response.extract().jsonPath().getString("accessToken").substring(7);
    }

    @Test
    @Description("Логин под существующим пользователем")
    public void login() {
        createUser();
        Credentials credentials = Credentials.authorization("Puskin1@yandex.ru", "1234");
        ValidatableResponse response=client.login(credentials);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
        accessToken=response.extract().jsonPath().getString("accessToken").substring(7);
    }

    @Test
    @Description("Логин с неверным Email")
    public void loginWrongEmail() {
        createUser();
        Credentials credentials = Credentials.authorization("Puskin1", "1234");
        ValidatableResponse response=client.login(credentials);
        response.assertThat().statusCode(401).and().onFailMessage("email or password are incorrect");
    }

    @Test
    @Description("Логин с неверным паролем")
    public void loginWrongPassword() {
        createUser();
        Credentials credentials = Credentials.authorization("Puskin1@yandex.ru", "12341234");
        ValidatableResponse response=client.login(credentials);
        response.assertThat().statusCode(401).and().onFailMessage("email or password are incorrect");
    }

}
