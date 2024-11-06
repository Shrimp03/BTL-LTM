package server.controller.handler;

import model.DataTransferObject;
import model.User;
import server.controller.ServerThread;
import server.controller.threadManager.ThreadManager;

import java.util.List;

public class GameSessionHandler {
    public static DataTransferObject<Boolean> sendInvite(DataTransferObject<List<User>> request){
        User userOnline = request.getData().get(0);
        ServerThread serverThread = ThreadManager.getUserThread(userOnline);
        serverThread.sendEvent(new DataTransferObject<>("INVITE", request.getData()));
        return new DataTransferObject<>("", true);
    }

}
