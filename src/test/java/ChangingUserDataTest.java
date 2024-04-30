import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangingUserDataTest {
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
    @Description("Изменение Email пользователя")
    public void changingEmailData() {
        createUser();
        UserData userData = UserData.changingData("P", "Puskin1");
        ValidatableResponse response=client.changingUserData(userData, accessToken);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Изменение Name пользователя")
    public void changingNameData() {
        createUser();
        UserData userData = UserData.changingData("Puskin1@yandex.ru", "P");
        ValidatableResponse response=client.changingUserData(userData, accessToken);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Изменение Email и Name пользователя")
    public void changingEmailAndNameData() {
        createUser();
        UserData userData = UserData.changingData("P", "P");
        ValidatableResponse response=client.changingUserData(userData, accessToken);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Изменение данных пользователя без авторизации")
    public void changingDataWithoutAuthorization() {
        createUser();
        UserData userData = UserData.changingData("P", "Puskin1");
        ValidatableResponse response=client.changingUserDataWithoutAuthorization(userData);
        response.assertThat().statusCode(401).and().onFailMessage("You should be authorised");
    }

}
