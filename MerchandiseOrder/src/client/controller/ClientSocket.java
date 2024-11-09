package client.controller;

import client.view.GameRoomInvitationScreen;
import client.view.PopupInvite;
import dto.UserStatusDto;
import model.DataTransferObject;
import model.Product;
import model.User;
import client.view.GameSoloListener;
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
    private final ArrayList<Pair<GameSoloListener, User>> listeners = new ArrayList<>();
    private Boolean accepted = false;

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
                        DataTransferObject<?> res = (DataTransferObject<?>) response;
                    switch (res.getType()) {
                        case "INVITE":
                            DataTransferObject<List<User>> resInvite = (DataTransferObject<List<User>>) res;
                            PopupInvite.showInvitationDialog(resInvite.getData().get(0), resInvite.getData().get(1));
                            break;

                        case "ACCEPT":
                            setAccepted(true);
                            break;

                        case "DECLINE":
                            setAccepted(false);
                            System.out.println("DECLINE");
                            break;

                        case "PLAY":
                            DataTransferObject<Pair<GameSession, User>> resPlay = (DataTransferObject<Pair<GameSession, User>>) res;
                            System.out.println("pair");
                            System.out.println(resPlay.getData().getFirst().getUser2());
                            break;
                        case "Trash":
                            System.out.println("trash");
                        case "BroadCastProductIds":
                            Pair<Pair<User, GameSession>, ArrayList<Integer>> dataReceived = (Pair<Pair<User, GameSession>, ArrayList<Integer>>) dto.getData();
                            User nextUser = dataReceived.getFirst().getFirst();
                            GameSession gameSession = dataReceived.getFirst().getSecond();
                            ArrayList<Integer> productIds = dataReceived.getSecond();

                            for (Pair<GameSoloListener, User> p : listeners) {
                                if (p.getSecond().equals(gameSession.getUser1()) || p.getSecond().equals(gameSession.getUser2())) {
                                    p.getFirst().onProductOrderReceived(new Pair<>(nextUser, productIds));
                                }
                            }
                            break;
                        case "GameSoloFinish":
                            Pair<User, GameSession> dataRecived = (Pair<User, GameSession>) dto.getData();
                            User winner = dataRecived.getFirst();
                            GameSession gameSession = dataRecived.getSecond();
                            for (Pair<GameSoloListener, User> p : listeners) {
                                if (p.getSecond().equals(gameSession.getUser1()) || p.getSecond().equals(gameSession.getUser2())) {
                                    p.getFirst().onFinishGame(winner);
                                }
                            }
                            break;

                        default:
                            messageQueue.put(response);
                            break;
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
            if(response instanceof DataTransferObject<?>){
                DataTransferObject<User> res = (DataTransferObject<User>) response;

                if("ALREADY_LOGGED_IN".equals(res.getType())){
                    return null;
                }
                if ("SUCCESS".equals(res.getType())) {
                    return res.getData();  // Trả về User nếu đăng nhập thành công
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean logoutUser(User user) {
        try {
            // Tạo đối tượng DataTransferObject để gửi yêu cầu logout
            DataTransferObject<User> dto = new DataTransferObject<>("Logout", user);
            Client.oos.writeObject(dto); // Gửi yêu cầu logout tới server
            Client.oos.flush();
            Object response = getNextMessage();
           if(response instanceof DataTransferObject<?>){
               // Nhận phản hồi từ server
               DataTransferObject<Boolean> res = (DataTransferObject<Boolean>) response;
               if (!res.getType().equals("Logout response")){
                   return false;
               }
               return res.getData(); // Trả về kết quả từ server
           }
        } catch (IOException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false; // Trả về false nếu có lỗi xảy ra hoặc logout không thành công
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
                    System.out.println("size user status online");
                    System.out.println(users.size());

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

    public void sendInvite(User onlineUser, User inviter) {
        try {
            // Tạo đối tượng truyền dữ liệu yêu cầu danh sách người dùng
            List<User> twoUser =  new ArrayList<>();
            twoUser.add(onlineUser);
            twoUser.add(inviter);
            DataTransferObject<?> dto = new DataTransferObject<>("INVITE", twoUser);

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAcceptInvite(String responseType, List<User> twoUser) {
        try {
            DataTransferObject<?> dto = new DataTransferObject<>(responseType, twoUser);

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlay(String responseType, List<User> twoUser) {
        try {
            DataTransferObject<?> dto = new DataTransferObject<>(responseType, twoUser);

            // Gửi yêu cầu tới server
            Client.oos.writeObject(dto);
            Client.oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAccepted(Boolean accepted){
         this.accepted = accepted;
    }

    public Boolean getAccepted(){
        return accepted;
    }
}