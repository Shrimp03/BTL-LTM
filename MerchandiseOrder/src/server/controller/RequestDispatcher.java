package server.controller;

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
            default:
                return new DataTransferObject<>("Error", "Unknown request type");
        }
    }
}