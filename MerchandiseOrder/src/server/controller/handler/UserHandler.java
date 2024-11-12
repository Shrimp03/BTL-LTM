package server.controller.handler;

import model.UserStatusDto;
import model.*;
import server.controller.threadManager.ThreadManager;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;
import utils.PasswordUtil;

import java.util.List;

public class UserHandler {

    // Thêm phương thức xử lý đăng ký
    public static DataTransferObject<Boolean> registerUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User registerUser = (User) request.getData();

        // Kiểm tra nếu username đã tồn tại
        if (userDAO.getUserByUsername(registerUser.getUsername()) != null) {
            return new DataTransferObject<>("FAIL", false);  // Username đã tồn tại
        }

        // Kiểm tra nếu email có hợp lệ hay không
        if (!PasswordUtil.isValidEmail(registerUser.getEmail())) {
            return new DataTransferObject<>("INVALID_EMAIL", false);  // Email không hợp lệ
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
        if (user != null) {
            // Kiểm tra nếu người dùng đã đăng nhập (trạng thái ONLINE)
            if (user.getStatus() == UserStatus.ONLINE) {
                return new DataTransferObject<>("ALREADY_LOGGED_IN", null);  // Người dùng đã đăng nhập ở nơi khác
            }

            // Kiểm tra mật khẩu
            if (PasswordUtil.verifyPassword(loginUser.getPassword(), user.getPassword())) {
                // Cập nhật trạng thái người dùng thành ONLINE
                user.setStatus(UserStatus.ONLINE);
                userDAO.updateUser(user);
                return new DataTransferObject<>("SUCCESS", user);  // Trả về thành công nếu đăng nhập đúng
            }
        }
        return new DataTransferObject<>("FAIL", null);  // Trả về thất bại nếu sai
    }

    public static DataTransferObject<Boolean> updateUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User user = (User) request.getData();
        boolean updateSuccess = userDAO.updateUser(user);
        return new DataTransferObject<>("Update user response", updateSuccess);
    }
    public static DataTransferObject<List<User>> getAllUsers(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        List<User> users = userDAO.getAllUsers();
        System.out.println(users.get(0).getUsername());

        return new DataTransferObject<>("GetUsersResponse", users);
    }

    public static DataTransferObject<List<User>> getUserByStatus(DataTransferObject<UserStatusDto> request) {
        UserDAO userDAO = new UserDAOImpl();
        List<User> users = userDAO.getUserByStatus(request.getData().getStatus(), request.getData().getUserName());

        return new DataTransferObject<>("GetUserByStatus", users);

    }


    public static DataTransferObject<Boolean> logoutUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        User logoutUser = (User) request.getData();

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        User user = userDAO.getUserByUsername(logoutUser.getUsername());
        if (user != null) {
            // Cập nhật trạng thái người dùng thành OFFLINE
            user.setStatus(UserStatus.OFFLINE);
            userDAO.updateUser(user);

            boolean updateSuccess = userDAO.updateUser(user);
            ThreadManager.removeUserThread(user);
            if (updateSuccess) {
                return new DataTransferObject<>("Logout response", true); // Trả về phản hồi thành công
            }
        }
        return new DataTransferObject<>("LOGOUT_FAIL", false); // Trả về thất bại nếu có lỗi
    }

    public static DataTransferObject<Boolean> updateStatusUser(DataTransferObject<?> request) {
        UserDAO userDAO = new UserDAOImpl();
        return new DataTransferObject<>("UpdateStatusUser", userDAO.updateStatusUser((Pair<Integer, String>) request.getData()));
    }

}