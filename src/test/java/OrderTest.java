import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class OrderTest {
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
    @Description("Создание заказа с авторизацией")
    public void creatingOrderWithAuthorization() {
        createUser();
        Order order = Order.orderWithIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"});
        ValidatableResponse response=client.orderWithIngredients(order, accessToken);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Создание заказа без авторизации")
    public void creatingOrderWithoutAuthorization() {
        client=new StellarBurgersClientImple();
        Order order = Order.orderWithIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"});
        ValidatableResponse response=client.orderWithIngredients(order);
        response.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Создание заказа с авторизацией без ингредиентов")
    public void creatingOrderWithAuthorizationNoIngredients() {
        createUser();
        ValidatableResponse response=client.orderWithoutIngredients(accessToken);
        response.assertThat().statusCode(400).and().onFailMessage("Ingredient ids must be provided");
    }

    @Test
    @Description("Создание заказа без авторизации без ингредиентов")
    public void creatingOrderWithoutAuthorizationNoIngredients() {
        client=new StellarBurgersClientImple();
        ValidatableResponse response=client.orderWithoutIngredients();
        response.assertThat().statusCode(400).and().onFailMessage("Ingredient ids must be provided");
    }

    @Test
    @Description("Создание заказа с авторизацией с неверным хешем ингредиентов")
    public void creatingOrderWithAuthorizationIncorrectIngredientHash() {
        createUser();
        Order order = Order.orderWithIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61"});
        ValidatableResponse response=client.orderWithIngredients(order, accessToken);
        response.assertThat().statusCode(500).and().onFailMessage("Ingredient ids must be provided");
    }

    @Test
    @Description("Создание заказа без авторизации с неверным хешем ингредиентов")
    public void creatingOrderWithoutAuthorizationIncorrectIngredientHash() {
        client=new StellarBurgersClientImple();
        Order order = Order.orderWithIngredients(new String[]{"61","61c0c5a71d1f82001bdaaa6f"});
        ValidatableResponse response=client.orderWithIngredients(order);
        response.assertThat().statusCode(500).and().onFailMessage("Internal Server Error");
    }

}
