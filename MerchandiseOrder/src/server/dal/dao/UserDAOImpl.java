package server.dal.dao;

import model.User;
import utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl extends DAOConnection implements UserDAO {

    @Override
    public User getUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection con = getConnection(); // Mở kết nối mới
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
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
            e.printStackTrace();  //Sao k dùng logger nhỉ
        }
        return null; // Trả về null nếu không tìm thấy người dùng
    }

    @Override
    public boolean register(String username, String password, String email) {
        // Kiểm tra thông tin không được trống
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            System.out.println("Username, password, and email cannot be empty.");
            return false; // Không cho phép đăng ký nếu thông tin trống
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false; // Không cho phép đăng ký nếu email không đúng định dạng
        }

        // Kiểm tra xem tài khoản đã tồn tại
        if (userExists(username)) {
            System.out.println("Username already exists.");
            return false; // Tài khoản đã tồn tại, không cho phép đăng ký
        }

        String hashedPassword = PasswordUtil.hashPassword(password); // Mã hóa mật khẩu
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection con = getConnection(); // Mở kết nối mới
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, email);
            return stmt.executeUpdate() > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Đăng ký thất bại
        }
    }

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // Biểu thức chính quy kiểm tra email
        return email.matches(emailRegex); // Trả về true nếu email hợp lệ
    }


    @Override
    public boolean login(String username, String password) {
        if (!userExists(username)) {
            System.out.println("Username does not exist.");
            return false;
        }
        String hashedPassword = PasswordUtil.hashPassword(password); // Mã hóa mật khẩu
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection con = getConnection(); // Mở kết nối mới
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(hashedPassword); // So sánh với mật khẩu đã mã hóa
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Đăng nhập thất bại
    }

    @Override
    public boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection con = getConnection(); // Mở kết nối mới
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu tài khoản đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Người dùng không tồn tại
    }




    public static void main(String[] args) {
        UserDAOImpl dao = new UserDAOImpl();

    }
}
