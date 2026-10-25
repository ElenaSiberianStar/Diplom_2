package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private final String BASE_URL = "https://qa-stellarburgers.education-services.ru";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post("/api/orders")
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post("/api/orders")
                .then();
    }

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .when()
                .get("/api/orders")
                .then();
    }
}
