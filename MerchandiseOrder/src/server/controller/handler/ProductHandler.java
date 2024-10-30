package server.controller.handler;

import model.DataTransferObject;
import model.User;
import server.dal.dao.ProductDAO;
import server.dal.dao.ProductDAOImpl;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;

public class ProductHandler {
    public static DataTransferObject<Boolean> getProduct(DataTransferObject<?> request) {
        ProductDAO productDAO = new ProductDAOImpl();
        User user = (User) request.getData();

        boolean updateSuccess = userDAO.updateUser(user);

        return new DataTransferObject<>("GetProductResponse", product);
    }
}
