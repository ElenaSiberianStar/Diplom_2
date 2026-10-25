package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.generator.UserGenerator;
import praktikum.model.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserRegisterTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Успешная регистрация уникального пользователя")
    public void userCanBeRegistered() {
        ValidatableResponse response = userClient.create(user);

        response.assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Нельзя зарегистрировать двух одинаковых пользователей")
    public void cannotRegisterSameUserTwice() {
        ValidatableResponse firstResponse = userClient.create(user);
        accessToken = firstResponse.extract().path("accessToken");

        ValidatableResponse secondResponse = userClient.create(user);

        secondResponse.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Нельзя зарегистрировать пользователя без email")
    public void cannotRegisterUserWithoutEmail() {
        user.setEmail(null);

        ValidatableResponse response = userClient.create(user);

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}