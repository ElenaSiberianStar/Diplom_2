package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.generator.UserGenerator;
import praktikum.model.Order;
import praktikum.model.User;
import static io.restassured.RestAssured.given;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private String accessToken;
    // Объявляем список ингридиентов
    private List<String> validIngredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandom();
        // Регистрируем пользователя, чтобы получать токен для авторизованных запросов
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        // Динамически запрашиваем актуальные ID ингредиентов
        validIngredients = given()
                .baseUri("https://qa-stellarburgers.education-services.ru")
                .get("/api/ingredients")
                .then()
                .extract().path("data._id");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    public void createOrderWithAuthAndIngredients() {
        Order order = new Order(validIngredients);

        ValidatableResponse response = orderClient.createOrder(order, accessToken);

        response.assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());


    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        Order order = new Order(validIngredients);

        ValidatableResponse response = orderClient.createOrderWithoutAuth(order);

        // Согласно требованиям, неавторизованный пользователь не может сделать заказ
        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов возвращает ошибку")
    public void createOrderWithoutIngredients() {
        Order order = new Order(List.of()); // Пустой список ингредиентов

        ValidatableResponse response = orderClient.createOrder(order, accessToken);

        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов возвращает ошибку сервера")
    public void createOrderWithInvalidIngredientHash() {
        Order order = new Order(List.of("invalid_hash_12345"));

        ValidatableResponse response = orderClient.createOrder(order, accessToken);

        // При невалидном хеше сервер возвращает 500 Internal Server Error
        response.assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
