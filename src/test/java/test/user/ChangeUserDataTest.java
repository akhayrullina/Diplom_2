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
import java.util.stream.Stream;

public class ChangeUserDataTest extends BaseURL {
    private UserApi userApi;
    private UserCheckResponse checkResponse;
    private String accessToken;
    private static User user = User.random();

    private static Stream<Arguments> newDataForUpdateUser() {
        return Stream.of(
                Arguments.of(new User(null, "test000test", null)),
                Arguments.of(new User("test000@mail.ru", null, null)),
                Arguments.of(new User(null, null, "testUser0000")),
                Arguments.of(User.random())
        );
    }

    @BeforeEach
    public void init() {
        userApi = new UserApi(setUp());
        checkResponse = new UserCheckResponse();
        user = User.random();
        Response createResponse = userApi.createUser(user);
        accessToken = checkResponse.saveUserAccessToken(createResponse);
    }

    @AfterEach
    public void deleteUsers() {
        if(accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }

    @ParameterizedTest
    @MethodSource("newDataForUpdateUser")
    @DisplayName("Успешное обновление данных авторизованного пользователя")
    public void updateUserDataSuccess(User user) {
        Response updateResponse = userApi.patchDataAboutUser(accessToken, user);
        checkResponse.updateUserDataReturn200(updateResponse);
    }

    @Test
    @DisplayName("Обновление почты, которая уже используется, у авторизованного пользователя")
    public void updateUserEmailError() {
        Response updateResponse = userApi.patchDataAboutUser(accessToken, user);
        checkResponse.updateUserEmailReturn403(updateResponse);
    }

    @ParameterizedTest
    @MethodSource("newDataForUpdateUser")
    @DisplayName("Обновление данных неавторизованного пользователя")
    public void updateUserDataError(User user) {
        Response updateResponse = userApi.patchDataAboutUser("", user);
        checkResponse.updateUserDataReturn401(updateResponse);
    }

}
