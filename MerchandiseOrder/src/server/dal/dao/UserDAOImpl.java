package server.dal.dao;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl extends DAOConnection implements UserDAO {

    @Override
    public User getUser(String username, String password)  {
        try {
            PreparedStatement ps = con.prepareStatement("select * from users where username = ? and password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs.toString() + "jfkdjkdjkf");
            while (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("points"), rs.getString("avatar"), rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User getUserById(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("points"), rs.getString("avatar"), rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE users SET email = ?, points = ?, avatar = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPoints());
            ps.setString(3, user.getAvatar());
            ps.setString(4, user.getStatus().toString());
            ps.setInt(5, user.getId());

            int rowsAffected = ps.executeUpdate();

            // If at least one row was updated, return true
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    @Override
    public User getUserByUsername(String username) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("points"), rs.getString("avatar"), rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Thêm phương thức lưu người dùng mới
    @Override
    public boolean saveUser(User user) {
        String query = "INSERT INTO users (username, password, email, points, avatar) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());  // Mật khẩu đã được mã hóa trước đó
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPoints());
            ps.setString(5, user.getAvatar());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;  // Trả về false nếu có lỗi
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from users ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                 User user = new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("points"), rs.getString("avatar"), rs.getString("status")
                );
                 users.add(user);
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        return null;
    }

    public static void main(String[] args) {
        UserDAOImpl dao = new UserDAOImpl();
        User user = dao.getUser("user1", "password1");
        System.out.println(user.getHighScore());

    }
}
