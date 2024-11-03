package server.controller.threadManager;

import model.GameSession;
import server.controller.ServerThread;
import model.DataTransferObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameSessionManager {
    // Concurrent map to store sessions
    private static final Map<GameSession, Session> sessions = new ConcurrentHashMap<>();

    // Method to create a new game session
    public static Session createSession(GameSession gameSession) {
        Session session = new Session(gameSession);
        sessions.put(gameSession, session);
        return session;
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

    // Inner class representing a game session
    public static class Session {
        private GameSession gameSession;
        private List<ServerThread> players;

        // Constructor to initialize the session
        public Session(GameSession gameSession) {
            this.gameSession = gameSession;
            this.players = new CopyOnWriteArrayList<>();
        }

        // Method to add a player to the session
        public void addPlayer(ServerThread player) {
            players.add(player);
        }

        // Method to remove a player from the session
        public void removePlayer(ServerThread player) {
            players.remove(player);
        }

        // Check if the session is empty
        public boolean isEmpty() {
            return players.isEmpty();
        }

        // Broadcast an event to all players in the session
        public void broadcast(DataTransferObject<?> event) {
            for (ServerThread player : players) {
                player.sendEvent(event);
            }
        }

        // Getter for sessionId
        public GameSession getGameSession() {
            return gameSession;
        }
    }
}