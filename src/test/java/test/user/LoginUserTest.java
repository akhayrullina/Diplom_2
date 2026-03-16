package test.user;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.api.BaseURL;
import utils.api.user.UserApi;
import utils.api.user.UserCheckResponse;
import utils.pojo.User;
import utils.pojo.UserCredentials;
import java.util.stream.Stream;

public class LoginUserTest extends BaseURL {

    private UserApi userApi;
    private UserCheckResponse checkResponse;
    private String accessToken;
    private UserCredentials userCredentials;
    private User user;

    private static Stream<Arguments> incorrectDataForLogin() {
        return Stream.of(
                Arguments.of(UserCredentials.random()),
                Arguments.of(new UserCredentials("", "testtest")),
                Arguments.of(new UserCredentials("test456@mail.ru", ""))
        );
    }

    @BeforeEach
    public void init() {
        userApi = new UserApi(setUp());
        checkResponse = new UserCheckResponse();
        user = user.random();
        Response createResponse = userApi.createUser(user);
        accessToken = checkResponse.saveUserAccessToken(createResponse);
    }

    @AfterEach
    public void deleteUsers() {
        if(accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешная авторизация под существующим пользователем")
    public void loginUserSuccess() {
        userCredentials = new UserCredentials(user);
        Response loginResponse = userApi.loginUser(userCredentials);
        checkResponse.loginUserReturn200(loginResponse);
    }

    @ParameterizedTest
    @MethodSource("incorrectDataForLogin")
    @DisplayName("Авторизация с неверным логином и паролем")
    public void loginNonExistentUserError(UserCredentials userCredentials) {
        Response loginResponse = userApi.loginUser(userCredentials);
        checkResponse.loginUserReturn401(loginResponse);
    }
}
