package server.controller;

import dto.UserStatusDto;
import model.DataTransferObject;
import model.Product;
import server.controller.handler.ProductHandler;
import server.controller.handler.UserHandler;

public class RequestDispatcher {

    public static DataTransferObject<?> dispatch(DataTransferObject<?> request) {
        switch (request.getType()) {
            case "UpdateUser":
                return UserHandler.updateUser(request);
            case "GetProduct":
                return ProductHandler.getProduct(request);
            case "Login":
                return UserHandler.loginUser(request);  // Thêm xử lý login
            case "Register":
                return UserHandler.registerUser(request);
            case "GetUsers":
                return UserHandler.getAllUsers(request);
            case "GetUserByStatus":
                return UserHandler.getUserByStatus((DataTransferObject<UserStatusDto>) request);
            default:
                return new DataTransferObject<>("Error", "Unknown request type");
        }
    }
}