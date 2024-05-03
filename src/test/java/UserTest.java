import client.StellarBurgersClient;
import client.StellarBurgersClientImple;
import client.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserTest {

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
    @Description("Проверка - нельзя создать двух одинаковых пользователей")
    public void createTwoUsers() {
        createUser();
        User user=User.create("Puskin1@yandex.ru", "1234", "Puskin1");
        ValidatableResponse response=client.createUser(user);
        response.assertThat().statusCode(403).and().onFailMessage("User already exists");
    }

    @Test
    @Description("Проверка - нельзя создать пользователя без email")
    public void createWithoutEmail() {
        client=new StellarBurgersClientImple();
        User user=User.create("", "1234", "Puskin1");
        ValidatableResponse response=client.createUser(user);
        response.assertThat().statusCode(403).and().onFailMessage("Email, password and name are required fields");
    }

    @Test
    @Description("Проверка - нельзя создать пользователя без password")
    public void createWithoutPassword() {
        client=new StellarBurgersClientImple();
        User user=User.create("Puskin1@yandex.ru", "", "Puskin1");
        ValidatableResponse response=client.createUser(user);
        response.assertThat().statusCode(403).and().onFailMessage("Email, password and name are required fields");
    }

    @Test
    @Description("Проверка - нельзя создать пользователя без name")
    public void createWithoutName() {
        client=new StellarBurgersClientImple();
        User user=User.create("Puskin1@yandex.ru", "1234", "");
        ValidatableResponse response=client.createUser(user);
        response.assertThat().statusCode(403).and().onFailMessage("Email, password and name are required fields");
    }



}
