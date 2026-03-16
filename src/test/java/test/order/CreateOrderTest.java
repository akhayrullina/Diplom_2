package test.order;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.api.BaseURL;
import utils.api.order.OrderApi;
import utils.api.order.OrderCheckResponse;
import utils.api.user.UserApi;
import utils.api.user.UserCheckResponse;
import utils.pojo.User;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CreateOrderTest extends BaseURL {
    private OrderApi orderApi;
    private OrderCheckResponse orderCheckResponse;
    private Map<String, List<String>> ingredients;

    private UserApi userApi;
    private UserCheckResponse userCheckResponse;
    private User user;
    private String accessToken;

    @BeforeEach
    public void init() {
        orderApi = new OrderApi(setUp());
        orderCheckResponse = new OrderCheckResponse();
        ingredients = orderCheckResponse.getIngredientsToList(orderApi);

        userApi = new UserApi(setUp());
        userCheckResponse = new UserCheckResponse();
        user = User.random();
        Response createResponse = userApi.createUser(user);
        accessToken = userCheckResponse.saveUserAccessToken(createResponse);
    }

    @AfterEach
    public void deleteUsers() {
        if(accessToken != null) {
            userApi.deleteUser(accessToken);
        }
        ingredients.clear();
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией и со списком ингредиентов")
    public void createOrderWithAuthorizationAndIngredients() {
        Response createResponse = orderApi.createOrderWithAuthorization(accessToken, ingredients);
        orderCheckResponse.createdOrderReturn200(createResponse);
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации и со списком ингредиентов")
    public void createOrderWithIngredientsAndWithoutAuthorization() {
        Response createResponse = orderApi.createOrderWithoutAuthorization(ingredients);
        orderCheckResponse.createdOrderReturn200(createResponse);
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации и без списка ингредиентов")
    public void createOrderWithoutAuthorizationAndIngredients() {
        ingredients.get("ingredients").clear();
        Response createResponse = orderApi.createOrderWithoutAuthorization(ingredients);
        orderCheckResponse.createdOrderReturn400(createResponse);
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией и без списка ингредиентов")
    public void createOrderWithAuthorizationAndWithoutIngredients() {
        ingredients.clear();
        Response createResponse = orderApi.createOrderWithAuthorization(accessToken, ingredients);
        orderCheckResponse.createdOrderReturn400(createResponse);
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongHashIngredients() {
        ingredients.get("ingredients").clear();
        ingredients.get("ingredients").addAll(Arrays.asList(""));
        Response createResponse = orderApi.createOrderWithoutAuthorization(ingredients);
        orderCheckResponse.createdOrderReturn500(createResponse);
    }
}
