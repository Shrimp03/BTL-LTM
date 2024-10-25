package server.dal.dao;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl extends DAOConnection implements UserDAO {

    @Override
    public User getUser(String username, String password)  {
        try {
            PreparedStatement ps = con.prepareStatement("select * from users where username = ? and password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("points"), rs.getString("avatar")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE users SET email = ?, points = ?, avatar = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPoints());
            ps.setString(3, user.getAvatar());
            ps.setInt(4, user.getId());

            int rowsAffected = ps.executeUpdate();

            // If at least one row was updated, return true
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        UserDAOImpl dao = new UserDAOImpl();
        User user = dao.getUser("user1", "password1");
        System.out.println(user.getHighScore());

    }
}
