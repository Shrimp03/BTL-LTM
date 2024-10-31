package server.controller.handler;

import model.DataTransferObject;
import model.Product;
import model.User;
import server.dal.dao.ProductDAO;
import server.dal.dao.ProductDAOImpl;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;
import utils.PasswordUtil;

import java.util.Collections;
import java.util.List;

public class UserHandler {

    public static DataTransferObject<Boolean> updateUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User user = (User) request.getData();
        boolean updateSuccess = userDAO.updateUser(user);
        return new DataTransferObject<>("Update user response", updateSuccess);
    }

    // Thêm phương thức xử lý đăng ký
    public static DataTransferObject<Boolean> registerUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User registerUser = (User) request.getData();

        // Kiểm tra nếu username đã tồn tại
        if (userDAO.getUserByUsername(registerUser.getUsername()) != null) {
            return new DataTransferObject<>("FAIL", false);  // Username đã tồn tại
        }

        // Kiểm tra mật khẩu có hợp lệ hay không
        if (!PasswordUtil.isValidPassword(registerUser.getPassword())) {
            return new DataTransferObject<>("INVALID_PASSWORD", false);  // Mật khẩu không hợp lệ
        }

        // Mã hóa mật khẩu trước khi lưu
        String hashedPassword = PasswordUtil.hashPassword(registerUser.getPassword());
        registerUser.setPassword(hashedPassword);
        boolean registrationSuccess = userDAO.saveUser(registerUser);
        return new DataTransferObject<>("register response", registrationSuccess);
    }

    // Thêm phương thức xử lý đăng nhập
    public static DataTransferObject<User> loginUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User loginUser = (User) request.getData();


        // Kiểm tra username và mật khẩu
        User user = userDAO.getUserByUsername(loginUser.getUsername());
        if (user != null && PasswordUtil.verifyPassword(loginUser.getPassword(), user.getPassword())) {
            return new DataTransferObject<>("SUCCESS", user);  // Trả về thành công nếu đăng nhập đúng
        } else {
            return new DataTransferObject<>("FAIL", null);  // Trả về thất bại nếu sai
        }
    }

    public static DataTransferObject<List<User>> getAllUsers(DataTransferObject<?> request) {
       UserDAO userDAO = new UserDAOImpl();
       List<User> users = userDAO.getAllUsers();
        System.out.println(users.get(0).getUsername());

        return new DataTransferObject<>("GetUsersResponse", users);
    }


}
