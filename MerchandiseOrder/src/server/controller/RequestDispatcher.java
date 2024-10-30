package server.controller;

import model.DataTransferObject;
import server.controller.handler.UserHandler;

public class RequestDispatcher {

    public static DataTransferObject<?> dispatch(DataTransferObject<?> request) {
        switch (request.getType()) {
            case "UpdateUser":
                return UserHandler.updateUser(request);
            case "GetProduct":
                Product product = new Product("Product1", 123.45);
                System.out.println("Sending product: " + product);


            default:
                return new DataTransferObject<>("Error", "Unknown request type");
        }
    }
}