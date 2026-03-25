package utils.api.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCheckResponse {

    @Step("GET-запрос на получение данных об ингредиентах возвращает код ответа 200 OK и массив ингредиентов")
    public void getIngredientsInfoReturn200(Response getIngredientsInfo) {
        getIngredientsInfo.then().assertThat().body("success", equalTo(true))
                .body("data", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Получение и добавление ингредиентов в массив")
    public Map<String, List<String>> getIngredientsToList(OrderApi orderApi) {
        Map<String, List<String>> ingredientsMap = new HashMap<>();
        List<String> ingredientsList = orderApi.getIngredientsInfo()
                .path("data._id");
        ingredientsMap.put("ingredients", ingredientsList);
        return ingredientsMap;
    }

    @Step("Успешное создание заказа возвращает код ответа 200 OK")
    public void createdOrderReturn200(Response createResponse) {
        createResponse.then().assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Если не передать ни один ингредиент, север возвращает код ответа 500 Internal Server Error")
    public void createdOrderReturn500(Response createResponse) {
        createResponse.then().assertThat()
                .statusCode(500);
    }

    @Step("Если в запросе передан невалидный хэш ингредиента, сервер возвращает код ответа 400 Bad Request")
    public void createdOrderReturn400(Response createResponse) {
        createResponse.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Step("GET-запрос на получение заказов пользователя возвращает код ответа 200 OK и список максимум 50 последних заказов")
    public void getInfoAboutOrderFromUserReturn200(Response response) {
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Step("GET-запрос на получение заказов неавторизованного пользователя возвращает код ответа 401 Unauthorized")
    public void getInfoAboutOrderFromUnauthorizedUserReturn401(Response response) {
        response.then().assertThat().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
