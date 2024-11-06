package server.dal.dao;

import model.GameSession;

public interface GameSessionDAO {
    public GameSession getGameSessionById(int id);
    public boolean updateGameSession(GameSession gameSession);
}