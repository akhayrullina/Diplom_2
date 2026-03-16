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

public class GetOrdersFromUser extends BaseURL {
    private OrderApi orderApi;
    private OrderCheckResponse orderCheckResponse;
    private UserApi userApi;
    private UserCheckResponse userCheckResponse;
    private User user;
    private String accessToken;

    @BeforeEach
    public void init() {
        orderApi = new OrderApi(setUp());
        orderCheckResponse = new OrderCheckResponse();

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
    }

    @Test
    @DisplayName("Проверка получения информации о заказе авторизованного пользователя")
    public void getInfoAboutOrderFromSpecificUserWithAuthorization() {
        Response response = orderApi.getInfoAboutOrderFromSpecificUser(accessToken);
        orderCheckResponse.getInfoAboutOrderFromUserReturn200(response);
    }

    @Test
    @DisplayName("Проверка получения информации о заказе неавторизованного пользователя")
    public void getInfoAboutOrderFromSpecificUserWithoutAuthorization() {
        Response response = orderApi.getInfoAboutOrderFromSpecificUser("");
        orderCheckResponse.getInfoAboutOrderFromUnauthorizedUserReturn401(response);
    }
}
