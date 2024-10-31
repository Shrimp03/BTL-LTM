package client.controller;

import model.DataTransferObject;
import model.Product;
import model.User;

import java.io.IOException;
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

}