package server.dal.dao;

import model.Product;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl extends DAOConnection implements ProductDAO {

    @Override
    public List<Product> getAllProducts() {
        try {
            List<Product> products = new ArrayList<Product>();
            PreparedStatement ps = con.prepareStatement("select * from products");
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                Product pr =  new Product(rs.getInt("id"), rs.getString("name"),
                        rs.getString("image_url")
                );
                products.add(pr);
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        return null;
    }
}
