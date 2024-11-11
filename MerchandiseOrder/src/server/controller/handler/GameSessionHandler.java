package server.controller.handler;

import model.*;
import server.controller.ServerThread;
import server.controller.threadManager.ThreadManager;
import server.dal.dao.GameSessionDAO;
import server.dal.dao.GameSessionDAOImpl;
import server.dal.dao.ProductDAO;
import server.dal.dao.ProductDAOImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class GameSessionHandler {
    public static DataTransferObject<Boolean> sendInvite(DataTransferObject<List<User>> request){
        User userOnline = request.getData().get(0);
        ServerThread serverThread = ThreadManager.getUserThread(userOnline);
        serverThread.sendEvent(new DataTransferObject<>("INVITE", request.getData()));
        return new DataTransferObject<>("Trash", true);
    }
    public static DataTransferObject<Boolean> sendJoin(DataTransferObject<List<User>> request){
        User userInvite = request.getData().get(1);
        ServerThread serverInvite = ThreadManager.getUserThread(userInvite);
        serverInvite.sendEvent(new DataTransferObject<>("ACCEPT", request.getData()));
        return new DataTransferObject<>("Trash", true);
    }

    public static DataTransferObject<Boolean> sendLeave(DataTransferObject<List<User>> request){
        User userInvite = request.getData().get(1);
        ServerThread serverInvite = ThreadManager.getUserThread(userInvite);
        serverInvite.sendEvent(new DataTransferObject<>("DECLINE", request.getData()));
        return new DataTransferObject<>("Trash", true);
    }

    public static DataTransferObject<Boolean> sendPlay(DataTransferObject<List<User>> request){
        GameSessionDAO gameSessionDAO = new GameSessionDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        List<Product> products = productDAO.getAllProducts();

        Collections.shuffle(products);
        Product[] first12ProductsArray = products.stream()
                .limit(12)
                .toArray(Product[]::new);
        User userOnline = request.getData().get(0);
        User userInvite = request.getData().get(1);
        GameSession gameSession = new GameSession(LocalDateTime.now(), LocalDateTime.now(), userInvite, userOnline);
        gameSessionDAO.createGameSession(gameSession);
        ServerThread serverInvite = ThreadManager.getUserThread(userInvite);
        ServerThread serverOnline = ThreadManager.getUserThread(userOnline);
        Pair<GameSession, User> pairInvite = new Pair<>(gameSession, userInvite);
        Pair<GameSession, User> pairOnline = new Pair<>(gameSession, userOnline);
        serverOnline.sendEvent(new DataTransferObject<>("PLAY", pairOnline, first12ProductsArray));
        serverInvite.sendEvent(new DataTransferObject<>("PLAY", pairInvite, first12ProductsArray));
        return new DataTransferObject<>("Trash", true);
    }
}
