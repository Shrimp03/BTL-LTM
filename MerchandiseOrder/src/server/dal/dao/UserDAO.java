package server.dal.dao;

import model.User;

import java.sql.SQLException;

public interface UserDAO {
    User getUser(String username) throws SQLException;
    boolean register(String username, String password, String email);
    boolean login(String username, String password);
    boolean userExists(String username);

}