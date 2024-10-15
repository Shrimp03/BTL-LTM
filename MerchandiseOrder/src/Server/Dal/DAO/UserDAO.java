package Server.Dal.DAO;

import Model.User;

import java.sql.SQLException;

public interface UserDAO {
    public User getUser(String username, String password) throws SQLException;
    boolean register(String username, String password);

    boolean login(String username, String password);
    boolean userExists(String username);
}

