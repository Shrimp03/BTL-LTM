package server.controller;

import server.dal.dao.UserDAO;
import server.dal.dao.UserDAOImpl;

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
             BufferedReader in = new BufferedReader(new InputStreamReader(input));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Received command: " + command);
                if (command.equals("REGISTER")) {
                    String username = in.readLine();
                    String password = in.readLine();
                    String email = in.readLine();
                    boolean isRegistered = userDAO.register(username, password, email); // Sử dụng userDAO
                    out.println(isRegistered ? "Registration Successful" : "Registration Failed");
                } else if (command.equals("LOGIN")) {
                    String username = in.readLine();
                    String password = in.readLine();
                    boolean isLoggedIn = userDAO.login(username, password); // Sử dụng userDAO
                    out.println(isLoggedIn ? "Login Successful" : "Login Failed");
                }
            }
        } catch (IOException e) {
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
