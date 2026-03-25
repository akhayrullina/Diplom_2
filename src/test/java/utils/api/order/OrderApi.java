package utils.api.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class OrderApi {
    private final RequestSpecification spec;

    public OrderApi(RequestSpecification spec) {
        this.spec = spec;
    }

    @Step("Получение данных об ингредиентах")
    public Response getIngredientsInfo() {
        return given()
                .spec(spec)
                .when()
                .get("/ingredients");
    }

    @Step("Создание заказа  с авторизацией")
    public Response createOrderWithAuthorization(String accessToken, Map<String, List<String>> ingredients) {
        return given()
                .spec(spec)
                .auth().oauth2(accessToken)
                .body(ingredients)
                .when()
                .post("/orders");
    }

    @Step("Создание заказа  без авторизацией")
    public Response createOrderWithoutAuthorization(Map<String, List<String>> ingredients) {
        return given()
                .spec(spec)
                .body(ingredients)
                .when()
                .post("/orders");
    }

    @Step("Получение информации обо всех заказах")
    public Response getInfoAboutAllOrders() {
        return given()
                .spec(spec)
                .when()
                .get("/orders/all");
    }

    @Step("Получение информации о заказе конкретного пользователя")
    public Response getInfoAboutOrderFromSpecificUser(String accessToken) {
        return given()
                .spec(spec)
                .auth().oauth2(accessToken)
                .when()
                .get("/orders");
    }
}
