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
}
