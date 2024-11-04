package client.controller;

import dto.UserStatusDto;
import model.DataTransferObject;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientSocket {
    public ClientSocket() {}

    public boolean updateUser(User user) {
        try {
            DataTransferObject<User> dto = new DataTransferObject<>("UpdateUser", user);
            Client.oos.writeObject(dto);
            Client.oos.flush();
            DataTransferObject<Boolean> res = (DataTransferObject<Boolean>) Client.ois.readObject();

            if (!res.getType().equals("Update user response"))
                return false;
            return res.getData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Product[]> getProduct() {
        try {
            DataTransferObject<?> dto = new DataTransferObject<>("GetProduct");
//            System.out.println(dto.getData());
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = Client.ois.readObject();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<Product[]> res = (DataTransferObject<Product[]>) response;
                if (!res.getType().equals("GetProductResponse"))
                    return Optional.empty();
                System.out.println(res.getData());
                return Optional.of(res.getData());
            } else {
                System.err.println("Phản hồi không đúng kiểu: " + response.getClass().getName());
                return Optional.empty();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    public Boolean registerUser(User user) {
        try {
            // Tạo đối tượng DataTransferObject để gửi thông tin đăng ký
            DataTransferObject<User> dto = new DataTransferObject<>("Register", user);
            Client.oos.writeObject(dto); // Gửi yêu cầu đến server
            Client.oos.flush();

            DataTransferObject<Boolean> res = (DataTransferObject<Boolean>) Client.ois.readObject();

            if (!res.getType().equals("register response"))
                return false ;
            return res.getData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
        return false; // Trả về null nếu có lỗi hoặc đăng ký không thành công
    }

    public User loginUser(String username, String password) {
        try {
            DataTransferObject<User> userLogin = new DataTransferObject<>("Login", new User(username, password));
            Client.oos.writeObject(userLogin);
            Client.oos.flush();
            DataTransferObject<User> res = (DataTransferObject<User>) Client.ois.readObject();

            if ("SUCCESS".equals(res.getType())) {
                return res.getData();  // Trả về User nếu đăng nhập thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Trả về null nếu đăng nhập thất bại
    }

    public List<User> getAllUsers() {
        try {
            // Tạo đối tượng truyền dữ liệu yêu cầu danh sách người dùng
            DataTransferObject<?> dto = new DataTransferObject<>("GetUsers");

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

            // Nhận phản hồi từ server
            DataTransferObject<List<User>> res = (DataTransferObject<List<User>>) Client.ois.readObject();

            // Kiểm tra phản hồi từ server
            if ("GetUsersResponse".equals(res.getType())) {
                List<User> users = res.getData();

                // Kiểm tra nếu dữ liệu người dùng bị null
                if (users != null) {
                    System.out.println(users);
                    return users;  // Trả về danh sách người dùng nếu thành công
                } else {
                    System.out.println("No users found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;  // Trả về null nếu có lỗi xảy ra
    }

    public List<User> getUsersByStatus(String status, String username) {
        try {
            // Tạo đối tượng truyền dữ liệu yêu cầu danh sách người dùng
            DataTransferObject<?> dto = new DataTransferObject<>("GetUserByStatus", new UserStatusDto(status, username));

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

            // Nhận phản hồi từ server
            DataTransferObject<List<User>> res = (DataTransferObject<List<User>>) Client.ois.readObject();

            // Kiểm tra phản hồi từ server
            if ("GetUserByStatus".equals(res.getType())) {
                List<User> users = res.getData();

                // Kiểm tra nếu dữ liệu người dùng bị null
                if (users != null) {
                    System.out.println(users);
                    return users;  // Trả về danh sách người dùng nếu thành công
                } else {
                    System.out.println("No users found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;  // Trả về null nếu có lỗi xảy ra
    }
}