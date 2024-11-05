package client.controller;

import client.view.PopupInvite;
import dto.UserStatusDto;
import model.DataTransferObject;
import model.Product;
import model.User;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientSocket {
    // Áp dụng Singleton
    private static ClientSocket instance;
    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();

    // Constructor riêng để tránh tạo nhiều phiên bản
    private ClientSocket() {}

    // Phương thức để lấy phiên bản duy nhất của ClientSocket
    public static synchronized ClientSocket getInstance() {
        if (instance == null) {
            instance = new ClientSocket();
        }
        return instance;
    }

    public void listening() {
        new Thread(() -> {
            try {
                while (true) {
                    Object response = Client.ois.readObject();
                        DataTransferObject<List<User>> res = (DataTransferObject<List<User>>) response;
                        if("INVITE".equals(res.getType())){
                            System.out.println("Open Micro:");
                            System.out.println(res.getData().get(0));
                            PopupInvite.showInvitationDialog(res.getData().get(0), res.getData().get(1));
                        }
                        else {
                            messageQueue.put(response);
                        }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
//                e.printStackTrace();
            }
        }).start();
    }

    // Phương thức lấy gói tin từ hàng đợi
    public Object getNextMessage() throws InterruptedException {
        return messageQueue.take();
    }

    public boolean updateUser(User user) {
        try {
            DataTransferObject<User> dto = new DataTransferObject<>("UpdateUser", user);
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<Boolean> res = (DataTransferObject<Boolean>) response;
                if (!"Update user response".equals(res.getType())) {
                    return false;
                }
                return res.getData();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Product[]> getProduct() {
        try {
            DataTransferObject<?> dto = new DataTransferObject<>("GetProduct");
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<Product[]> res = (DataTransferObject<Product[]>) response;
                if (!"GetProductResponse".equals(res.getType())) {
                    return Optional.empty();
                }
                return Optional.of(res.getData());
            } else {
                System.err.println("Invalid response type: " + response.getClass().getName());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Boolean registerUser(User user) {
        try {
            DataTransferObject<User> dto = new DataTransferObject<>("Register", user);
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<Boolean> res = (DataTransferObject<Boolean>) response;
                if (!"register response".equals(res.getType())) {
                    return false;
                }
                return res.getData();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User loginUser(String username, String password) {
        try {
            DataTransferObject<User> userLogin = new DataTransferObject<>("Login", new User(username, password));
            Client.oos.writeObject(userLogin);
            Client.oos.flush();

            Object response = getNextMessage();
            System.out.println(response);
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<User> res = (DataTransferObject<User>) response;
                if ("SUCCESS".equals(res.getType())) {

                    return res.getData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        try {
            DataTransferObject<?> dto = new DataTransferObject<>("GetUsers");
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<List<User>> res = (DataTransferObject<List<User>>) response;
                if ("GetUsersResponse".equals(res.getType())) {
                    List<User> users = res.getData();
                    if (users != null) {
                        return users;
                    } else {
                        System.out.println("No users found.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUsersByStatus(String status, String username) {
        try {
            // Tạo đối tượng truyền dữ liệu yêu cầu danh sách người dùng
            DataTransferObject<?> dto = new DataTransferObject<>("GetUserByStatus", new UserStatusDto(status, username));

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();

            // Nhận phản hồi từ server
            if(response instanceof DataTransferObject<?>) {
                DataTransferObject<List<User>> res = (DataTransferObject<List<User>>) response;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;  // Trả về null nếu có lỗi xảy ra
    }

    public GameSession requestSolo(User user) {
        try {
            DataTransferObject<User> dto = new DataTransferObject<>("RequestSolo", user);
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<GameSession> res = (DataTransferObject<GameSession>) response;
                if ("ResponseSolo".equals(res.getType())) {
                    return res.getData();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sendCorrectProductIds(Pair<GameSession, ArrayList<Integer>> dataSend) {
        try {
            DataTransferObject<Pair<GameSession, ArrayList<Integer>>> dto = new DataTransferObject<>("SendCorrectProductIds", dataSend);
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
            if (response instanceof DataTransferObject<?>) {
                DataTransferObject<Boolean> res = (DataTransferObject<Boolean>) response;
                if ("ReceiveCorrectProductIds".equals(res.getType())) {
                    return res.getData();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendInvite(User user, User inviter) {
        try {
            // Tạo đối tượng truyền dữ liệu yêu cầu danh sách người dùng
            List<User> twoUser =  new ArrayList<>();
            twoUser.add(user);
            twoUser.add(inviter);
            DataTransferObject<?> dto = new DataTransferObject<>("INVITE", twoUser);

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

            Object response = getNextMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}