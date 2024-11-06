package client.controller;

import client.view.GameSoloListener;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientSocket {
    // Áp dụng Singleton
    private static ClientSocket instance;
    private final BlockingQueue<Object> messageQueue = new LinkedBlockingQueue<>();
    private final ArrayList<Pair<GameSoloListener, User>> listeners = new ArrayList<>();

    // Constructor riêng để tránh tạo nhiều phiên bản
    private ClientSocket() {}

    // Phương thức để lấy phiên bản duy nhất của ClientSocket
    public static synchronized ClientSocket getInstance() {
        if (instance == null) {
            instance = new ClientSocket();
        }
        return instance;
    }

    public void addGameSoloListener(GameSoloListener listener, User user) {
        listeners.add(new Pair<>(listener, user));
    }

    public void listening() {
        new Thread(() -> {
            try {
                while (true) {
                    Object response = Client.ois.readObject();
                    try {
                        DataTransferObject<?> dto = (DataTransferObject<?>) response;
                        if (Objects.equals(dto.getType(), "BroadCastProductIds")) {
                            Pair<Pair<User, GameSession>, ArrayList<Integer>> dataReceived = (Pair<Pair<User, GameSession>, ArrayList<Integer>>) dto.getData();
                            User nextUser = dataReceived.getFirst().getFirst();
                            GameSession gameSession = dataReceived.getFirst().getSecond();
                            ArrayList<Integer> productIds = dataReceived.getSecond();

                            for (Pair<GameSoloListener, User> p : listeners) {
                                if (p.getSecond().equals(gameSession.getUser1()) || p.getSecond().equals(gameSession.getUser2())) {
                                    p.getFirst().onProductOrderReceived(new Pair<>(nextUser, productIds));
                                }
                            }
                        } else if (Objects.equals(dto.getType(), "GameSoloFinish")) {
                            Pair<User, GameSession> dataRecived = (Pair<User, GameSession>) dto.getData();
                            User winner = dataRecived.getFirst();
                            GameSession gameSession = dataRecived.getSecond();
                            for (Pair<GameSoloListener, User> p : listeners) {
                                if (p.getSecond().equals(gameSession.getUser1()) || p.getSecond().equals(gameSession.getUser2())) {
                                    p.getFirst().onFinishGame(winner);
                                }
                            }

                        } else {
                            messageQueue.put(response);
                        }
                    } catch (ClassCastException ignored) {
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

    public boolean sendCorrectProductIds(Pair<Pair<User, GameSession>, Pair<ArrayList<Integer>, Boolean>> dataSend) {
        try {
            DataTransferObject<Pair<Pair<User, GameSession>, Pair<ArrayList<Integer>, Boolean>>> dto = new DataTransferObject<>("SendCorrectProductIds", dataSend);
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
}