package client.view;

import client.controller.Client;
import client.controller.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client clientSocket; // Thay đổi UserDAO thành ClientSocket

    public LoginScreen(Main mainFrame, Client clientSocket) {
        this.clientSocket = clientSocket; // Khởi tạo clientSocket
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        createUIComponents(gbc);
        addLoginButtonActionListener(mainFrame);
        addRegisterButtonActionListener(mainFrame);
    }

    private void createUIComponents(GridBagConstraints gbc) {
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        add(registerButton, gbc);
    }

    private void addLoginButtonActionListener(Main mainFrame) {
        JButton loginButton = (JButton) getComponent(4);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty.");
                    return;
                }

                // Xác thực người dùng thông qua ClientSocket
                if (clientSocket.authenticateUser(username, password)) {
                    mainFrame.showMainScreen(username); // Hiển thị giao diện chính với tên người dùng
                } else {
                    JOptionPane.showMessageDialog(null, "Username or password is incorrect, please try again.");
                }
            }
        });
    }

    private void addRegisterButtonActionListener(Main mainFrame) {
        JButton registerButton = (JButton) getComponent(5);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegisterScreen(); // Chuyển đến giao diện đăng ký
            }
        });
    }
}
