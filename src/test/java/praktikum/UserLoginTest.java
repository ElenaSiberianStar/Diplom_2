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

public class UserLoginTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        // Перед тестами логина нам нужно сначала зарегистрировать пользователя
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Успешный вход под существующим пользователем")
    public void userCanLogin() {
        // Создаем объект с теми же учетными данными для логина (можно использовать только email и password)
        User credentials = new User(user.getEmail(), user.getPassword(), null);

        ValidatableResponse response = userClient.login(credentials);

        response.assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Нельзя войти с неверным паролем")
    public void cannotLoginWithWrongPassword() {
        // Указываем неверный пароль
        User wrongCredentials = new User(user.getEmail(), "wrong_password_123", null);

        ValidatableResponse response = userClient.login(wrongCredentials);

        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
