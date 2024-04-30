import client.Order;
import client.StellarBurgersClient;
import client.StellarBurgersClientImple;
import client.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class OrderUserTest {
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

    @Step("Создание заказа с авторизацией")
    public void creatingOrderWithAuthorization() {
        Order order = Order.orderWithIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"});
        ValidatableResponse response=client.orderWithIngredients(order, accessToken);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Получение заказов авторизованного пользователя")
    public void ordersAuthorizedUser() {
        createUser();
        creatingOrderWithAuthorization();
        ValidatableResponse response=client.orderUser(accessToken);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Получение заказов НЕ авторизованного пользователя")
    public void ordersNotAuthorizedUser() {
        client=new StellarBurgersClientImple();
        ValidatableResponse response=client.orderUser();
        response.assertThat().statusCode(401).and().onFailMessage("You should be authorised");
    }

}
