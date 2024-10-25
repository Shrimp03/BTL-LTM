package server.dal.dao;

import model.User;

import java.sql.SQLException;

public interface UserDAO {
    public User getUser(String username, String password);
    public boolean updateUser(User user);
}