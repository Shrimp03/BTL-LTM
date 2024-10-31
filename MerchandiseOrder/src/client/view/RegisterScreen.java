package client.view;

import client.controller.Client;
import client.controller.ClientSocket;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;  // Thêm nút quay lại màn hình đăng nhập
    private ClientSocket clientSocket;
    private Client client;  // Dùng để điều hướng giữa các màn hình

    public RegisterScreen(Client client) {
        this.client = client;
        setLayout(new GridLayout(5, 20, 10, 10));
        setPreferredSize(new Dimension(385, 685));  // Kích thước khung giống PlayScreen

        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        emailField = new JTextField(10);
        registerButton = new JButton("Đăng kí");
        backButton = new JButton("Trở về trang đăng nhập");  // Thêm nút quay lại màn hình đăng nhập

        add(new JLabel("Tên tài khoản:"));
        add(usernameField);
        add(new JLabel("Mật khẩu:"));
        add(passwordField);
        add(new JLabel("Email:"));
        add(emailField);
        add(registerButton);
        add(backButton);  // Thêm nút Back to Login vào giao diện

        // Thêm sự kiện khi nhấn nút "Register"
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                // Gửi yêu cầu đăng ký qua ClientSocket
                ClientSocket clientSocket = new ClientSocket();
                Boolean success = clientSocket.registerUser(new User(username, password, email));
                if (success) {
                    JOptionPane.showMessageDialog(null, "Đăng kí thành công!");
                    client.showLoginScreen();  // Chuyển về màn hình đăng nhập nếu đăng ký thành công
                } else {
                    JOptionPane.showMessageDialog(null, "Đăng kí thất bại!");
                }
            }
        });

        // Thêm sự kiện khi nhấn nút "Đăng kí thành công" để quay lại màn hình đăng nhập
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.showLoginScreen();  // Quay lại màn hình đăng nhập
            }
        });
    }
}
