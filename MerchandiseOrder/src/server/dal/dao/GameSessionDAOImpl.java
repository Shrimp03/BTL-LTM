package server.dal.dao;

import model.GameSession;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class GameSessionDAOImpl extends DAOConnection implements GameSessionDAO {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public GameSession getGameSessionById(int id) {
        String query = "SELECT * FROM game_sessions WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new GameSession(
                        id,
                        rs.getTimestamp("time_start").toLocalDateTime(), // Chuyển từ Timestamp thành LocalDateTime
                        rs.getTimestamp("time_finish").toLocalDateTime(), // Chuyển từ Timestamp thành LocalDateTime
                        userDAO.getUserById(rs.getInt("user1_id")),
                        userDAO.getUserById(rs.getInt("user2_id")),
                        rs.getInt("winner_id") != 0 ? userDAO.getUserById(rs.getInt("winner_id")) : null
                );
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateGameSession(GameSession gameSession) {
        String query = "UPDATE game_sessions SET time_start = ?, time_finish = ?, user1_id = ?, user2_id = ?, winner_id = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setTimestamp(1, gameSession.getTimeStart()); // Sử dụng Timestamp trực tiếp
            ps.setTimestamp(2, gameSession.getTimeFinish()); // Sử dụng Timestamp trực tiếp
            ps.setInt(3, gameSession.getUser1().getId());
            ps.setInt(4, gameSession.getUser2().getId());

            // Kiểm tra winner có null không trước khi lấy ID
            if (gameSession.getWinner() != null) {
                ps.setInt(5, gameSession.getWinner().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setInt(6, gameSession.getId());
            int rowsAffected = ps.executeUpdate();

            // If at least one row was updated, return true
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createGameSession(GameSession gameSession) {
        String query = "INSERT INTO game_sessions (time_start, time_finish, user1_id, user2_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setTimestamp(1, gameSession.getTimeStart()); // Using Timestamp for start time
            ps.setTimestamp(2, gameSession.getTimeFinish()); // Using Timestamp for finish time
            ps.setInt(3, gameSession.getUser1().getId());    // Setting user1's ID
            ps.setInt(4, gameSession.getUser2().getId());    // Setting user2's ID

            int rowsAffected = ps.executeUpdate();

            // Return true if a row was inserted successfully
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}