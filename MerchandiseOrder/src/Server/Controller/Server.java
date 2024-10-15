package Server.Controller;

import Server.Dal.DAO.UserDAO;  // Import đúng UserDAO
import Server.Dal.DAO.UserDAOImpl; // Import đúng UserDAOImpl
import Utils.PasswordUtil;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ServerThread(clientSocket).start(); // Khởi động luồng cho mỗi client kết nối
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;
    private UserDAO userDAO;

    public ServerThread(Socket socket) {
        this.socket = socket;
        this.userDAO = new UserDAOImpl(); // Khởi tạo UserDAO
    }

    public void run() {
        try (InputStream input = socket.getInputStream();
             ObjectInputStream in = new ObjectInputStream(input);
             OutputStream output = socket.getOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(output)) {

            String command;
            while ((command = (String) in.readObject()) != null) {
                System.out.println("Received command: " + command);
                if (command.equals("REGISTER")) {
                    String username = (String) in.readObject();
                    String password = (String) in.readObject();
                    String hashedPassword = PasswordUtil.hashPassword(password);
                    boolean isRegistered = userDAO.register(username, hashedPassword); // Sử dụng userDAO
                    out.writeObject(isRegistered ? "Registration Successful" : "Registration Failed");
                } else if (command.equals("LOGIN")) {
                    String username = (String) in.readObject();
                    String password = (String) in.readObject();
                    String hashedPassword = PasswordUtil.hashPassword(password); // Mã hóa mật khẩu
                    boolean isLoggedIn = userDAO.login(username, hashedPassword); // Sử dụng userDAO
                    out.writeObject(isLoggedIn ? "Login Successful" : "Login Failed");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close(); // Đóng kết nối khi hoàn tất
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
