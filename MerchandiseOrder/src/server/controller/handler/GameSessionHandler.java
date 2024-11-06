package server.controller.handler;

import model.DataTransferObject;
import model.GameSession;
import model.User;
import server.controller.ServerThread;
import server.controller.threadManager.ThreadManager;

import java.util.List;

public class GameSessionHandler {

    public static void sendInvite(DataTransferObject<List<User>> request){
        List<User> users = request.getData();
//        System.out.println(user.getUsername());
        ServerThread serverThread = ThreadManager.getUserThread(users.get(0));
        if (serverThread != null) {
            serverThread.sendEvent( new DataTransferObject<>("INVITE", users));
        } else {
            System.out.println("User not found or not connected: " + users);
        }
    }

//    public static DataTransferObject<?> resend(DataTransferObject<List<User>> request){
//        switch (request.getType()){
//            case "ACCEPT":
//                System.out.println("accept");
//            case "DECLINE":
//                System.out.println("decline");
//            default:
//                return new DataTransferObject<>("Error", "Unknown request type");
//        }
//    }
}
