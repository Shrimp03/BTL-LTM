package client.controller;

import model.DataTransferObject;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.Optional;
import java.util.Objects;

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


}