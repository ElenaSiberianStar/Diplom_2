        package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    // Базовый URL для всех запросов Stellar Burgers
    private final String BASE_URL = "https://qa-stellarburgers.education-services.ru";

    @Step("Создание пользователя (регистрация)")
    public ValidatableResponse create(User user) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user)
                .when()
                .post("/api/auth/register")
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user)
                .when()
                .post("/api/auth/login")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .when()
                .delete("/api/auth/user")
                .then();
    }
}
