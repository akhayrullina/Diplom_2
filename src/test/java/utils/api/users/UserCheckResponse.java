package utils.api.users;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCheckResponse {

    @Step("Успешное создание пользователя возвращает код ответа 200 OK")
    public void createdUserReturn200(Response createResponse) {
        createResponse.then().assertThat().body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Создание пользователя, который уже зарегистрирован, возвращает код ответа 403 Forbidden")
    public void createdDuplicateUserReturn403(Response duplicateResponse) {
        duplicateResponse.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Step("Создание пользователя без заполнения одного из обязательных полей возвращает код ответа 403 Forbidden")
    public void createdUserWithoutRequiredFieldsReturn403(Response response) {
        response.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Step("Сохранение accessToken пользователя")
    public String saveUserAccessToken(Response createResponse) {
        return createResponse.jsonPath().getString("accessToken").substring(7);
    }

    @Step("Успешная авторизация пользователя возвращает код ответа 200 OK")
    public void loginUserReturn200(Response loginResponse) {
        loginResponse.then().assertThat().body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Авторизация с неверным или пустым логином или паролем возвращает код ответа 401 Unauthorized")
    public void loginUserReturn401(Response loginResponse) {
        loginResponse.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Step("Успешное обновление данных авторизованного пользователя возвращает код ответа 200 OK")
    public void updateUserDataReturn200(Response updateResponse) {
        updateResponse.then().assertThat().body("success", equalTo(true))
                .body("user", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Обновление данных неавторизованного пользователя возвращает код ответа 401 Unauthorized")
    public void updateUserDataReturn401(Response updateResponse) {
        updateResponse.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @Step("Обновление почты, которая уже используется, у авторизованного пользователя возвращает код ответа 403 Forbidden")
    public void updateUserEmailReturn403(Response updateResponse) {
        updateResponse.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"))
                .and()
                .statusCode(403);
    }
}
