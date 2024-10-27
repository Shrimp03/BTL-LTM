package server.controller;

import model.DataTransferObject;
import server.controller.handler.UserHandler;

public class RequestDispatcher {

    public static DataTransferObject<?> dispatch(DataTransferObject<?> request) {
        switch (request.getType()) {
            case "UpdateUser":
                return UserHandler.updateUser(request);
            default:
                return new DataTransferObject<>("Error", "Unknown request type");
        }
    }
}