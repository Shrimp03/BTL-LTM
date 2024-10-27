package client.controller;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
//    public static void main(String[] args) {
//        try (Socket socket = new Socket("localhost", 1234)) {
//            OutputStream output = socket.getOutputStream();
//            PrintWriter writer = new PrintWriter(output, true);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }




    public boolean authenticateUser(String username, String password) {
        try {
            // Gửi yêu cầu đăng nhập đến Server
            out.println("LOGIN");
            out.println(username);
            out.println(password);
            String response = in.readLine();
            return "Login Successful".equals(response); // Kiểm tra phản hồi từ Server
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean registerUser(String username, String password, String email) {
        try {
            // Gửi yêu cầu đăng ký đến Server
            out.println("REGISTER");
            out.println(username);
            out.println(password);
            out.println(email);
            String response = in.readLine();
            return "Registration Successful".equals(response); // Kiểm tra phản hồi từ Server
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}

