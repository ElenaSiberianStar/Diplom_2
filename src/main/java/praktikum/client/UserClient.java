        package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseApiClient {




    @Step("Создание пользователя (регистрация)")
    public ValidatableResponse create(User user) {
        return given()

                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(User user) {
        return given()

                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(Endpoints.LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()

                .spec(getBaseSpec())
                .when()
                .delete(Endpoints.USER)
                .then();
    }
}
