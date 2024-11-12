package server.controller;

import model.UserStatusDto;
import model.DataTransferObject;
import model.User;
import server.controller.handler.GameSessionHandler;
import server.controller.handler.GameSoloHandler;
import server.controller.handler.ProductHandler;
import server.controller.handler.UserHandler;

import java.util.List;

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
            case "Logout":
                return UserHandler.logoutUser(request);
            case "GetUserByStatus":
                return UserHandler.getUserByStatus((DataTransferObject<UserStatusDto>) request);
            case "RequestSolo": //todo: dùng khi chưa có chức năng tạo phòng solo
                return GameSoloHandler.requestSolo(request);
            case "SendCorrectProductIds":
                return GameSoloHandler.sendCorrectProductIds(request);
            case "INVITE":
                return GameSessionHandler.sendInvite((DataTransferObject<List<User>>) request);
            case "ACCEPT":
                return GameSessionHandler.sendJoin((DataTransferObject<List<User>>) request);
            case "DECLINE":
                return GameSessionHandler.sendLeave((DataTransferObject<List<User>>) request);
            case "PLAY":
                return GameSessionHandler.sendPlay((DataTransferObject<List<User>>) request);
            case "UpdateStatusUser":
                return UserHandler.updateStatusUser((DataTransferObject<User>) request);
            default:
                return new DataTransferObject<>("Error", "Unknown request type");
        }
    }
}