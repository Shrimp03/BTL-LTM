package Client.View;

import Client.Controller.Client;
import Client.Controller.Main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private Client clientSocket; // Đối tượng để giao tiếp với Server

    public RegisterScreen(Main mainFrame, Client clientSocket) {
        this.clientSocket = clientSocket; // Khởi tạo clientSocket
        setLayout(null); // Để có thể thiết lập vị trí

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 25);
        add(passwordField);

        // Thêm trường nhập email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 110, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 110, 150, 25);
        add(emailField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(30, 150, 100, 30);
        add(registerButton);

        // Thêm nút Quay lại trang đăng nhập
        JButton backButton = new JButton("Back");
        backButton.setBounds(130, 150, 100, 30);
        add(backButton);

        // Action listener cho nút đăng ký
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText(); // Lấy email từ trường nhập

                // Gọi đến Server để đăng ký
                if (registerUser(username, password, email)) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    mainFrame.showLoginScreen(); // Chuyển về giao diện đăng nhập
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed, please try again.");
                }
            }
        });

        // Action listener cho nút Quay lại
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLoginScreen(); // Chuyển về giao diện đăng nhập
            }
        });
    }

    private boolean registerUser(String username, String password, String email) {
        return clientSocket.registerUser(username, password, email); // Gọi đến phương thức đăng ký trong ClientSocket
    }
}
