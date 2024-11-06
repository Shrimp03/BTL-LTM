package server.controller;

import model.DataTransferObject;
import model.GameSession;
import model.User;
import model.UserStatus;
import server.controller.threadManager.ThreadManager;
import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;
    private User user;
    private UserDAO userDAO = new UserDAOImpl();
    private ObjectOutputStream oos;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                try {
                    DataTransferObject<?> request = (DataTransferObject<?>) ois.readObject();
                    System.out.println("Received request from client: " + request);

                    // Xử lý yêu cầu ngắt kết nối
                    if ("DISCONNECT".equals(request.getType())) {
                        if (user != null) {
                            user.setStatus(UserStatus.OFFLINE);
                            ThreadManager.removeUserThread(user);
                        }
                        System.out.println("Client requested disconnect. Closing connection...");
                        break; // Thoát khỏi vòng lặp để ngắt kết nối
                    }

                    DataTransferObject<?> response = RequestDispatcher.dispatch(request);

                    if ("Login".equals(request.getType()) && "SUCCESS".equals(response.getType())) {
                        setUser((User) response.getData());
                    }

                    sendEvent(response);
                } catch (EOFException e) {
                    if (user != null) {
                        user.setStatus(UserStatus.OFFLINE);
                    }
                    userDAO.updateUser(user);
                    System.out.println("Client has closed the connection unexpectedly.");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during communication with client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Server socket closed");
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    // Thêm phương thức sendEvent để gửi dữ liệu đến client
    public void sendEvent(DataTransferObject<?> event) {
        try {
            if (oos != null) {
                oos.writeObject(event);
                oos.flush();
                System.out.println("Event sent to client: " + event);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi gửi sự kiện đến client: " + e.getMessage());
        }
    }
}