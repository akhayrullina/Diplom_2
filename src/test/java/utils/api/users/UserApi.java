package utils.api.users;

import io.qameta.allure.Step;
import io.restassured.response.Response;;
import io.restassured.specification.RequestSpecification;
import utils.pojo.User;
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
    public Response loginUser(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .post("/auth/login");
    }

    @Step("Выход пользователя из системы")
    public Response logoutUser(User user) {
        return given()
                .spec(spec)
                .body(user)
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
    public Response refreshToken(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .post("/auth/token");
    }

    @Step("Получение данных о пользователе")
    public Response getDataAboutUser(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .get("/auth/user");
    }

    @Step("Обновление данных о пользователе")
    public Response patchDataAboutUser(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .patch("/auth/user");
    }
}
