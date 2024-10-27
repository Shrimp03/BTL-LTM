package server.controller.handler;

import model.DataTransferObject;
import model.User;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;

public class UserHandler {

    public static DataTransferObject<Boolean> updateUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User user = (User) request.getData();

        boolean updateSuccess = userDAO.updateUser(user);

        return new DataTransferObject<>("Update user response", updateSuccess);
    }
}
