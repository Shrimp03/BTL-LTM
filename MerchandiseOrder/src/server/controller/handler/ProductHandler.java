package server.controller.handler;

import model.DataTransferObject;
import model.Product;
import model.User;
import server.dal.dao.ProductDAO;
import server.dal.dao.ProductDAOImpl;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductHandler {
    public static DataTransferObject<Product[]> getProduct(DataTransferObject<?> request) {
        ProductDAO productDAO = new ProductDAOImpl();
        List<Product> products = productDAO.getAllProducts();

        Collections.shuffle(products);
        Product[] first12ProductsArray = products.stream()
                .limit(12)
                .toArray(Product[]::new);

        return new DataTransferObject<>("GetProductResponse", first12ProductsArray);
    }
}
