package server.dal.dao;

import model.GameSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSessionDAOImpl extends DAOConnection implements GameSessionDAO {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public GameSession getGameSessionById(int id) {
        String query = "SELECT * FROM game_sessions WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new GameSession(id, rs.getDate("time_start"), rs.getDate("time_finish"),
                        userDAO.getUserById(rs.getInt("user1_id")), userDAO.getUserById(rs.getInt("user2_id")),
                        userDAO.getUserById(rs.getInt("winner_id")));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}