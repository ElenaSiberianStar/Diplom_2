package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient {



    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)

                .body(order)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getBaseSpec())

                .body(order)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)

                .when()
                .get(Endpoints.ORDERS)
                .then();
    }
    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(Endpoints.INGREDIENTS)
                .then();
    }
}
