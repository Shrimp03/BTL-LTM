package server.controller.threadManager;

import model.DataTransferObject;
import model.GameSession;
import server.controller.ServerThread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Session {
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

    @Override
    public String toString() {
        return "Session{" +
                "gameSession=" + gameSession +
                ", players=" + players +
                '}';
    }
}