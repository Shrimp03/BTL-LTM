package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;  // Thêm nút Register

    private Client client;  // Dùng để điều hướng giữa các màn hình

    public LoginScreen(Client client) {
        this.client = client;
        setLayout(new GridLayout(4, 2, 10, 10));
        setPreferredSize(new Dimension(385, 685));  // Kích thước khung giống PlayScreen

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Đăng nhập");
        registerButton = new JButton("Đăng kí");

        add(new JLabel("Tên đăng nhập:"));
        add(usernameField);
        add(new JLabel("Mật khẩu:"));
        add(passwordField);
        add(loginButton);
        add(registerButton);  // Thêm nút Register vào giao diện

        // Thêm sự kiện khi nhấn nút "Login"
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Gửi yêu cầu đăng nhập qua ClientSocket
                ClientSocket clientSocket = new ClientSocket();
                User user = clientSocket.loginUser(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                    client.setCurrentUser(user);  // Lưu thông tin người dùng
                    client.showHomeScreen();  // Chuyển sang màn hình HomeScreen nếu đăng nhập thành công
                } else {
                    JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không đúng");
                }
            }
        });

        // Thêm sự kiện khi nhấn nút "Register" để chuyển sang màn hình đăng ký
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.showRegisterScreen();  // Chuyển sang màn hình đăng ký
            }
        });
    }
}
