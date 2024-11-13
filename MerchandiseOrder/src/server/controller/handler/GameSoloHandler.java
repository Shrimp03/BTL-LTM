package server.controller.handler;

import model.*;
import server.controller.threadManager.GameSessionManager;
import server.controller.threadManager.Session;
import server.controller.threadManager.ThreadManager;
import server.dal.dao.GameSessionDAO;
import server.dal.dao.GameSessionDAOImpl;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        GameSessionDAO gameSessionDAO = new GameSessionDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        Pair<Pair<User, GameSession>, Pair<ArrayList<Integer>, Boolean>> dataRecive = (Pair<Pair<User, GameSession>, Pair<ArrayList<Integer>, Boolean>>) request.getData();
        User currentUser = dataRecive.getFirst().getFirst();
        GameSession gameSession = dataRecive.getFirst().getSecond();
        ArrayList<Integer> productIds = dataRecive.getSecond().getFirst();
        Boolean finish = dataRecive.getSecond().getSecond();

        User nextUser = gameSession.getUser1();
        if (currentUser.equals(gameSession.getUser1())) {
            nextUser = gameSession.getUser2();
        }

        Pair<Pair<User, GameSession>, ArrayList<Integer>> dataSend = new Pair<>(new Pair<>(nextUser, gameSession), productIds);
        GameSessionManager.broadcastToSession(gameSession, new DataTransferObject<Pair<Pair<User, GameSession>, ArrayList<Integer>>>("BroadCastProductIds", dataSend));

        if (finish && gameSessionDAO.getGameSessionById(gameSession.getId()).getWinner() == null) {
            gameSession.setWinner(currentUser);
            gameSession.setTimeFinish(Timestamp.valueOf(LocalDateTime.now()));
            gameSessionDAO.updateGameSession(gameSession);
            gameSession.getUser1().setStatus(UserStatus.ONLINE);
            gameSession.getUser2().setStatus(UserStatus.ONLINE);
            userDAO.updateUser(gameSession.getUser1());
            userDAO.updateUser(gameSession.getUser2());

            GameSessionManager.broadcastToSession(gameSession, new DataTransferObject<Pair<User, GameSession>>("GameSoloFinish", new Pair<>(currentUser, gameSession)));
        }

        return new DataTransferObject<>("ReceiveCorrectProductIds", true);
    }

    public static DataTransferObject<Boolean> sendOutSoloToHome(DataTransferObject<?> request) {
        GameSessionDAO gameSessionDAO = new GameSessionDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        Pair<User, GameSession> dataRecive = (Pair<User, GameSession>) request.getData();
        User currentUser = dataRecive.getFirst();
        GameSession gameSession = dataRecive.getSecond();
        User winner = (currentUser.equals(gameSession.getUser1())) ? gameSession.getUser2() : gameSession.getUser1();

        if (gameSessionDAO.getGameSessionById(gameSession.getId()).getWinner() == null) {
            gameSession.setWinner(winner);
            gameSession.setTimeFinish(Timestamp.valueOf(LocalDateTime.now()));
            gameSessionDAO.updateGameSession(gameSession);
            gameSession.getUser1().setStatus(UserStatus.ONLINE);
            gameSession.getUser2().setStatus(UserStatus.ONLINE);
            userDAO.updateUser(gameSession.getUser1());
            userDAO.updateUser(gameSession.getUser2());

            GameSessionManager.broadcastToSession(gameSession, new DataTransferObject<Pair<User, GameSession>>("GameSoloFinish", new Pair<>(winner, gameSession)));
        }

        return new DataTransferObject<>("ReceiveOutSoloToHome", true);
    }
}