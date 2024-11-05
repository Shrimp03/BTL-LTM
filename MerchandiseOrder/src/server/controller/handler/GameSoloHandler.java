package server.controller.handler;

import model.DataTransferObject;
import model.GameSession;
import model.Pair;
import model.User;
import server.controller.threadManager.GameSessionManager;
import server.controller.threadManager.Session;
import server.controller.threadManager.ThreadManager;
import server.dal.dao.GameSessionDAO;
import server.dal.dao.GameSessionDAOImpl;

import java.util.ArrayList;

public class GameSoloHandler {
    // todo: dùng khi chưa có chức năng tạo phòng solo
    public static DataTransferObject<GameSession> requestSolo(DataTransferObject<?> request) {
        GameSessionDAO gameSessionDAO = new GameSessionDAOImpl();
        GameSession gameSession = gameSessionDAO.getGameSessionById(1);
        Session session = GameSessionManager.getSession(gameSession);
        if (session == null) {
            GameSessionManager.createSession(gameSession);
            session = GameSessionManager.getSession(gameSession);
        }

        User user = (User) request.getData();
        session.addPlayer(ThreadManager.getUserThread(user));

        return new DataTransferObject<>("ResponseSolo", gameSession);
    }

    public static DataTransferObject<Boolean> sendCorrectProductIds(DataTransferObject<?> request) {
        Pair<GameSession, ArrayList<Integer>> dataRecive = (Pair<GameSession, ArrayList<Integer>>) request.getData();
        GameSession gameSession = dataRecive.getFirst();
        ArrayList<Integer> productIds = dataRecive.getSecond();

        GameSessionManager.broadcastToSession(gameSession, new DataTransferObject<ArrayList<Integer>>("BroadCastProductIds", productIds));

        return new DataTransferObject<>("ReceiveCorrectProductIds", true);
    }
}