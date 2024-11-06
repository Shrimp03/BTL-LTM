package server.controller.handler;

import model.DataTransferObject;
import model.GameSession;
import model.Pair;
import model.User;
import server.controller.ServerThread;
import server.controller.threadManager.ThreadManager;

import java.util.Date;
import java.util.List;

public class GameSessionHandler {
    public static DataTransferObject<Boolean> sendInvite(DataTransferObject<List<User>> request){
        User userOnline = request.getData().get(0);
        ServerThread serverThread = ThreadManager.getUserThread(userOnline);
        serverThread.sendEvent(new DataTransferObject<>("INVITE", request.getData()));
        return new DataTransferObject<>("", true);
    }
    public static DataTransferObject<Boolean> sendJoin(DataTransferObject<List<User>> request){
        User userInvite = request.getData().get(1);
        ServerThread serverInvite = ThreadManager.getUserThread(userInvite);
        serverInvite.sendEvent(new DataTransferObject<>("ACCEPT", request.getData()));
        return new DataTransferObject<>("", true);
    }

    public static DataTransferObject<Boolean> sendLeave(DataTransferObject<List<User>> request){
        User userInvite = request.getData().get(1);
        ServerThread serverInvite = ThreadManager.getUserThread(userInvite);
        serverInvite.sendEvent(new DataTransferObject<>("DECLINE", request.getData()));
        return new DataTransferObject<>("", true);
    }

    public static DataTransferObject<Boolean> sendPlay(DataTransferObject<List<User>> request){
        User userOnline = request.getData().get(0);
        User userInvite = request.getData().get(1);
        GameSession gameSession = new GameSession(new Date(), new Date(), userInvite, userOnline);
        ServerThread serverInvite = ThreadManager.getUserThread(userInvite);
        ServerThread serverOnline = ThreadManager.getUserThread(userOnline);
        Pair<GameSession, User> pair = new Pair<>(gameSession, userInvite);
        serverOnline.sendEvent(new DataTransferObject<>("PLAY", pair));
        serverInvite.sendEvent(new DataTransferObject<>("PLAY", pair));
        return new DataTransferObject<>("", true);
    }
}
