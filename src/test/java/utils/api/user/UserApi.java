package utils.api.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;;
import io.restassured.specification.RequestSpecification;
import utils.pojo.User;
import utils.pojo.UserCredentials;
import static io.restassured.RestAssured.given;

public class UserApi {
    private final RequestSpecification spec;

    public UserApi(RequestSpecification spec) {
        this.spec = spec;
    }

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .post("/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(UserCredentials userCredentials) {
        return given()
                .spec(spec)
                .body(userCredentials)
                .when()
                .post("/auth/login");
    }

    @Step("Выход пользователя из системы")
    public Response logoutUser(String refreshToken) {
        String json = "{\"token\": " + refreshToken + "}";
        return given()
                .spec(spec)
                .body(json)
                .when()
                .post("/auth/logout");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(spec)
                .auth().oauth2(accessToken)
                .delete("/auth/user");
    }

    @Step("Обновление токена пользователя")
    public Response refreshToken(UserCredentials userCredentials) {
        return given()
                .spec(spec)
                .body(userCredentials)
                .when()
                .post("/auth/token");
    }

    @Step("Получение данных о пользователе")
    public Response getDataAboutUser(String accessToken) {
        return given()
                .spec(spec)
                .auth().oauth2(accessToken)
                .when()
                .get("/auth/user");
    }

    @Step("Обновление данных о пользователе")
    public Response patchDataAboutUser(String accessToken, User user) {
        return given()
                .spec(spec)
                .auth().oauth2(accessToken)
                .body(user)
                .when()
                .patch("/auth/user");
    }
}
