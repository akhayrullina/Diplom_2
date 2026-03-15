package test;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.api.BaseURL;
import utils.api.users.UserApi;
import utils.api.users.UserCheckResponse;
import utils.pojo.User;
import java.util.stream.Stream;

public class CreateUserTest extends BaseURL {
    private UserApi userApi;
    private UserCheckResponse checkResponse;
    private String accessToken;
    private User user;

    private static Stream<Arguments> incorrectDataForCreate() {
        return Stream.of(
                Arguments.of(new User("", "testtest", "test1")),
                Arguments.of(new User("test123@mail.ru", "testtest", "")),
                Arguments.of(new User("test456@mail.ru", "", "testUser"))
        );
    }

    @BeforeEach
    public void init() {
        userApi = new UserApi(setUp());
        checkResponse = new UserCheckResponse();
    }

    @AfterEach
    public void deleteUsers() {
        if(accessToken != null) {
          userApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    public void createUniqueUser() {
        user = user.random();
        Response createResponse = userApi.createUser(user);
        accessToken = checkResponse.saveUserAccessToken(createResponse);
        checkResponse.createdUserReturn200(createResponse);
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {
        user = user.random();
        Response createResponse = userApi.createUser(user);
        accessToken = checkResponse.saveUserAccessToken(createResponse);

        Response duplicateResponse = userApi.createUser(user);
        checkResponse.createdDuplicateUserReturn403(duplicateResponse);
    }

    @ParameterizedTest
    @MethodSource("incorrectDataForCreate")
    @DisplayName("Проверка создания пользователя, если не заполнить одно из обязательных полей")
    public void createUserWithoutRequiredFields(User user) {
        Response createResponse = userApi.createUser(user);
        checkResponse.createdUserWithoutRequiredFieldsReturn403(createResponse);
    }
}
