package client.controller;

import model.DataTransferObject;
import model.User;

import java.io.IOException;

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
}