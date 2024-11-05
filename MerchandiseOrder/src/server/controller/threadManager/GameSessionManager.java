package server.controller.threadManager;

import model.GameSession;
import server.controller.ServerThread;
import model.DataTransferObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    // Concurrent map to store sessions
    private static final Map<GameSession, Session> sessions = new ConcurrentHashMap<>();

    // Method to create a new game session
    public static void createSession(GameSession gameSession) {
        Session session = new Session(gameSession);
        sessions.put(gameSession, session);
    }

    // Method to add a user to a session
    public static void addUserToSession(GameSession gameSession, ServerThread userThread) {
        if (userThread == null) return; // Check for null userThread
        Session session = sessions.get(gameSession);
        if (session != null) {
            session.addPlayer(userThread);
        }
    }

    // Method to remove a user from a session
    public static void removeUserFromSession(GameSession gameSession, ServerThread userThread) {
        if (userThread == null) return; // Check for null userThread
        Session session = sessions.get(gameSession);
        if (session != null) {
            session.removePlayer(userThread);
            if (session.isEmpty()) {
                sessions.remove(gameSession);
            }
        }
    }

    // Method to broadcast an event to all players in a session
    public static void broadcastToSession(GameSession gameSession, DataTransferObject<?> event) {
        Session session = sessions.get(gameSession);
        if (session != null) {
            session.broadcast(event);
        }
    }

    public static Session getSession(GameSession gameSession) {
        return sessions.get(gameSession);
    }
}