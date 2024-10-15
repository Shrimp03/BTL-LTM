package Server.Dal.DAO;

import Model.User;
import Utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UserDAOImpl extends DAOConnection implements UserDAO {


    @Override
    public User getUser(String username, String password) {
        try {
            String hashedPassword = PasswordUtil.hashPassword(password);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("points"),
                        rs.getString("avatar")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addUser(User user) throws SQLException {
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword()); // Mã hóa mật khẩu
        PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
        ps.setString(1, user.getUsername());
        ps.setString(2, hashedPassword);
        ps.setString(3, user.getEmail());
        ps.executeUpdate();
    }


    public static void main(String[] args) {
        UserDAOImpl dao = new UserDAOImpl();
//        User user = dao.getUser("", "");
//        System.out.println(user.getHighScore());

    }


    @Override
    public boolean userExists(String username) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean register(String username, String password) {
        // Kiểm tra xem tài khoản đã tồn tại hay chưa
        if (userExists(username)) {
            System.out.println("Username already exists.");
            return false; // Tài khoản đã tồn tại, không cho phép đăng ký
        }

        String hashedPassword = PasswordUtil.hashPassword(password); // Mã hóa mật khẩu
        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Đăng ký thất bại
        }
    }

    @Override
    public boolean login(String username, String password) {
        if (!userExists(username)) {
            System.out.println("Username does not exist.");
            return false;
        }
        String hashedPassword = PasswordUtil.hashPassword(password); // Mã hóa mật khẩu
        try (PreparedStatement stmt = con.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(hashedPassword); // So sánh với mật khẩu đã mã hóa
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
