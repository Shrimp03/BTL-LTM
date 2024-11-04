package server.dal.dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    public User getUser(String username, String password);
    public User getUserById(int id);
    public boolean updateUser(User user);
    // Thêm phương thức mới để tìm user theo username
    public User getUserByUsername(String username);
    boolean saveUser(User user);
    List<User> getAllUsers();
}